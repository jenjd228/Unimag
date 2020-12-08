package com.example.unimag.ui.Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.OrdersDTO;

import java.util.List;


public class GridAdapterOrder extends BaseAdapter {

    private List<OrdersDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public GridAdapterOrder(Context aContext,  List<OrdersDTO> listData) { //Конструктор нашего Grid'a
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() { //Кол-во элементов Grid'а
        return listData.size();
    }

    @Override
    public Object getItem(int position) { //Похоже на полуение позиции gridItem'а
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) { //Поучение номера нашего Item'а
        return position;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.order_item_layout, null);
            holder = new ViewHolder();
            //Поиск всех нужных элементов
            holder.orderView = convertView.findViewById(R.id.order_layout);
            holder.imageOrderView = convertView.findViewById(R.id.image_order);
            holder.orderIdView = convertView.findViewById(R.id.id_order);
            holder.dataOfOrderView = convertView.findViewById(R.id.data_of_order);
            holder.statusView = convertView.findViewById(R.id.status_of_order);
            holder.pickUpPoint = convertView.findViewById(R.id.pick_up_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrdersDTO mainOrder = this.listData.get(position);

        //Установка нужных значений для элементов
        holder.orderIdView.setText("" +mainOrder.getOrderId());
        holder.dataOfOrderView.setText("" + mainOrder.getDataOfOrder());
        holder.statusView.setText("" + mainOrder.getStatus());
        holder.pickUpPoint.setText("" + mainOrder.getPickUpPoint());

        //Перекрас фона Item'а
        if(position%2==0) {
            holder.orderView.setBackgroundColor(Color.parseColor("#dadfe0"));
        }

        //Получение полного пути изображения
        //int imageId = this.getMipmapResIdByName(order.getImageName());
        //Вставка изображения
        //holder.imageOrderView.setImageResource(imageId);
        //Glide.with(convertView).load("http://"+ GlobalVar.ip +":8080/upload/"+mainOrder.getImageName()).into(holder.imageOrderView);

        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addList(List<OrdersDTO> listData){
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    //Функция, создающая полный путь до нашего изображения в mipmap
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("GridViewOrder", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    public List<OrdersDTO> getListData() {
        return listData;
    }

    //Класс со всеми нашими View элементами данного Item'а
    static class ViewHolder {
        LinearLayout orderView;
        ImageView imageOrderView;
        TextView orderIdView;
        TextView dataOfOrderView;
        TextView statusView;
        TextView pickUpPoint;
    }

}
