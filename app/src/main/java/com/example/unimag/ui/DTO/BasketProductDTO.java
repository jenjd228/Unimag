package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketProductDTO {

    private String productHash;

    private String mainImage;

    private String category;

    private Integer price;

    private String title;

    private String descriptions;

    private Integer count;

    private String color;

    private String size;
}
