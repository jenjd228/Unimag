package com.example.unimag.ui.partner_program;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.PartnerProgramDTO;

import java.util.ArrayList;

public class RecyclerAdapterPartnerProgram extends RecyclerView.Adapter<RecyclerAdapterPartnerProgram.PartnerProgramViewHolder> {

    private ArrayList<PartnerProgramDTO> listData; //Лист со списком партнеров


    //Конструктор адаптера
    //На вход принимает список партнеров
    public RecyclerAdapterPartnerProgram(ArrayList<PartnerProgramDTO> listData) {
        this.listData = listData;
    }


    //Своеобразный класс View(или item) нашего партнера (здесь объявляется и находятся все нужные view-элементы для дальнейшей работы)
    //Здесь мы ссылаемся на разметку View из partner_program_item_layout.xml, откуда достаем нужные нам view элементы
    //для создания Item партнера в методе onCreateViewHolder (смотреть ниже)
    public class PartnerProgramViewHolder extends RecyclerView.ViewHolder {

        LinearLayout partnerLayout; //Layout item'а партнера (весь блок с картинкой, названием и описанием)
        ImageView imagePartner; //Логотип партнера
        TextView titlePartner; //TextView с названием партнера
        TextView descriptionPartner; //Описание скидки партнера

        //Конструктор класса
        //На вход принимает View-элемент (нашу разметку из partner_program_item_layout.xml)
        public PartnerProgramViewHolder(View view) {
            super(view);

            partnerLayout = view.findViewById(R.id.partner_layout);
            imagePartner = view.findViewById(R.id.image_partner_program);
            titlePartner = view.findViewById(R.id.text_title_partner_program);
            descriptionPartner = view.findViewById(R.id.description_partner_program);
        }

    }


    //Создает View-разметку (item) для данного партнера
    @NonNull
    @Override
    public PartnerProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Создаем view нашего партнера, который потом добавляется в RecyclerView как item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.partner_program_item_layout, parent, false);
        return new PartnerProgramViewHolder(view);
    }


    //Заполняет содержимое созданной разметки для партнера
    //P.S. holder - это грубо говоря наше View
    //     position - это ТЕКУЩИЙ индекс партнера в списке listData
    @Override
    public void onBindViewHolder(@NonNull PartnerProgramViewHolder holder, int position) {
        //Сделать новую папку для фоток партнеров и прогружать их здесь
        //holder.imagePartner.setImageResources( /* Здесь достать фотку с сервера */ )
        holder.titlePartner.setText(listData.get(position).getTitle());
        holder.descriptionPartner.setText(listData.get(position).getDescription());
    }


    //Возвращает кол-во элементов в списке
    @Override
    public int getItemCount() {
        return listData.size();
    }

}
