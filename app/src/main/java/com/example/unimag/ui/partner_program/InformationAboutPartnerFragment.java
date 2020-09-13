package com.example.unimag.ui.partner_program;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.pay.RegisterOrderFragment;


public class InformationAboutPartnerFragment extends Fragment {

    String imageName; //Название картинки партнера (напр.: adidas.png)
    String titlePartner; //Имя партнера
    int price; //Цены оформления подписки на партнера
    String descriptionPartner; //Описание акции партнера


    //Конструктор класса
    public InformationAboutPartnerFragment(String imageName, String titlePartner, int price, String descriptionPartner){
        this.imageName = imageName;
        this.titlePartner = titlePartner;
        this.price = price;
        this.descriptionPartner = descriptionPartner;
    }


    //Установка основной информации о партнере
    private void setInformationAboutPartner(String imageName, String titlePartner, int price, String descriptionPartner) {
        ImageView image = getView().findViewById(R.id.image_info_partner);
        Glide.with(getView()).load("http://"+ GlobalVar.ip+":8080/upload/image_partner/"+imageName).into(image);

        TextView titlePartnerView = getView().findViewById(R.id.text_title_info_partner);
        titlePartnerView.setText(titlePartner);

        TextView pricePartnerView = getView().findViewById(R.id.price_partner);
        pricePartnerView.setText(String.valueOf(price) + " рублей");

        TextView descriptionPartnerView = getView().findViewById(R.id.text_description_partner);
        descriptionPartnerView.setText(descriptionPartner);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information_about_partner, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Заполняем поля
        setInformationAboutPartner(imageName, titlePartner, price, descriptionPartner);

        Button button_in_info_partner = getView().findViewById(R.id.button_in_info_partner); //Кнопка "Оформить подписку"/"Использовать"

        //Сделать проверку (отправляем на сервер запрос к БД и смотрим офрмлена ли подписка у чела)
        //В соответствии с этим устанавливаем setText для кнопки
        /**
         * ...
         */

        if (!GlobalVar.isBuy) {

            //Временно
            button_in_info_partner.setText("Оформить подписку");

            //Листенер для кнопки  "Оформить подписку"/"Использовать"
            button_in_info_partner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

                    //Сделать проверку (отправляем на сервер запрос к БД и смотрим офрмлена ли подписка у чела)
                    //В соответствии с этим устанавливаем setText для кнопки

                    //Временно
                    /*FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(InformationAboutPartnerFragment.this.getId(), new RegisterOrderFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();*/

                    GlobalVar.isBuy = true;
                }
            });

        } else {

            button_in_info_partner.setText("Воспользоваться");

            button_in_info_partner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Всплывающее окно
                    Dialog dialog = new Dialog(getActivity()); //Создаем диалоговое окно
                    dialog.setContentView(R.layout.dialog_qr_code); //Устанавливаем разметку в окне
                    dialog.show(); //Отображаем окно

                    //Устанавливаем Листенер для кнопки "ОК"
                    Button button_cancel = dialog.findViewById(R.id.button_qr_code);
                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            });

        }

    }

}
