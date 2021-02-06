package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {

    private String hash;

    private String mainImage;

    private String category;

    private Integer price;

    private String title;

    private String descriptions;

    private String date;

    private String listImage;
}
