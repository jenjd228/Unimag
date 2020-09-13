package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketProductDTO {

    private Integer productId;

    private String imageName;

    private String category;

    private Integer price;

    private String title;

    private String descriptions;

    private Integer count;
}
