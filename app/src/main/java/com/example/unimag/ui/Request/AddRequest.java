package com.example.unimag.ui.Request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.TechnicalWorkFragment;

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
    private String orderId;
    private String totalMoney;
    private String pickUpPoint;
    private String color;
    private Integer size;
    private FragmentManager manager;
    private Context context;

    public AddRequest(Context context, FragmentManager manager, Integer productId,String secureKod, String methodName){
        this.context = context;
        this.manager = manager;
        this.productId = productId;
        this.secureKod = secureKod;
        this.methodName = methodName;
        this.color = "";
        this.size = null;
    }

    public AddRequest(Context context, FragmentManager manager, Integer productId,String secureKod,String color,int size, String methodName){
        this.context = context;
        this.manager = manager;
        this.productId = productId;
        this.secureKod = secureKod;
        this.color = color;
        this.size = size;
        this.methodName = methodName;
    }

    public AddRequest(Context context, String stringIds,String secureKod,String orderId,String totalMoney, String pickUpPoint, String methodName){
        this.context = context;
        /**Нет менеджера*/
        this.stringIds = stringIds;
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
        switch (methodName){
            case "addToBasket":{
                RequestBody formBody = new FormBody.Builder()
                        .add("id", productId.toString())
                        .add("color", color)
                        .add("size", String.valueOf(size))
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
                        .add("totalMoney",totalMoney)
                        .add("orderId",orderId)
                        .add("pickUpPoint", pickUpPoint)
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
