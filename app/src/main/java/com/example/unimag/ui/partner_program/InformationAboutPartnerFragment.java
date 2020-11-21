package com.example.unimag.ui.partner_program;

import android.annotation.SuppressLint;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.Request.CheckRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;

import lombok.SneakyThrows;


public class InformationAboutPartnerFragment extends Fragment {

    private DataDBHelper dataDbHelper;

    private String secureKod = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information_about_partner, container, false);


        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Партнер");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект
        if (dataDbHelper!=null){
            secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        }
        return root;
    }


    @SneakyThrows
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button_in_info_partner = requireView().findViewById(R.id.button_in_info_partner); //Кнопка "Оформить подписку"/"Использовать"

        ImageView image =  requireView().findViewById(R.id.image_info_partner);
        TextView titlePartnerView =  requireView().findViewById(R.id.text_title_info_partner);
        TextView pricePartnerView =  requireView().findViewById(R.id.price_partner);
        TextView descriptionPartnerView =  requireView().findViewById(R.id.text_description_partner);

        Glide.with(requireView()).load("http://"+ GlobalVar.ip+":8080/upload/image_partner/"+InformationAboutPartnerFragmentArgs.fromBundle(requireArguments()).getImageName()).into(image);
        titlePartnerView.setText(InformationAboutPartnerFragmentArgs.fromBundle(requireArguments()).getTitle());
        pricePartnerView.setText(InformationAboutPartnerFragmentArgs.fromBundle(requireArguments()).getPrice() + " рублей");
        descriptionPartnerView.setText(InformationAboutPartnerFragmentArgs.fromBundle(requireArguments()).getDescription());


        CheckRequest checkRequest = new CheckRequest(secureKod,"userIsSub");
        checkRequest.execute();

        if (checkRequest.get().equals("ACCESS_CLOSED") || secureKod.equals("")) {

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
