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

public class DeleteRequest extends AsyncTask<Void, Void, String> {
    private String secureKod;
    private Integer productId;
    private String methodName;
    private FragmentManager manager;
    private Context context;

    public DeleteRequest(Context context, FragmentManager manager, String secureKod,Integer productId,String methodName){
        this.context = context;
        this.manager = manager;
        this.secureKod = secureKod;
        this.productId = productId;
        this.methodName = methodName;
    }


    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response;
        switch (methodName){
            case "deleteBasketProduct":{
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/deleteBasketProduct/"+secureKod+"/"+productId) // The URL to send the data to
                        .get()
                        .build();
                break;
            }
            case "deleteOneProductFromBasket" : {
                RequestBody formBody = new FormBody.Builder()
                        .add("id", productId.toString())
                        .add("secureKod",secureKod)
                        .build();
                request = new Request.Builder()
                        .url("http://"+ GlobalVar.ip +":8080/deleteOneProductFromBasket")
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
