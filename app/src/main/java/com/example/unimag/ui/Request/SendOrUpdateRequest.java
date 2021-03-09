package com.example.unimag.ui.Request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;

import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.ThreadCheckingConnection;

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
    private String secureKod;

    private FragmentManager manager;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public SendOrUpdateRequest(Context context, FragmentManager manager, String fio, String secureKod, String methodName) {
        this.context = context;
        this.manager = manager;
        this.fio = fio;
        this.secureKod = secureKod;
        this.methodName = methodName;
    }

    public SendOrUpdateRequest(Context context, FragmentManager manager, String email, String password, String fio, String birthDay, String methodName) {
        this.context = context;
        this.manager = manager;
        this.email = email;
        this.password = password;
        this.fio = fio;
        this.birthDay = birthDay;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName) {
            case "sendMessage": {
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/sendMessage")
                        .post(formBody)
                        .build();
                break;
            }
            case "firstUpdate": {
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .add("fio", fio)
                        .add("birthData", birthDay)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/firstUpdate")
                        .post(formBody)
                        .build();
                break;
            }
            case "userUpdate": {
                RequestBody formBody = new FormBody.Builder()
                        .add("fio", fio)
                        .add("secureKod",secureKod)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/userUpdate")
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

            //Если инет есть - то значит идут технические работы
            if (ThreadCheckingConnection.isConnect(context)) {
                ThreadCheckingConnection.goToTechnicalWorkFragment(manager);
            }
            return "";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }
}
