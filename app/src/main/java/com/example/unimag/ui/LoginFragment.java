package com.example.unimag.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.personal_area.MyCabinetFragment;
import com.example.unimag.ui.register.RegisterFragment;

public class LoginFragment extends Fragment {
    private Button register;
    private EditText login;
    private EditText password;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton showPassword = getView().findViewById(R.id.show_password_in_login);

        showPassword.setOnClickListener(new View.OnClickListener() { //Создание листенера для кнопки "Показать пароль"
            @Override
            public void onClick(View e) {
                //System.out.println(password.getInputType());
                //Если пароль невиден
                if (password.getInputType() == 131201) {
                    password.setInputType(1);
                    password.setSelection(password.getText().length()); //Установка курсора в конец ввода
                    showPassword.setColorFilter(getResources().getColor(R.color.light_blue)); //Покраска знака глаза
                }
                //Если пароль виден
                else if (password.getInputType() == 1) {
                    password.setInputType(131201);
                    password.setSelection(password.getText().length());
                    showPassword.setColorFilter(getResources().getColor(R.color.black));
                }
            }
        });

        Button register = getView().findViewById(R.id.button_register);
        Button vhod = getView().findViewById(R.id.button_vxod);
        register.setOnClickListener(e -> {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(this.getId(), new RegisterFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        vhod.setOnClickListener(e -> {
            //System.out.println(1);
            /*FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(this.getId(), new MyCabinetFragment());
            transaction.addToBackStack(null);
            transaction.commit();*/
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
