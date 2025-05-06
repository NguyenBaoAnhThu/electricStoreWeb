package org.example.electricstore.service.impl;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.*;
import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.mapper.order.OrderMapper;
import org.example.electricstore.model.Customer;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.OrderDetail;
import org.example.electricstore.repository.ICustomerRepository;
import org.example.electricstore.repository.IOrderDetailRepository;
import org.example.electricstore.repository.IOrderRepository;
import org.example.electricstore.repository.IProductRepository;
import org.example.electricstore.service.common.PDFService;
import org.example.electricstore.service.interfaces.IOrderService;
import org.example.electricstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    CustomerService customerService;

    @Autowired
    ICustomerRepository customerRepository;

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Autowired
    IProductService productService;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    PDFService pdfService;

    // Phương thức để tạo mã đơn hàng mới
    private String generateOrderCode() {
        // Lấy đơn hàng mới nhất để tạo mã tiếp theo
        Order latestOrder = orderRepository.findTopByOrderByOrderIDDesc();
        int nextNumber = 1;

        if (latestOrder != null && latestOrder.getCodeOrder() != null) {
            try {
                // Cắt phần "HD" đi và parse số
                String numberPart = latestOrder.getCodeOrder().substring(2);
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (Exception e) {
                // Nếu có lỗi, bắt đầu lại từ 1
                nextNumber = 1;
            }
        }

        // Format mã đơn hàng với định dạng HD00001
        return String.format("HD%05d", nextNumber);
    }


    @Override
    @Transactional
    public Integer saveOrder(OrderDTO orderDTO) {
        System.out.println("orderDTO: " + orderDTO);
        Order order = new Order();
        Double subtotal = 0.0;

        // Tạo mã đơn hàng tự động
        order.setCodeOrder(generateOrderCode());

        // Customer
        Integer customerID = orderDTO.getCustomerId();
        Customer customer = customerService.getCustomerById(customerID);
        order.setCustomer(customer);

        order.setTotalPrice(0.00);

        // Cập nhật: Đặt trạng thái đơn hàng phù hợp với phương thức thanh toán
        if(orderDTO.getPaymentMethod() == 2) {
            // Thanh toán tiền mặt - hoàn thành ngay
            order.setStatus(OrderStatus.COMPLETE);
        } else if(orderDTO.getPaymentMethod() == 1) {
            // Thanh toán chuyển khoản - đã thanh toán khi xác nhận
            order.setStatus(OrderStatus.COMPLETE);
        } else {
            // Phương thức thanh toán khác hoặc không xác định
            order.setStatus(OrderStatus.COMPLETE); // Mặc định là COMPLETE để tránh lỗi
        }

        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());

        // Lưu thông tin giảm giá
        order.setDiscount(orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : 0.0);
        order.setDiscountPercent(orderDTO.getDiscountPercent() != null ? orderDTO.getDiscountPercent() : 0.0);

        Order saveOrder = orderRepository.save(order);

        Integer orderID = saveOrder.getOrderID();

        // OrderDetails
        if (orderDTO.getProductOrderDTOList() != null && !orderDTO.getProductOrderDTOList().isEmpty()) {
            for (ProductOrderDTO productOrderDTO : orderDTO.getProductOrderDTOList()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(saveOrder);

                // Lấy sản phẩm
                org.example.electricstore.model.Product product = productService.findById(productOrderDTO.getProductId());

                // Kiểm tra xem tồn kho có đủ không
                Integer currentStock = product.getStock();
                if (currentStock == null) {
                    currentStock = 0;
                }

                if (currentStock < productOrderDTO.getQuantity()) {
                    throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng trong kho. Hiện chỉ còn " + currentStock + " sản phẩm.");
                }

                orderDetail.setProduct(product);
                orderDetail.setQuantity(productOrderDTO.getQuantity());
                orderDetail.setPrice((double)productOrderDTO.getPriceIndex());

                saveOrderDetail(orderDetail);

                // Tính tổng tiền trước khi áp dụng giảm giá
                subtotal += productOrderDTO.getQuantity() * productOrderDTO.getPriceIndex();

                // CẬP NHẬT TỒN KHO
                // Số lượng sau khi trừ đi số lượng đã đặt
                Integer newStock = currentStock - productOrderDTO.getQuantity();
                if (newStock < 0) {
                    newStock = 0; // Đảm bảo tồn kho không âm
                }

                // Cập nhật tồn kho
                product.setStock(newStock);
                productRepository.save(product);
            }
        }

        // Tính giảm giá và tổng tiền cuối cùng
        Double discountValue = 0.0;

        // Giảm giá theo VNĐ
        if (orderDTO.getDiscountAmount() != null) {
            discountValue += orderDTO.getDiscountAmount();
        }

        // Giảm giá theo %
        if (orderDTO.getDiscountPercent() != null && orderDTO.getDiscountPercent() > 0) {
            discountValue += subtotal * (orderDTO.getDiscountPercent() / 100);
        }

        // Tổng tiền sau khi đã giảm giá
        Double totalPrice = subtotal - discountValue;
        if (totalPrice < 0) {
            totalPrice = 0.0;
        }

        saveOrder.setTotalPrice(totalPrice);
        orderRepository.updateTotalPrice(orderID, totalPrice);

        return orderID;
    }

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public Page<CustomerDTO> getAllCustomersDTO(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(this::convertToDTO);
    }

    @Override
    public Order getOrderById(Integer id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public List<OrderHistoryRq> getAllOrderHistoryRqByCustomer(Customer customer) {
        return this.orderRepository.findByCustomer(customer).stream().map(
                order -> this.orderMapper.toOrderHistoryRq(order)
        ).toList();
    }

    @Transactional
    @Override
    public List<OrderDetailDTO> getAllOrderDetailDTOByCustomer(int orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found"));
        return order.getOrderDetails().stream().map(
                orderDetail -> this.orderMapper.toOrderDetailDTO(orderDetail)
        ).toList();
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getBirthDate(),
                customer.getEmail()
        );
    }

    @Override
    public List<Order> getCompletedOrders(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByStatusAndCreateAtBetween(OrderStatus.COMPLETE, startDate, endDate);
    }

    @Override
    public long getTotalCompletedOrders(LocalDateTime startDate, LocalDateTime endDate) {
        return getCompletedOrders(startDate, endDate).size();
    }

    @Override
    public double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return getCompletedOrders(startDate, endDate)
                .stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    @Transactional
    public OrderDTO getOrderDTOById(Integer orderId) {
        OrderDTO orderDTO = new OrderDTO();
        Order order = orderRepository.findById(orderId).orElse(null);
        System.out.println("order: " + order);

        if (order == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO(
                order.getCustomer().getCustomerId(),
                order.getCustomer().getCustomerName(),
                order.getCustomer().getPhoneNumber(),
                order.getCustomer().getAddress(),
                order.getCustomer().getBirthDate(),
                order.getCustomer().getEmail()
        );

        orderDTO.setId(order.getOrderID());
        orderDTO.setCustomerDTO(customerDTO);

        // Thêm mã đơn hàng vào OrderDTO
        orderDTO.setInvoiceNumber(order.getCodeOrder());

        // Thêm thông tin giảm giá
        orderDTO.setDiscountAmount(order.getDiscount());
        orderDTO.setDiscountPercent(order.getDiscountPercent());

        // Remove default productOrderDTO in productOrderDTOList
        orderDTO.getProductOrderDTOList().removeFirst();

        List<ProductOrderDTO> productOrderDTOList = order.getOrderDetails().stream().map(orderDetail -> {
            ProductOrderDTO productOrderDTO = new ProductOrderDTO();
            productOrderDTO.setProductId(orderDetail.getProduct().getProductID());
            productOrderDTO.setProductName(orderDetail.getProduct().getName());
            productOrderDTO.setPriceIndex(orderDetail.getPrice().intValue());
            productOrderDTO.setQuantity(orderDetail.getQuantity());
            return productOrderDTO;
        }).toList();

        for(ProductOrderDTO productOrderDTO : productOrderDTOList) {
            orderDTO.getProductOrderDTOList().add(productOrderDTO);
        }

        return orderDTO;
    }

    @Override
    public Page<Order> getAllOrders(String orderCode, String customerName, String status,
                                    String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // Create a specification that combines all filter criteria
        Specification<Order> spec = Specification.where(null);

        // Tìm kiếm theo mã đơn hàng (codeOrder)
        if (orderCode != null && !orderCode.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("codeOrder")), "%" + orderCode.toLowerCase() + "%"));
        }

        if (customerName != null && !customerName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("customer").get("customerName")),
                            "%" + customerName.toLowerCase() + "%"));
        }

        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status);
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), orderStatus));
            } catch (IllegalArgumentException e) {
                // Handle invalid status
            }
        }

        // Handle date range
        if (fromDate != null && !fromDate.isEmpty()) {
            try {
                LocalDateTime startDate = LocalDateTime.parse(fromDate + "T00:00:00");
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createAt"), startDate));

                if (toDate != null && !toDate.isEmpty()) {
                    LocalDateTime endDate = LocalDateTime.parse(toDate + "T23:59:59");
                    spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createAt"), endDate));
                }
            } catch (Exception e) {
                // Handle date parsing error
            }
        }

        return orderRepository.findAll(spec, pageable);
    }

    public String getNextOrderCode() {
        // Sử dụng lại logic từ phương thức generateOrderCode()
        Order latestOrder = orderRepository.findTopByOrderByOrderIDDesc();
        int nextNumber = 1;

        if (latestOrder != null && latestOrder.getCodeOrder() != null) {
            try {
                // Cắt phần "HD" đi và parse số
                String numberPart = latestOrder.getCodeOrder().substring(2);
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (Exception e) {
                // Nếu có lỗi, bắt đầu lại từ 1
                nextNumber = 1;
            }
        }

        // Format mã đơn hàng với định dạng HD00001
        return String.format("HD%05d", nextNumber);
    }
    // Thêm phương thức này vào OrderService của bạn (Nếu chưa có)
    public boolean cancelOrder(Integer orderId) {
        try {
            // Lấy đơn hàng theo ID
            Order order = getOrderById(orderId);

            if (order == null) {
                return false;
            }

            // Kiểm tra nếu đơn hàng đã hủy thì không cần xử lý
            if (order.getStatus() == OrderStatus.CANCELLED) {
                return true;
            }

            // Cập nhật trạng thái đơn hàng thành CANCELLED
            order.setStatus(OrderStatus.CANCELLED);

            // Lưu đơn hàng đã cập nhật
            orderRepository.save(order);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}