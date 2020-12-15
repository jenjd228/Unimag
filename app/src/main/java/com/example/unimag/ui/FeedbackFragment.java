package com.example.unimag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;


public class FeedbackFragment extends Fragment {

    private EditText textSubject;
    private EditText textMessage;
    private EditText textPhone;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        new ThreadCheckingConnection(getFragmentManager(), requireContext()); //Проверка на подключение к интернету
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button buttonSend = requireView().findViewById(R.id.buttonSend); //Кнопка "Отправить"
        textSubject =  requireView().findViewById(R.id.editTextSubject); //Тема письма
        textMessage =  requireView().findViewById(R.id.editTextMessage); //Сообщение письма
        textPhone = requireView().findViewById(R.id.editTextPhone);      //Номер телефона

        /**Листенер для ввода телефона*/
        textPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+7")){
                    textPhone.setText("+7");
                    Selection.setSelection(textPhone.getText(), textPhone.getText().length());
                }
            }
        });

        /**Листенер для кнопки отправить*/
        buttonSend.setOnClickListener(v -> {

            String subject = textSubject.getText().toString();
            String message = textMessage.getText().toString();
            String phone = textPhone.getText().toString();

            //Проверка на ошибки введенных данных
            if (subject.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Не задана тема сообщения", Toast.LENGTH_SHORT);
                toast.show();
            } else if (phone.length() != 12) {
                Toast toast = Toast.makeText(getContext(), "Неверно введен номер телефона", Toast.LENGTH_SHORT);
                toast.show();
            } else {

                message += "\n\nНомер телефона: " + phone;
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
                textPhone.setText("");
            }

        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putString("textMessage",textMessage.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            textMessage.setText(savedInstanceState.getString("textMessage"));
        }
    }

}
