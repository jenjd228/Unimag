package com.example.unimag.ui.Request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.TechnicalWorkFragment;
import com.example.unimag.ui.basket.GridAdapterBasket;
import com.example.unimag.ui.catalog.CustomGridAdapter;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendOrUpdateRequest extends AsyncTask<Void, Void, String> {
    private String email;
    private String fio;
    private String birthDay;
    private String methodName;
    private String password;
    private FragmentManager manager;
    private Context context;

    public SendOrUpdateRequest(Context context, FragmentManager manager, String email,String password,String methodName){
        this.context = context;
        this.manager = manager;
        this.email = email;
        this.password = password;
        this.methodName = methodName;
    }

    public SendOrUpdateRequest(Context context, FragmentManager manager, String email,String fio,String birthDay,String methodName){
        this.context = context;
        this.manager = manager;
        this.email = email;
        this.fio = fio;
        this.birthDay = birthDay;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName){
            case "sendMessage":{
                RequestBody formBody = new FormBody.Builder()
                        .add("email",email)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/sendMessage")
                        .post(formBody)
                        .build();
                break;
            }
            case "firstUpdate":{
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password",  password)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/firstUpdate")
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
                        .url("http://"+ GlobalVar.ip +":8080/userUpdate")
                        .post(formBody)
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

            //Если инет есть - то значит идут технические работы
            if(isConnect(context)) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(manager.getFragments().get(0).getId(), new TechnicalWorkFragment()); //Переходим на новый фрагмент
                transaction.commit();
            }

            return "";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }

    //Проверка подключения к интернету
    public static boolean isConnect(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
