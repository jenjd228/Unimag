package com.example.unimag.ui.partner_program;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.PartnerProgramDTO;
import com.example.unimag.ui.GlobalVar;

import java.util.List;

public class GridAdapterPartnerProgram extends BaseAdapter {

    private List<PartnerProgramDTO> listData; //Лист со списком партнеров
    private LayoutInflater layoutInflater; //
    private Context context; //Контекст страницы


    //Конструктор класса
    public GridAdapterPartnerProgram(Context context, List<PartnerProgramDTO> listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }


    //Возвращает кол-во элементов в списке
    @Override
    public int getCount() {
        return listData.size();
    }


    //Возвращает элемент списка по данному int айди
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }


    //Возвращает id данного элемента
    @Override
    public long getItemId(int position) {
        return position;
    }


    //Создание View для данного item'а (элемента списка)
    //P.S. position - номер элемента списка
    //     converView - создаваемый View
    //     parent - родитель данного View?
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder; //Части View создаваемого элемента (см. ниже на анонимный класс ViewHolder)

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.partner_program_item_layout, null);

            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.image_partner_program);
            holder.titleView = (TextView) convertView.findViewById(R.id.text_title_partner_program);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description_partner_program);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PartnerProgramDTO partnerProgramDTO = this.listData.get(position); //Получение объекта списка
        //Загружаем фото партнера
        Glide.with(convertView)
                .load("http://"+ GlobalVar.ip +":8080/upload/image_partner/"+listData.get(position).getImageName()) //Откуда загружаем
                .into(holder.imageView); //Куда загружаем
        //Заполняем поля
        holder.titleView.setText(partnerProgramDTO.getTitle());
        holder.descriptionView.setText(partnerProgramDTO.getDescription());

        return convertView;
    }


    //Части создаваемого элемента
    static class ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
    }

}
