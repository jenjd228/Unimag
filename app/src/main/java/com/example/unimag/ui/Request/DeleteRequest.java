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

public class DeleteRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private String productHash;
    private String methodName;
    private FragmentManager manager;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public DeleteRequest(Context context, FragmentManager manager, String secureKod, String productHash, String methodName) {
        this.context = context;
        this.manager = manager;
        this.secureKod = secureKod;
        this.productHash = productHash;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName) {
            case "deleteBasketProduct": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/deleteBasketProduct/" + secureKod + "/" + productHash) // The URL to send the data to
                        .get()
                        .build();
                break;
            }
            case "deleteOneProductFromBasket": {
                RequestBody formBody = new FormBody.Builder()
                        .add("productHash", productHash)
                        .add("secureKod", secureKod)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/deleteOneProductFromBasket")
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
