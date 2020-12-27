package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {

    private Integer orderId;

    private String dataOfOrder;

    private String status;

    private String pickUpPoint;
}
