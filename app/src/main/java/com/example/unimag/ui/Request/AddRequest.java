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

public class AddRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private Integer productId;
    private String productHash;
    private String methodName;
    private String productList;
    private String orderId;
    private String totalMoney;
    private String pickUpPoint;
    private String color;
    private String size;
    private FragmentManager manager;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public AddRequest(Context context, FragmentManager manager, String productHash, String secureKod, String methodName) {
        this.context = context;
        this.manager = manager;
        this.productHash = productHash;
        this.secureKod = secureKod;
        this.methodName = methodName;
        this.color = "";
        this.size = null;
    }

    public AddRequest(Context context, FragmentManager manager, String productHash, String secureKod, String color, String size, String methodName) {
        this.context = context;
        this.manager = manager;
        this.productHash = productHash;
        this.secureKod = secureKod;
        this.color = color;
        this.size = size;
        this.methodName = methodName;
    }

    public AddRequest(Context context, String productList, String secureKod, String orderId, String totalMoney, String pickUpPoint, String methodName) {
        this.context = context;
        /**Нет менеджера*/
        this.productList = productList;
        this.secureKod = secureKod;
        this.methodName = methodName;
        this.orderId = orderId;
        this.totalMoney = totalMoney;
        this.pickUpPoint = pickUpPoint;
        this.color = "";
        this.size = null;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response = null;
        switch (methodName) {
            case "addToBasket": {
                RequestBody formBody = new FormBody.Builder()
                        .add("productHash", productHash)
                        .add("color", color)
                        .add("size", size)
                        .add("secureKod", secureKod)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/addToBasket")
                        .post(formBody)
                        .build();
                break;
            }
            case "addToOrders": {
                RequestBody formBody = new FormBody.Builder()
                        .add("secureKod", secureKod)
                        .add("productList", productList)
                        .add("totalMoney", totalMoney)
                        .add("orderId", orderId)
                        .add("pickUpPoint", pickUpPoint)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/addToOrders")
                        .post(formBody)
                        .build();
                break;
            }
            case "addOneProductToBasket": {
                RequestBody formBody = new FormBody.Builder()
                        .add("productHash", productHash)
                        .add("secureKod", secureKod)
                        .build();
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/addOneProductToBasket")
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
