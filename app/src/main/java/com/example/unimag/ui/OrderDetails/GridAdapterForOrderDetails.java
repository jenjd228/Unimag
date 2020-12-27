package com.example.unimag.ui.OrderDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.Order2ProductDTO;
import com.example.unimag.ui.GlobalVar;

import java.util.List;

public class GridAdapterForOrderDetails extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Order2ProductDTO> list;


    GridAdapterForOrderDetails(Context context, List<Order2ProductDTO> listProduct) {
        this.list = listProduct;
        layoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        GridAdapterForOrderDetails.ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.order_for_pay_item_layout, null);
            holder = new GridAdapterForOrderDetails.ViewHolder();

            //holder.orderLayout = convertView.findViewById(R.id.order_layout);

            holder.imageView = convertView.findViewById(R.id.image_order);
            holder.productPrice = convertView.findViewById(R.id.cost);
            holder.productTitle = convertView.findViewById(R.id.title_order);
            holder.productSize = convertView.findViewById(R.id.size);
            holder.productCount = convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (GridAdapterForOrderDetails.ViewHolder) convertView.getTag();
        }

        Order2ProductDTO product = this.list.get(position);

        holder.productSize.setText(""+product.getSize());
        holder.productCount.setText(""+product.getCount());
        holder.productPrice.setText(""+product.getPrice());
        holder.productTitle.setText(""+product.getTitle());
        /*if (position % 2 == 0) {
            holder.orderLayout.setBackgroundColor(Color.parseColor("#dadfe0"));
        }*/

        Glide.with(convertView).load("http://" + GlobalVar.ip + ":8080/upload/" + product.getImageName()).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;

        TextView productTitle;
        TextView productPrice;
        TextView productCount;
        TextView productSize;

        //LinearLayout orderLayout;

    }

    public void addList(List<Order2ProductDTO> listData) {
        this.list.addAll(listData);
        System.out.println("asdasdasdasdasdasd");
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
