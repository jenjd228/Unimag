package com.example.unimag.ui.register;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
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

import com.example.unimag.MainActivity;
import com.example.unimag.R;
import com.example.unimag.ui.MyCustomPatterns;
import com.example.unimag.ui.OrderDetails.OrderDetailsFragment;
import com.example.unimag.ui.Request.SendOrUpdateRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.personal_area.MyCabinetFragment;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.SneakyThrows;

public class RegisterFragment extends Fragment {
    private DataDBHelper dataDbHelper;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private ImageButton showPassword;
    private EditText birthDay;
    private EditText birthMonth;
    private EditText birthYear;
    private EditText name;
    private EditText surname;
    private EditText patronymic;

    @Override
    public void onStart() {
        super.onStart();
        try {
            email = requireActivity().findViewById(R.id.email);
            password = requireActivity().findViewById(R.id.password);
            repeatPassword = requireActivity().findViewById(R.id.repeatPassword);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Регистрация");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        View root = inflater.inflate(R.layout.fragment_register, container, false);
        dataDbHelper = new DataDBHelper(Objects.requireNonNull(container).getContext());
        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button buttonRegister = requireView().findViewById(R.id.register_button);
        showPassword = requireView().findViewById(R.id.show_password_in_registr);
        password = requireView().findViewById(R.id.password);
        repeatPassword = requireView().findViewById(R.id.repeatPassword);
        birthDay = requireView().findViewById(R.id.birthDay);
        birthMonth = requireView().findViewById(R.id.birthMonth);
        birthYear = requireView().findViewById(R.id.birthYear);
        name = requireView().findViewById(R.id.name);
        surname = requireView().findViewById(R.id.surname);
        patronymic = requireView().findViewById(R.id.patronymic);


        //Создание листенера для кнопки "Показать пароль"
        showPassword.setOnClickListener(e -> {
            //Если пароль невиден
            if (password.getInputType() == 131201) {
                password.setInputType(1);
                repeatPassword.setInputType(1);
                password.setSelection(password.getText().length()); //Установка курсора в конец ввода
                repeatPassword.setSelection(repeatPassword.getText().length()); //Установка курсора в конец ввода
                showPassword.setColorFilter(getResources().getColor(R.color.light_blue)); //Покраска знака глаза
            }
            //Если пароль виден
            else if (password.getInputType() == 1) {
                password.setInputType(131201);
                repeatPassword.setInputType(131201);
                password.setSelection(password.getText().length()); //Установка курсора в конец ввода
                repeatPassword.setSelection(repeatPassword.getText().length()); //Установка курсора в конец ввода
                showPassword.setColorFilter(getResources().getColor(R.color.black));
            }
        });

        //Листенер для кнопки "Зарегистрироваться"
        buttonRegister.setOnClickListener(e -> {
            String email2 = String.valueOf(email.getText());
            String password2 = String.valueOf(password.getText());
            String repeatPassword2 = String.valueOf(repeatPassword.getText());

            String birthDay2 = checkDay(String.valueOf(birthDay.getText()));
            String birthMonth2 = checkBirthMonth(String.valueOf(birthMonth.getText()));
            String birthYear2 = checkBirthYear(String.valueOf(birthYear.getText()));

            String name2 = String.valueOf(name.getText());
            String surname2 = String.valueOf(surname.getText());
            String patronymic2 = String.valueOf(patronymic.getText());

            boolean pas = MyCustomPatterns.getInstance().isValidPassword(password2);
            boolean repPas = checkRepeatPassword(password2, repeatPassword2);
            boolean em = MyCustomPatterns.getInstance().isValidEmail(email2);

            TextView viewError = requireView().findViewById(R.id.text_hint_registration); //TextView с подсказкой об ошибке

            email.setError(null);
            password.setError(null);
            repeatPassword.setError(null);
            name.setError(null);
            surname.setError(null);
            patronymic.setError(null);
             //Проверка логина и пароля
            if (!em) {
                email.setError("Incorrect email!");
            } else if (!pas) {
                password.setError("Incorrect password!");
                viewError.setText("Ошибка: некорректный пароль !\nПароль должен содержать в себе латинский алфавит, цифры и нижнее подчеркивание\nДлина пароля не менее 6 символов.");
            } else if (!repPas) {
                repeatPassword.setError("Passwords do not match!");
            }


             // Проверка данных о пользователе
            else if (!MyCustomPatterns.getInstance().isValidString(surname2)) {
                surname.setError("Incorrect surname!");
            } else if (!MyCustomPatterns.getInstance().isValidString(name2)) {
                name.setError("Incorrect name!");
            } else if (!MyCustomPatterns.getInstance().isValidString(patronymic2)) {
                patronymic.setError("Incorrect patronymic!");
            } else if (birthDay2.equals("false")) {
                Toast toast = Toast.makeText(getContext(),
                        "Неверный день рождения!", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setText("Неверный день рождения");
            } else if (birthDay2.equals("День рождения не может быть 0")) {
                Toast toast = Toast.makeText(getContext(),
                        "День рождения не может быть 0!", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setText("День рождения не может быть 0");
            } else if (birthMonth2.equals("false")) {
                Toast toast = Toast.makeText(getContext(),
                        "Неверный месяц рождения!", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setText("Неверный месяц рождения");
            } else if (birthMonth2.equals("Месяц рождения не может быть 0")) {
                Toast toast = Toast.makeText(getContext(),
                        "Месяц рождения не может быть 0!", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setText("Месяц рождения не может быть 0");
            } else if (birthYear2.equals("false")) {
                Toast toast = Toast.makeText(getContext(),
                        "Неверный год рождения!", Toast.LENGTH_SHORT);
                toast.show();
                viewError.setText("Неверный год рождения");
            } else {
                String birthData = birthDay2 + "." + birthMonth2 + "." + birthYear2;
                String fio = MyCustomPatterns.getInstance().firstCharToUpperCase(surname2) + " "
                        + MyCustomPatterns.getInstance().firstCharToUpperCase(name2) + " "
                        + MyCustomPatterns.getInstance().firstCharToUpperCase(patronymic2);

                //Если все введено корректно - то только тогда шлем запрос на сервер
                try {
                    SendOrUpdateRequest sendOrUpdateRequest = new SendOrUpdateRequest(requireContext(), getParentFragmentManager(),
                            String.valueOf(email.getText()), String.valueOf(password.getText()), fio, birthData, "firstUpdate");
                    sendOrUpdateRequest.execute();
                    String otvet = sendOrUpdateRequest.get();
                    //Если такой email зарегистрирован - то говорим
                    if (otvet.equals("yes")) {
                        Toast toast = Toast.makeText(getContext(),
                                "Такой email уже зарегистрирован!", Toast.LENGTH_SHORT);
                        toast.show();
                        viewError.setText("Такой email уже зарегистрирован");
                    } else {
                        //Иначе - все хорошо и мы заносим пользователя в БД и переходим дальше
                        addToSqLite(otvet);
                        goToLK();
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    private boolean checkRepeatPassword(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    private void addToSqLite(String hex) {
        SQLiteDatabase sqLiteDatabase = dataDbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(DataDBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();//запись
            contentValues.put(DataDBHelper.KEY_SECUREKOD, hex);
            sqLiteDatabase.insert(DataDBHelper.TABLE_CONTACTS, null, contentValues);
        } else {
            sqLiteDatabase.delete(DataDBHelper.TABLE_CONTACTS, null, null);//удаление
            ContentValues contentValues = new ContentValues();//запись
            contentValues.put(DataDBHelper.KEY_SECUREKOD, hex);
            sqLiteDatabase.insert(DataDBHelper.TABLE_CONTACTS, null, contentValues);
        }
        dataDbHelper.close();
    }


    private String checkDay(String birthDay) {
        String birthDayPravilo = "^[0-9]+$";
        Pattern pattern = Pattern.compile(birthDayPravilo);
        Matcher matcher = pattern.matcher(birthDay);
        boolean flag = matcher.find();
        if (!flag) { //передалать switch case
            return "false";
        } else {
            int intBirthDay = Integer.parseInt(birthDay);
            if (intBirthDay > 0 && intBirthDay <= 31) {
                if (birthDay.length() == 1) {
                    return "0" + intBirthDay;
                }
                if ((birthDay.startsWith("0") && Integer.parseInt(birthDay.substring(1, 2)) >= 1) || (Integer.parseInt(birthDay.substring(0, 1)) >= 1 && Integer.parseInt(birthDay.substring(1, 2)) <= 9)) {
                    return Integer.toString(intBirthDay);
                }
            }
        }
        return "false";
    }


    private String checkBirthMonth(String birthMonth) {
        String birthMonthPravilo = "^[0-9]+$";
        Pattern pattern = Pattern.compile(birthMonthPravilo);
        Matcher matcher = pattern.matcher(birthMonth);
        if (!matcher.find()) { //передалать switch case
            return "false";
        } else {
            int intBirthMonth = Integer.parseInt(birthMonth);
            if (intBirthMonth > 0 && intBirthMonth <= 12) {
                if (birthMonth.length() == 1) {
                    return "0" + birthMonth;
                }
                if (birthMonth.length() > 1) {
                    if ((birthMonth.startsWith("0") && Integer.parseInt(birthMonth.substring(1, 2)) >= 1) || (Integer.parseInt(birthMonth.substring(0, 1)) == 1 && Integer.parseInt(birthMonth.substring(1, 2)) <= 2)) {
                        return birthMonth;
                    }
                }
            }
        }
        return "false";
    }

    private String checkBirthYear(String birthYear) {
        String birthYearPravilo = "^[0-9]+$";
        Pattern pattern = Pattern.compile(birthYearPravilo);
        Matcher matcher = pattern.matcher(birthYear);
        if (!matcher.find()) {
            return "false";
        } else {
            int intBirthYear = Integer.parseInt(birthYear);
            if (birthYear.length() == 4 && intBirthYear > 1950 && intBirthYear < 2010) {
                return Integer.toString(intBirthYear);
            }
        }
        return "false";
    }


    private void goToLK() {
        ((MainActivity)getActivity()).navigateIn(MainActivity.TAB_PERSONAL_AREA, new MyCabinetFragment(), new Bundle());
    }

    @Override
    public void onDestroyView() {
        ((MainActivity)getActivity()).addInStack(MainActivity.TAB_PERSONAL_AREA, this);
        super.onDestroyView();
    }

}
