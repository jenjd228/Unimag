package com.example.unimag.ui.Request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;

import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.sort.GlobalSort;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private Integer currentNumberList;
    private String methodName;
    private FragmentManager manager;
    private Integer orderId;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public GetRequest(Context context, FragmentManager manager, String secureKod, String methodName) {
        this.context = context;
        this.manager = manager;
        this.secureKod = secureKod;
        this.methodName = methodName;
    }

    public GetRequest(Context context, FragmentManager manager, Integer currentNumberList, String methodName) {
        this.context = context;
        this.manager = manager;
        this.currentNumberList = currentNumberList;
        this.methodName = methodName;
    }

    public GetRequest(Context context, FragmentManager manager, String methodName) {
        this.context = context;
        this.manager = manager;
        this.methodName = methodName;
    }

    public GetRequest(Context context, FragmentManager manager, String secureKod, Integer orderId, String methodName) {
        this.orderId = orderId;
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
            case "getOrdersList": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getOrdersList/" + secureKod)
                        .get()
                        .build();
                break;
            }
            case "getBasketList": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getBasketList/" + secureKod)
                        .get()
                        .build();
                break;
            }
            case "getList": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getList/" + currentNumberList + "/" + GlobalSort.getInstance().getSpinnerSortItemNameCategory() + "/" + GlobalSort.getInstance().getSortByPriceString() + "/" + GlobalSort.getInstance().getWhereFlag())
                        .get()
                        .build();
                break;
            }
            case "getUser": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getUser/" + secureKod)
                        .get()
                        .build();
                break;
            }
            case "getPartner": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getPartner/")
                        .get()
                        .build();
                break;
            }
            case "getCatalogSize": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getCatalogSize")
                        .get()
                        .build();
                break;
            }
            case "getPickUpPointList": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getPickUpPointList")
                        .get()
                        .build();
                break;
            }
            case "getOrderToProductList": {
                request = new Request.Builder()
                        .url(GlobalVar.ip + "/getOrderToProductList/"+secureKod+"/"+orderId+"")
                        .get()
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