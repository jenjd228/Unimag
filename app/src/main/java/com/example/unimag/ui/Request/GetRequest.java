package com.example.unimag.ui.Request;

import android.os.AsyncTask;

import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.sort.GlobalSort;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    public GetRequest(String methodName){
        this.methodName = methodName;
    }

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName){
            case "getOrdersList":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getOrdersList/"+secureKod)
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
                        .url("http://"+ GlobalVar.ip +":8080/getList/"+currentNumberList +"/"+GlobalSort.getInstance().getSpinnerSortItemNameCategory()+"/"+GlobalSort.getInstance().getSortByPriceString()+"/"+GlobalSort.getInstance().getWhereFlag())
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
            case "getPartner":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getPartner/")
                        .get()
                        .build();
                break;
            }
            case "getCatalogSize":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getCatalogSize")
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