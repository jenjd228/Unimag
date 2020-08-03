package com.example.unimag.ui;

import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterFragment extends Fragment {
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
        root = inflater.inflate(R.layout.fragment_register, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b = getView().findViewById(R.id.register_button);
        b.setOnClickListener(e -> {
            String email2 = String.valueOf(email.getText());
            boolean pas = checkPassword(password.getText().toString());
            boolean repPas = checkRepeatPassword(password.getText().toString(),repeatPassword.getText().toString());
            boolean em = checkEmail(email2);


            if (pas && em){ //если пароль email правильный
                sendEmail(email2);
            }
            if (!pas){
                Toast toast = Toast.makeText(this.getContext(),
                        "Некорректный пароль !", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (!repPas){
                Toast toast = Toast.makeText(this.getContext(),
                        "Пароли не совпадают !", Toast.LENGTH_SHORT);
                toast.show();
            }
            if(!em){
                Toast toast = Toast.makeText(this.getContext(),
                        "Некорректный email !", Toast.LENGTH_SHORT);
                toast.show();
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


    private void sendEmail(String email){

         CreateAndSendRequast createAndSendRequast = new CreateAndSendRequast(email);
         createAndSendRequast.execute();
    }

    private void qwe(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(this.getId(), new RegisterKodFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class CreateAndSendRequast extends AsyncTask<Void, Void, Response> {
        private String email;
        CreateAndSendRequast(String email){
            this.email = email;
        }

        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("email", email) // A simple POST field
                    .add("kod",  getKod()) // Another sample POST field
                    .build();
            Request request = new Request.Builder()
                    .url("http://192.168.31.143:8080/sendMessage") // The URL to send the data to
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute(); //ответ сервера
                //System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            try {
                if(result.body().string().equals("no")){
                    qwe();
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Такой email уже зарегистрирован !", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private String getKod(){
            Random random = new Random();
            int rage=9999;
            return String.valueOf(random.nextInt(rage));
        }

    }

}
