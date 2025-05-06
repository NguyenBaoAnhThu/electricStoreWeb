package org.example.electricstore.service.impl;

import org.example.electricstore.model.Supplier;
import org.example.electricstore.repository.ISupplierRepository;
import org.example.electricstore.service.interfaces.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements ISupplierService {

    @Autowired
    private ISupplierRepository supplierRepository;

    @Override
    public Page<Supplier> getSuppliers(int page, int size) {
        return supplierRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> getSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier addSupplier(Supplier supplier) {
        // Nếu không có supplierCode, tạo mã tự động
        if (supplier.getSupplierCode() == null || supplier.getSupplierCode().isEmpty()) {
            supplier.setSupplierCode(generateNewSupplierCode());
        }

        supplier.setCreateAt(LocalDateTime.now());
        supplier.setUpdateAt(LocalDateTime.now());
        return supplierRepository.save(supplier);
    }

    public String generateNewSupplierCode() {
        String maxSupplierCode = supplierRepository.findMaxSupplierCode();

        if (maxSupplierCode == null) {
            return "NCC0001";
        } else {
            // Trích xuất số từ mã hiện tại (lấy phần sau "NCC")
            int currentNumber = Integer.parseInt(maxSupplierCode.substring(3));
            // Tạo mã mới với số tăng lên 1
            return String.format("NCC%04d", currentNumber + 1);
        }
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplierDetails) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplier.setSupplierName(supplierDetails.getSupplierName());
                    supplier.setAddress(supplierDetails.getAddress());
                    supplier.setPhone(supplierDetails.getPhone());
                    supplier.setEmail(supplierDetails.getEmail());
                    supplier.setSupplierCode(supplierDetails.getSupplierCode());
                    supplier.setUpdateAt(LocalDateTime.now());
                    return supplierRepository.save(supplier);
                }).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @Override
    @Transactional
    public void deleteSuppliers(List<Integer> ids) {
        supplierRepository.deleteBySupplierIDIn(ids);
    }

    @Override
    public List<Supplier> searchSuppliersByAllFields(
            String supplierCode,
            String supplierName,
            String address,
            String phone,
            String email) {
        return List.of();
    }

    @Override
    public Page<Supplier> searchSuppliersByAllFields(
            String supplierCode,
            String supplierName,
            String address,
            String phone,
            String email,
            Pageable pageable) {
        return supplierRepository.searchByFields(supplierCode, supplierName, address, phone, email, pageable);
    }
}