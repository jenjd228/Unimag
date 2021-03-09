package com.example.unimag.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;

public class NoConnectionFragment extends Fragment {

    private Button button_update_connection; //Кнопка "Переподключиться"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Нет соединения");
        actionBar.setDisplayHomeAsUpEnabled(false);

        View root = inflater.inflate(R.layout.fragment_no_connection, container, false);
        return root;
    }


    //Проверка подключения к интернету
    public static boolean isConnect(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE); //Убираем меню

        button_update_connection = getView().findViewById(R.id.button_update_connection);
        button_update_connection.setOnClickListener(new View.OnClickListener() { //Установка onClickListener'а для кнопки "Переподключиться"
            @Override
            public void onClick(View v) {

                //Если интернет вернулся
                if (isConnect(requireContext())) {
                    getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE); //Возвращаем меню
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Toast.makeText(requireContext(), "Подключение восстановлено!", Toast.LENGTH_LONG).show();
                    manager.popBackStack("last_no_connection_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE); //Достаем последний фрагмент из стека
                    transaction.commit();
                } else {
                    Toast.makeText(requireContext(), "Нет соединения с интернетом!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
