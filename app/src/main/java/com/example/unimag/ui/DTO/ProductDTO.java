package com.example.unimag.ui.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    protected Integer id;

    protected String imageName;

    protected String category;

    protected Integer price;

    protected String title;

    protected String descriptions;

    protected Date date;


}
