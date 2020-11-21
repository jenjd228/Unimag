package com.example.unimag.ui.register;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.Request.SendOrUpdateRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    private DataDBHelper dataDbHelper;
    private View root;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    @Override
    public void onStart() {
        super.onStart();
        try {
            email =  getActivity().findViewById(R.id.email);
            password =  getActivity().findViewById(R.id.password);
            repeatPassword =  getActivity().findViewById(R.id.repeatPassword);
        }  catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Регистрация");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        root = inflater.inflate(R.layout.fragment_register, container, false);
        dataDbHelper = new DataDBHelper(Objects.requireNonNull(container).getContext());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b = getView().findViewById(R.id.register_button);
        ImageButton showPassword = getView().findViewById(R.id.show_password_in_registr);
        EditText editPassword1 = getView().findViewById(R.id.password);
        EditText editPassword2 = getView().findViewById(R.id.repeatPassword);

        showPassword.setOnClickListener(new View.OnClickListener() { //Создание листенера для кнопки "Показать пароль"
            @Override
            public void onClick(View e) {
                //Если пароль невиден
                if (editPassword1.getInputType() == 131201) {
                    editPassword1.setInputType(1);
                    editPassword2.setInputType(1);
                    editPassword1.setSelection(editPassword1.getText().length()); //Установка курсора в конец ввода
                    editPassword2.setSelection(editPassword2.getText().length()); //Установка курсора в конец ввода
                    showPassword.setColorFilter(getResources().getColor(R.color.light_blue)); //Покраска знака глаза
                }
                //Если пароль виден
                else if (editPassword1.getInputType() == 1) {
                    editPassword1.setInputType(131201);
                    editPassword2.setInputType(131201);
                    editPassword1.setSelection(editPassword1.getText().length()); //Установка курсора в конец ввода
                    editPassword2.setSelection(editPassword2.getText().length()); //Установка курсора в конец ввода
                    showPassword.setColorFilter(getResources().getColor(R.color.black));
                }
            }
        });

        b.setOnClickListener(e -> {
            new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
            String email2 = String.valueOf(email.getText());
            String password2 = String.valueOf(password.getText());
            String repeatPassword2 = String.valueOf(repeatPassword.getText());
            boolean pas = checkPassword(password2);
            boolean repPas = checkRepeatPassword(password2,repeatPassword2);
            boolean em = checkEmail(email2);
            TextView viewError = getView().findViewById(R.id.text_hint_registration); //TextView с подсказкой об ошибке


            if (repPas && pas && em){ //если пароль email правильный
                //sendEmail(email2);
                try {
                    firstUpdate(email2,password2);
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (!pas){
                Toast toast = Toast.makeText(this.getContext(),
                        "Некорректный пароль !", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setTextColor(getResources().getColor(R.color.colorPrimary)); //Установка красного цвета
                viewError.setText("Ошибка: некорректный пароль !\nПароль должен состоять из латинских букв и содержать как минимум 1 цифру, 1 знак, 1 заглавную букву и 1 маленькую букву.\nДлина пароля не менее 6 символов.");
            }
            if (!repPas){
                Toast toast = Toast.makeText(this.getContext(),
                        "Пароли не совпадают !", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewError.setText("Ошибка: пароли не совпадают");
            }
            if(!em){
                Toast toast = Toast.makeText(this.getContext(),
                        "Некорректный email !", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewError.setText("Ошибка: некорректный Email");
            }
        });
    }

    private boolean checkEmail(String email){
        String emailPravilo = "^.+@+.+\\..+$";
        Pattern pattern = Pattern.compile(emailPravilo);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private boolean checkPassword(String password){
        String passwordPravilo = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^\\w\\s]).{6,}$";
        Pattern pattern = Pattern.compile(passwordPravilo);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    private boolean checkRepeatPassword(String password, String repeatPassword){
        if (password.equals(repeatPassword)){
            return true;
        }
        return false;
    }

    private void firstUpdate(String email,String password) throws ExecutionException, InterruptedException {
        SendOrUpdateRequest sendOrUpdateRequest = new SendOrUpdateRequest(email,password,"firstUpdate");
        sendOrUpdateRequest.execute();
        String otvet = sendOrUpdateRequest.get();
        if (otvet.equals("yes")){
            Toast toast = Toast.makeText(getContext(),
                    "Такой email уже зарегистрирован !", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            addToSqLite(otvet);
        }
    }

    private void addToSqLite(String hex){
        SQLiteDatabase sqLiteDatabase = dataDbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataDBHelper.TABLE_CONTACTS,null,null,null,null,null,null);
        if (cursor.getCount()==0){
            ContentValues contentValues = new ContentValues();//запись
            contentValues.put(DataDBHelper.KEY_SECUREKOD,hex);
            sqLiteDatabase.insert(DataDBHelper.TABLE_CONTACTS, null, contentValues);
        }else {
            sqLiteDatabase.delete(DataDBHelper.TABLE_CONTACTS,null,null);//удаление
            ContentValues contentValues = new ContentValues();//запись
            contentValues.put(DataDBHelper.KEY_SECUREKOD,hex);
            sqLiteDatabase.insert(DataDBHelper.TABLE_CONTACTS, null, contentValues);
        }
        dataDbHelper.close();
        goToNextFragmentRegistration();
    }

    /*@SneakyThrows
    private void sendEmail(String email){
        SendOrUpdateRequest sendOrUpdateRequest = new SendOrUpdateRequest(email,"sendMessage");
        sendOrUpdateRequest.execute();
        String otvet = sendOrUpdateRequest.get();
        System.out.println(otvet);
        if(otvet.equals("no")){
            qwe();
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "Такой email уже зарегистрирован !", Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/

    private void goToNextFragmentRegistration(){
        RegisterFragmentDirections.ActionRegisterFragment3ToRegisterFragment2 action = RegisterFragmentDirections.actionRegisterFragment3ToRegisterFragment2(String.valueOf(email.getText()));
        Navigation.findNavController(requireView()).navigate(action);
    }
}
