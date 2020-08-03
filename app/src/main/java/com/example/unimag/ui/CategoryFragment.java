package com.example.unimag.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.catalog.CatalogFragment;


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


        viewClothes.setOnClickListener(new View.OnClickListener() { //Создание листенера для кнопки "Одежда"
            @Override
            public void onClick(View v) {
                //Передача информации (мб в MySQL)

                //Переход обратно во вкладку каталога
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(CategoryFragment.this.getId(), new CatalogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        viewSouvenirs.setOnClickListener(new View.OnClickListener() { //Создание листенера для кнопки "Сувениры"
            @Override
            public void onClick(View e) {
                //Передача информации (мб в MySQL)

                //Переход обратно во вкладку каталога
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(CategoryFragment.this.getId(), new CatalogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
