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

public class CheckRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private String email;
    private String kodOrSecureKod;
    private String password;
    private String methodName;
    private FragmentManager manager;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public CheckRequest(Context context, FragmentManager manager, String email, String kodOrSecureKod, String password, String methodName) {
        this.context = context;
        this.manager = manager;
        this.email = email;
        this.kodOrSecureKod = kodOrSecureKod;
        this.password = password;
        this.methodName = methodName;
    }

    public CheckRequest(Context context, FragmentManager manager, String email, String password, String methodName) {
        this.context = context;
        this.manager = manager;
        this.email = email;
        this.password = password;
        this.methodName = methodName;
    }

    public CheckRequest(Context context, FragmentManager manager, String secureKod, String methodName) {
        this.context = context;
        this.manager = manager;
        this.secureKod = secureKod;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName) {
            case "checkBySecureKod": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/checkBySecureKod/" + secureKod)
                        .get()
                        .build();
                break;
            }
            case "checkUserForLoginIn": {
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/checkUserForLoginIn")
                        .post(formBody)
                        .build();
                break;
            }
            case "userIsSub": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/userIsSub/" + secureKod)
                        .get()
                        .build();
                break;
            }
            case "checkByKod": {
                RequestBody formBody = new FormBody.Builder()
                        .add("kod", kodOrSecureKod)
                        .add("email", email)
                        .add("password", password)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/checkByKod")
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
