package com.example.unimag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;


public class FeedbackFragment extends Fragment {

    private EditText textSubject;
    private EditText textMessage;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button buttonSend = requireView().findViewById(R.id.buttonSend); //Кнопка "Отправить"
        textSubject =  requireView().findViewById(R.id.editTextSubject); //Тема письма
        textMessage =  requireView().findViewById(R.id.editTextMessage); //Сообщение письма

        buttonSend.setOnClickListener(v -> {
            new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();

            String subject = textSubject.getText().toString();
            String message = textMessage.getText().toString();

            //Intent представляет собой объект обмена сообщениями,
            //с помощью которого можно запросить выполнение действия у компонента другого приложения
            Intent email = new Intent(Intent.ACTION_SEND);
            //Указываем получателя (почта нашего ИП)
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"pkuzmenko@sfedu.ru"});
            //Устанавливаем Тему сообщения
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            //Устанавливаем само сообщение
            email.putExtra(Intent.EXTRA_TEXT, message);
            //тип отправляемого сообщения
            email.setType("message/rfc822");
            //Вызываем intent выбора клиента для отправки сообщения
            startActivity(Intent.createChooser(email, "Выберите приложение (почту) для отправки письма:"));

            //Обнуление полей после отправки письма
            textSubject.setText("");
            textMessage.setText("");

        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //System.out.println(1);
            outState.putString("textMessage",textMessage.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            //System.out.println(2+" "+savedInstanceState.getString("textMessage"));
            textMessage.setText(savedInstanceState.getString("textMessage"));
        }
    }

}
