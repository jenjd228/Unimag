package com.example.unimag.ui.Request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.TechnicalWorkFragment;
import com.example.unimag.ui.sort.GlobalSort;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private Integer currentNumberList;
    private String methodName;
    private FragmentManager manager;
    private Context context;

    public GetRequest(Context context, FragmentManager manager, String secureKod, String methodName){
        this.context = context;
        this.manager = manager;
        this.secureKod = secureKod;
        this.methodName = methodName;
    }

    public GetRequest(Context context,FragmentManager manager, Integer currentNumberList, String methodName){
        this.context = context;
        this.manager = manager;
        this.currentNumberList = currentNumberList;
        this.methodName = methodName;
    }

    public GetRequest(Context context,FragmentManager manager, String methodName){
        this.context = context;
        this.manager = manager;
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
            case "getPickUpPointList":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/getPickUpPointList")
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