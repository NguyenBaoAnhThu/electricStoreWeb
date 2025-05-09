package org.example.electricstore.DTO.order;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Integer id;
    private String invoiceNumber;

    @Valid
    private List<ProductOrderDTO> productOrderDTOList = new ArrayList<>();

    @NotNull(message = "Phương thức thanh toán không được để trống !")
    @Min(value = 1, message = "Phương thức thanh toán không hợp lệ !")
    @Max(value = 2, message = "Phương thức thanh toán không hợp lệ !")
    private Integer paymentMethod;

    @Valid
    @JsonIgnore
    private CustomerDTO customerDTO;

    private Integer customerId;
    private Boolean isPrintInvoice;
    private Integer totalPrice;


    private Double discountAmount;
    private Double discountPercent;
    private OrderStatus status;

    public OrderDTO() {
        productOrderDTOList.add(new ProductOrderDTO());
        customerDTO = new CustomerDTO();
        totalPrice = 0;
        isPrintInvoice = false;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", productOrderDTOList=" + productOrderDTOList +
                ", paymentMethod=" + paymentMethod +
                ", customerDTO=" + customerDTO +
                ", status=" + status +
                ", isPrintInvoice=" + isPrintInvoice +
                '}';
    }
}