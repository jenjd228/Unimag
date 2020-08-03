package com.example.unimag.ui.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Date dateOfOrder;
}
