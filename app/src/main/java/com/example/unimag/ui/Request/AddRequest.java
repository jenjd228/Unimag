package com.example.unimag.ui.Request;

import android.os.AsyncTask;

import com.example.unimag.ui.GlobalVar;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private Integer productId;
    private String methodName;
    private String stringIds;


    public AddRequest(Integer productId,String secureKod,String methodName){
        this.productId = productId;
        this.secureKod = secureKod;
        this.methodName = methodName;
    }

    public AddRequest(String stringIds,String secureKod,String methodName){
        this.stringIds = stringIds;
        this.secureKod = secureKod;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response = null;
        switch (methodName){
            case "addToBasket":{
                RequestBody formBody = new FormBody.Builder()
                        .add("id", productId.toString())
                        .add("secureKod",secureKod)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/addToBasket")
                        .post(formBody)
                        .build();
                break;
            }
            case "addToOrders":{
                RequestBody formBody = new FormBody.Builder()
                        .add("secureKod", secureKod)
                        .add("stringIds",stringIds)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/addToOrders")
                        .post(formBody)
                        .build();
                break;
            }
            case "addOneProductToBasket":{
                RequestBody formBody = new FormBody.Builder()
                        .add("id",String.valueOf(productId))
                        .add("secureKod", secureKod)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/addOneProductToBasket")
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
            return "";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
    }
}
