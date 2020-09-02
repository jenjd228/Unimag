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

public class GetRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private Integer currentNumberList;
    private String methodName;

    public GetRequest(String secureKod,String methodName){
        this.secureKod = secureKod;
        this.methodName = methodName;
    }

    public GetRequest(Integer currentNumberList,String methodName){
        this.currentNumberList = currentNumberList;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response = null;
        switch (methodName){
            case "getOrdersList":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getOrders/"+secureKod)
                        .get()
                        .build();
                break;
            }
            case "getBasketList":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getBasketList/"+secureKod)
                        .get()
                        .build();
                break;
            }
            case "getList":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getList/"+currentNumberList)
                        .get()
                        .build();
                break;
            }
            case "getUser":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getUser/"+secureKod)
                        .get()
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
            return "";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }
}