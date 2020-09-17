package com.example.unimag.ui.Request;

import android.os.AsyncTask;

import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.basket.GridAdapterBasket;
import com.example.unimag.ui.catalog.CustomGridAdapter;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private String email;
    private String kodOrSecureKod;
    private String password;
    private String methodName;

    public CheckRequest(String email,String kodOrSecureKod,String password,String methodName){
        this.email = email;
        this.kodOrSecureKod = kodOrSecureKod;
        this.password = password;
        this.methodName = methodName;
    }
    public CheckRequest(String email,String password,String methodName){
        this.email = email;
        this.password = password;
        this.methodName = methodName;
    }

    public CheckRequest(String secureKod,String methodName){
        this.secureKod = secureKod;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName){
            case "checkBySecureKod":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/checkBySecureKod/"+secureKod)
                        .get()
                        .build();
                break;
            }
            case "checkUserForLoginIn":{
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/checkUserForLoginIn")
                        .post(formBody)
                        .build();
                break;
            }
            case "checkByKod":{
                RequestBody formBody = new FormBody.Builder()
                        .add("kod",  kodOrSecureKod)
                        .add("email", email)
                        .add("password", password)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/checkByKod")
                        .post(formBody)
                        .build();
                break;
            }
        }

        try {
            response = client.newCall(request).execute(); //ответ сервера
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }
}
