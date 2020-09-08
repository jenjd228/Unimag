package com.example.unimag.ui.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer productId;

    private String imageName;

    private String category;

    private Integer price;

    private String title;

    private String descriptions;

    private Date date;

    private String dataOfOrder; //Дата заказа

    private String status; //Статус товара

}