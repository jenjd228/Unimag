package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayDTO {

    private String imageName;

    private Integer price;

    private String title;

    private Integer count;

}
