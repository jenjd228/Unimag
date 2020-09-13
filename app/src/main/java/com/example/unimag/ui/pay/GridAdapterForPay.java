package com.example.unimag.ui.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.BasketProductDTO;
import com.example.unimag.ui.DTO.PayDTO;
import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.basket.GridAdapterBasket;

import java.util.ArrayList;
import java.util.List;

public class GridAdapterForPay extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private String secureKod;
    private List<PayDTO> listFromBasket;


    GridAdapterForPay(Context context , List<PayDTO> listProduct, String secureKod){
        this.listFromBasket = listProduct;
        this.secureKod = secureKod;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listFromBasket.size();
    }

    @Override
    public Object getItem(int position) {
        return listFromBasket.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        GridAdapterForPay.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.order_for_pay_item_layout, null);
            holder = new GridAdapterForPay.ViewHolder();
            holder.orderLayout = (LinearLayout) convertView.findViewById(R.id.order_layout);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_order);
            holder.productPrice = (TextView) convertView.findViewById(R.id.cost);
            holder.productTitle= (TextView) convertView.findViewById(R.id.title_order);
            //holder.productDescription = (TextView) convertView.findViewById(R.id.description_basket_product);
            holder.productCount = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (GridAdapterForPay.ViewHolder) convertView.getTag();
        }

        PayDTO product = this.listFromBasket.get(position);
        holder.productTitle.setText(product.getTitle());
        //holder.productDescription.setText("" + product.getDescriptions());
        holder.productPrice.setText("" + product.getPrice()*product.getCount());
        holder.productCount.setText("" + product.getCount());

        if(position%2==0) {
            holder.orderLayout.setBackgroundColor(Color.parseColor("#dadfe0"));
        }

        Glide.with(convertView).load("http://"+ GlobalVar.ip +":8080/upload/"+product.getImageName()).into(holder.imageView);

        return convertView;
    }

    public void addList(List<PayDTO> listData){
        this.listFromBasket.addAll(listData);
        notifyDataSetChanged();
    }

    public List<PayDTO> getProductList(){
        return listFromBasket;
    }

    static class ViewHolder {
        ImageView imageView;

        TextView productTitle;
        TextView productPrice;
        TextView productCount;

        LinearLayout orderLayout;

    }

}
