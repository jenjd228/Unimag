package com.example.unimag.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return root;
    }

    public interface OnSelectedButtonListener { //Интерфейс для связи с Activity
        void onButtonSelected(String category);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Создание функции передающей данные между фрагментами
        TextView buttonSouvenirs = getView().findViewById(R.id.souvenirs);
        buttonSouvenirs.setOnClickListener(new View.OnClickListener() { //Создание листенера для кнопки "Показать еще"
            @Override
            public void onClick(View e) {
                //Связывание с Activity
                OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
                //Выполнение функции onButtonSelected, объявленной в Activity, для дальнейшей передачи информации фрагменту
                listener.onButtonSelected("Сувениры");
                //Переход обратно во вкладку каталога
                FragmentManager manager = CategoryFragment.this.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(CategoryFragment.this.getId(), new CatalogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
