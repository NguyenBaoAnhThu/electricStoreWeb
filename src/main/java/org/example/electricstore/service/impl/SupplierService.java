package org.example.electricstore.service.impl;

import org.example.electricstore.exception.supplier.SupplierError;
import org.example.electricstore.exception.supplier.SupplierException;
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
import java.util.regex.Pattern;

@Service
public class SupplierService implements ISupplierService {

    @Autowired
    private ISupplierRepository supplierRepository;

    // Biểu thức chính quy để validate
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÀ-ỹ\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isEmpty()) {
            throw new SupplierException(SupplierError.SUPPLIER_NOT_FOUND);
        }
        return supplier;
    }

    @Override
    public Supplier addSupplier(Supplier supplier) {
        // Validate các trường thông tin
        validateSupplier(supplier);

        // Kiểm tra trùng lặp
        if (supplierRepository.existsByPhone(supplier.getPhone())) {
            throw new SupplierException(SupplierError.DUPLICATE_PHONE);
        }

        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new SupplierException(SupplierError.DUPLICATE_EMAIL);
        }

        if (supplierRepository.existsBySupplierCode(supplier.getSupplierCode())) {
            throw new SupplierException(SupplierError.DUPLICATE_SUPPLIER_CODE);
        }

        // Nếu không có supplierCode, tạo mã tự động
        if (supplier.getSupplierCode() == null || supplier.getSupplierCode().isEmpty()) {
            supplier.setSupplierCode(generateNewSupplierCode());
        }

        supplier.setCreateAt(LocalDateTime.now());
        supplier.setUpdateAt(LocalDateTime.now());
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplierDetails) {
        // Validate các trường thông tin
        validateSupplier(supplierDetails);

        return supplierRepository.findById(id)
                .map(supplier -> {
                    // Kiểm tra trùng lặp với các supplier khác
                    if (!supplier.getPhone().equals(supplierDetails.getPhone()) &&
                            supplierRepository.existsByPhoneAndSupplierIDNot(supplierDetails.getPhone(), id)) {
                        throw new SupplierException(SupplierError.DUPLICATE_PHONE);
                    }

                    if (!supplier.getEmail().equals(supplierDetails.getEmail()) &&
                            supplierRepository.existsByEmailAndSupplierIDNot(supplierDetails.getEmail(), id)) {
                        throw new SupplierException(SupplierError.DUPLICATE_EMAIL);
                    }

                    supplier.setSupplierName(supplierDetails.getSupplierName());
                    supplier.setAddress(supplierDetails.getAddress());
                    supplier.setPhone(supplierDetails.getPhone());
                    supplier.setEmail(supplierDetails.getEmail());
                    supplier.setSupplierCode(supplierDetails.getSupplierCode());
                    supplier.setUpdateAt(LocalDateTime.now());
                    return supplierRepository.save(supplier);
                }).orElseThrow(() -> new SupplierException(SupplierError.SUPPLIER_NOT_FOUND));
    }

    private void validateSupplier(Supplier supplier) {
        // Validate tên nhà cung cấp
        if (supplier.getSupplierName() == null || supplier.getSupplierName().trim().isEmpty()) {
            throw new SupplierException(SupplierError.INVALID_NAME_FORMAT, "Tên nhà cung cấp không được để trống");
        }

        if (!NAME_PATTERN.matcher(supplier.getSupplierName()).matches()) {
            throw new SupplierException(SupplierError.INVALID_NAME_FORMAT);
        }

        if (supplier.getSupplierName().length() < 2 || supplier.getSupplierName().length() > 100) {
            throw new SupplierException(SupplierError.INVALID_NAME_FORMAT, "Tên nhà cung cấp phải có độ dài từ 2 đến 100 ký tự");
        }

        // Validate địa chỉ
        if (supplier.getAddress() == null || supplier.getAddress().trim().isEmpty()) {
            throw new SupplierException(SupplierError.INVALID_ADDRESS_FORMAT, "Địa chỉ không được để trống");
        }

        if (supplier.getAddress().length() < 5 || supplier.getAddress().length() > 200) {
            throw new SupplierException(SupplierError.INVALID_ADDRESS_FORMAT, "Địa chỉ phải có độ dài từ 5 đến 200 ký tự");
        }

        // Validate số điện thoại
        if (supplier.getPhone() == null || supplier.getPhone().trim().isEmpty()) {
            throw new SupplierException(SupplierError.INVALID_PHONE_FORMAT, "Số điện thoại không được để trống");
        }

        if (!PHONE_PATTERN.matcher(supplier.getPhone()).matches()) {
            throw new SupplierException(SupplierError.INVALID_PHONE_FORMAT);
        }

        // Validate email
        if (supplier.getEmail() == null || supplier.getEmail().trim().isEmpty()) {
            throw new SupplierException(SupplierError.INVALID_EMAIL_FORMAT, "Email không được để trống");
        }

        if (!EMAIL_PATTERN.matcher(supplier.getEmail()).matches()) {
            throw new SupplierException(SupplierError.INVALID_EMAIL_FORMAT);
        }

        if (supplier.getEmail().length() < 5 || supplier.getEmail().length() > 100) {
            throw new SupplierException(SupplierError.INVALID_EMAIL_FORMAT, "Email phải có độ dài từ 5 đến 100 ký tự");
        }
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
        return supplierRepository.searchByFields(supplierCode, supplierName, null, phone, null, pageable);
    }
}