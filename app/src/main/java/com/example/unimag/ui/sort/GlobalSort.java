package com.example.unimag.ui.sort;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public final class GlobalSort {

    private static GlobalSort instance;

    private Integer sortByPricePosition;

    private Integer spinnerItemPosition = 0;

    private String sortByPriceString = "null"; // sortByPriceASC sortByPriceDESC

    private String spinnerSortItemNameCategory = "null"; // категории

    private Boolean sortByTime = false;

    public static GlobalSort getInstance() {
        if (instance == null) {
            instance = new GlobalSort();
        }
        return instance;
    }
}
