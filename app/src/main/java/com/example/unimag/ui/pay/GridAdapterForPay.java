package com.example.unimag.ui.pay;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.unimag.ui.DTO.BasketProductDTO;

import java.util.List;

public class GridAdapterForPay extends BaseAdapter {

    private List<BasketProductDTO> listData;

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
