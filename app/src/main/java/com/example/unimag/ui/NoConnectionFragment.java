package com.example.unimag.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.catalog.CatalogFragment;
import com.example.unimag.ui.register.RegisterFragment;

public class NoConnectionFragment extends Fragment {

    private Button button_update_connection; //Кнопка "Переподключиться"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Нет соединения");
        actionBar.setDisplayHomeAsUpEnabled(false);

        View root = inflater.inflate(R.layout.fragment_no_connection, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button_update_connection = getView().findViewById(R.id.button_update_connection);

        button_update_connection.setOnClickListener(new View.OnClickListener() { //Установка onClickListener'а для кнопки "Переподключиться"
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(NoConnectionFragment.this.getId(), new CatalogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

}
