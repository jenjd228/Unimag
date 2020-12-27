package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order2ProductDTO {

    private Integer productId;

    private Integer count;

    private String size;

    private String imageName;

    private String category;

    private Integer price;

    private String title;
}
