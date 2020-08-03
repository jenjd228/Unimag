package com.example.unimag.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.Request.SendOrUpdateRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.personal_area.MyCabinetFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.SneakyThrows;

public class RegisterFragment2 extends Fragment {
    private DataDBHelper dataDbHelper;
    private View root;
    private EditText birthDay;
    private EditText birthMonth;
    private EditText birthYear;
    private EditText name;
    private EditText surname;
    private EditText secondSurname;
    private String email;

    RegisterFragment2(String email){
        this.email = email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        root = inflater.inflate(R.layout.fragment_register2, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            birthDay =  getView().findViewById(R.id.birthDay);
            birthMonth =  getView().findViewById(R.id.birthMonth);
            birthYear =  getView().findViewById(R.id.birthYear);
            name =  getView().findViewById(R.id.name);
            surname =  getView().findViewById(R.id.surname);
            secondSurname =  getView().findViewById(R.id.secondSurname);
        }  catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b = getView().findViewById(R.id.register_button2);
        b.setOnClickListener(e -> {
            new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
            String birthDay2 = checkDay(String.valueOf(birthDay.getText()));
            String birthMonth2 = checkBirthMonth(String.valueOf(birthMonth.getText()));
            String birthYear2 = checkBirthYear(String.valueOf(birthYear.getText()));

            String name2 = checkString(String.valueOf(name.getText()));
            String surname2 = checkString(String.valueOf(surname.getText()));
            String secondSurname2 = checkString(String.valueOf(secondSurname.getText()));


            if(name2.equals("false")){
                Toast toast = Toast.makeText(getContext(),
                        "Имя должно начинаться с большой буквы!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(surname2.equals("false")){
                Toast toast = Toast.makeText(getContext(),
                        "Фамилия должна начинаться с большой буквы!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(secondSurname2.equals("false")){
                Toast toast = Toast.makeText(getContext(),
                        "Отчество должно начинаться с большой буквы!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(birthDay2.equals("false")){
                Toast toast = Toast.makeText(getContext(),
                        "Неверный день рождения!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(birthDay2.equals("День рождения не может быть 0")){
                Toast toast = Toast.makeText(getContext(),
                        "День рождения не может быть 0!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(birthMonth2.equals("false")){
                Toast toast = Toast.makeText(getContext(),
                        "Неверный месяц рождения!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(birthMonth2.equals("Месяц рождения не может быть 0")){
                Toast toast = Toast.makeText(getContext(),
                        "Месяц рождения не может быть 0!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(birthYear2.equals("false")){
                Toast toast = Toast.makeText(getContext(),
                        "Неверный год рождения!", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                String birthData = birthDay2+"."+birthMonth2+"."+birthYear2;
                String fio = name2+" "+surname2+" "+secondSurname2;
                update(email,fio,birthData);
            }
            });
    }

    private String checkString (String string){
        String stringPravilo = "^[A-ZА-Я][a-zа-я]{2,}$";
        Pattern pattern = Pattern.compile(stringPravilo);
        Matcher matcher = pattern.matcher(string);
        if(matcher.find()){
            return string;
        }
        return "false";
    }

    private String checkDay(String birthDay){
        String birthDayPravilo = "^[0-9]+$";
        Pattern pattern = Pattern.compile(birthDayPravilo);
        Matcher matcher = pattern.matcher(birthDay);
        boolean flag = matcher.find();
        if (!flag){ //передалать switch case
            return "false";
        }else {
            int intBirthDay = Integer.parseInt(birthDay);
                if (intBirthDay==0){
                    return "День рождения не может быть 0";
                }
            if (birthDay.length() == 1){
                return "0"+ intBirthDay;
            }
            if (birthDay.substring(0,1).equals("0") && Integer.parseInt(birthDay.substring(1,2))>=1 || Integer.parseInt(birthDay.substring(1,2))>=1 && Integer.parseInt(birthDay.substring(1,2))<=9){
                return Integer.toString(intBirthDay);
            }
        }
        return "false";
    }

    private String checkBirthMonth(String birthMonth){
        String birthMonthPravilo = "^[0-9]+$";
        Pattern pattern = Pattern.compile(birthMonthPravilo);
        Matcher matcher = pattern.matcher(birthMonth);
        if (!matcher.find()){ //передалать switch case
            return "false";
        }else {
            int intBirthMonth = Integer.parseInt(birthMonth);
            if (intBirthMonth==0){
                return "Месяц не может быть 0";
            }
            if (birthMonth.length()==1){
                return "0"+birthMonth;
            }
            if(birthMonth.length()>1){
                if (birthMonth.substring(0,1).equals("0") && Integer.parseInt(birthMonth.substring(1,2))>=1 || Integer.parseInt(birthMonth.substring(1,2))>=1 && Integer.parseInt(birthMonth.substring(1,2))<=9){
                    return birthMonth;
                }
            }
        }
        return "false";
    }

    private String checkBirthYear(String birthYear){
        String birthYearPravilo = "^[0-9]+$";
        Pattern pattern = Pattern.compile(birthYearPravilo);
        Matcher matcher = pattern.matcher(birthYear);
        if (!matcher.find()){ //передалать switch case
            return "false";
        }else {
            int intBirthYear = Integer.parseInt(birthYear);
            if (birthYear.length() == 4 && intBirthYear>1950 && intBirthYear<2010){
                return Integer.toString(intBirthYear);
            }
        }
        return "false";
    }

    @SneakyThrows
    private void update(String email, String fio, String birthData){
        SendOrUpdateRequest sendOrUpdateRequest = new SendOrUpdateRequest(email,fio,birthData,"userUpdate");
        sendOrUpdateRequest.execute();
        if(sendOrUpdateRequest.get().equals("ok")){
            goToLK();
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "Ошибка!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void goToLK(){
        FragmentManager manager = RegisterFragment2.this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(RegisterFragment2.this.getId(), new MyCabinetFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}