package com.example.unimag.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.Request.CheckRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;

import java.util.concurrent.ExecutionException;

public class LoginFragment extends Fragment {
    private EditText login;
    private EditText password;
    private DataDBHelper dataDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Вход");
        actionBar.setDisplayHomeAsUpEnabled(false);

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dataDBHelper = new DataDBHelper(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton showPassword = getView().findViewById(R.id.show_password_in_login);
        password = getView().findViewById(R.id.inputPassword);
        login = getView().findViewById(R.id.inputLogin);
        Button register = getView().findViewById(R.id.button_register);
        Button singIn = getView().findViewById(R.id.button_vxod);

        //Создание листенера для кнопки "Показать пароль"
        showPassword.setOnClickListener(e -> {
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
        });

        register.setOnClickListener(e -> {
            new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);
        });
        singIn.setOnClickListener(e -> {
            new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
            String password = String.valueOf(this.password.getText());
            String email = String.valueOf(this.login.getText());
            if (!password.equals("") && !email.equals("")) {
                CheckRequest checkRequest = new CheckRequest(email, password, "checkUserForLoginIn");
                checkRequest.execute();
                try {
                    String response = checkRequest.get();
                    if (response.equals("LOCKED")) {
                        Toast toast = Toast.makeText(getContext(),
                                "Неверный пароль!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (response.equals("NOT_FOUND")) {
                        Toast toast = Toast.makeText(getContext(),
                                "Пользователь с такой почтой не найден!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        SQLiteDatabase sqLiteDatabase = dataDBHelper.getWritableDatabase();
                        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(DataDBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                        if (cursor.getCount() == 0) {
                            ContentValues contentValues = new ContentValues();//запись
                            contentValues.put(DataDBHelper.KEY_SECUREKOD, response);
                            sqLiteDatabase.insert(DataDBHelper.TABLE_CONTACTS, null, contentValues);
                        } else {
                            sqLiteDatabase.delete(DataDBHelper.TABLE_CONTACTS, null, null);//удаление
                            ContentValues contentValues = new ContentValues();//запись
                            contentValues.put(DataDBHelper.KEY_SECUREKOD, response);
                            sqLiteDatabase.insert(DataDBHelper.TABLE_CONTACTS, null, contentValues);
                        }
                        dataDBHelper.close();
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_myCabinetFragment2);
                    }
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }else {
                Toast toast = Toast.makeText(getContext(),
                        "Введите данные!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
