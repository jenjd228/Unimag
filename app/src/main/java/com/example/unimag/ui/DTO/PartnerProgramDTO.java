package com.example.unimag.ui.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProgramDTO {

    private Integer id; //id партнера

    private String imageName; //Название картинки с логотипом партнера

    private String title; //Название партнера

    private String description; //Описание скидки партнера

    private BigDecimal price; //Цена подписки

}
