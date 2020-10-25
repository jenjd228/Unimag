package com.example.unimag.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unimag.R;


public class CategoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout viewClothes = getView().findViewById(R.id.clothes);
        LinearLayout viewSouvenirs = getView().findViewById(R.id.souvenirs);
        LinearLayout viewPartnerProgram = getView().findViewById(R.id.partner_program);


        //Создание листенера для кнопки "Одежда"
        viewClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Передача информации (мб в MySQL)

                //Переход обратно во вкладку каталога
                Navigation.findNavController(v).navigate(R.id.action_categoryFragment_to_navigation_catalog);
            }
        });

        //Создание листенера для кнопки "Сувениры"
        viewSouvenirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Передача информации (мб в MySQL)
                //
                //Переход обратно во вкладку каталога
                Navigation.findNavController(v).navigate(R.id.action_categoryFragment_to_navigation_catalog);
            }
        });

    }
}
