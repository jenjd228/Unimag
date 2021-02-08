package com.example.unimag.ui.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayDTO {

    private String productHash;

    private String mainImage;

    private Integer price;

    private String title;

    private Integer count;

    private String size;

    @Override
    public String toString() {
        return "{" +
                "\"productHash\":" + productHash +
                ",\"imageName\":\"" + mainImage + '\"' +
                ",\"price\":" + price +
                ",\"title\":\"" + title + '\"' +
                ",\"count\":" + count +
                ",\"size\":\"" + size + '\"' +
                '}';
    }
}
