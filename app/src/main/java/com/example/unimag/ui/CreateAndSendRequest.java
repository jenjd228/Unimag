package com.example.unimag.ui;

import android.os.AsyncTask;

import com.example.unimag.ui.basket.GridAdapterBasket;
import com.example.unimag.ui.catalog.CustomGridAdapter;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateAndSendRequest extends AsyncTask<Void, Void, String> {
    private String secureKodOrEmail;
    private String email;
    private String fio;
    private String birthDay;
    private String methodName;

    public CreateAndSendRequest(String secureKodOrEmail,String methodName){
        this.secureKodOrEmail = secureKodOrEmail;
        this.methodName = methodName;
    }

    public CreateAndSendRequest(String email,String fio,String birthDay,String methodName){
        this.email = email;
        this.fio = fio;
        this.birthDay = birthDay;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response = null;
        switch (methodName){
            case "sendMessage":{
                RequestBody formBody = new FormBody.Builder()
                        .add("email",email)
                        .build();
                request = new Request.Builder()
                        .url("http://192.168.31.143:8080/sendMessage")
                        .post(formBody)
                        .build();
                break;
            }
            case "userUpdate":{
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("fio",  fio)
                        .add("birthData",  birthDay)
                        .build();
               request = new Request.Builder()
                        .url("http://192.168.31.143:8080/userUpdate")
                        .put(formBody)
                        .build();
                break;
            }
        }

        try {
            response = client.newCall(request).execute(); //ответ сервера
            String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }
}
