package com.example.unimag.ui.DTO;

import java.util.List;

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

    private List<Order2ProductDTO> order2ProductsList;
}
