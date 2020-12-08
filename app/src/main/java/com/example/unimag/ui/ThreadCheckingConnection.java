package com.example.unimag.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ThreadCheckingConnection extends AsyncTask<Void, Void, Boolean> {

    private String host = GlobalVar.ip; //Хост (Примеры: "google.com", "8.8.8.8")
    private int port = 8080; //Порт
    private int timeout = 2000; //Время в мс для отклика сервера
    private FragmentManager fragmentManager;
    private  Bundle savedInstanceState;

    public ThreadCheckingConnection(FragmentManager fragmentManager, Bundle savedInstanceState) { //Констуркутор класса
        this.fragmentManager = fragmentManager;
        this.savedInstanceState = savedInstanceState;

        //Если нет подключения к интернету
        /**if(!isConnect(context)) {
            FragmentManager manager = fragmentManager;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(fragmentManager.getFragments().get(0).getId(), new NoConnectionFragment()); //Переходим на новый фрагмент
            transaction.addToBackStack("last_no_connection_fragment"); //Добавляем в стек
            transaction.commit();
        }*/
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


    @Override
    protected Boolean doInBackground(Void... params) {
        System.out.println("Start");
        Boolean isConnecting = false;
        try (final Socket socket = new Socket()) { //Отправляем запрос на сервер
            System.out.println("Try");
            final InetAddress inetAddress = InetAddress.getByName(host);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
            socket.connect(inetSocketAddress, timeout); //Подключаемся к серверу
            isConnecting = true;
        } catch (java.io.IOException e) { //Если сервер недоступен
            e.printStackTrace();
            System.out.println("Catch");
            isConnecting = false;
        }
        return isConnecting;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        System.out.println("onPost");
        if (result) {
            System.out.println("Connect");
        } else {
            System.out.println("No Connect");
            FragmentManager manager = fragmentManager;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(fragmentManager.getFragments().get(0).getId(), new NoConnectionFragment()); //Переходим на новый фрагмент
            transaction.addToBackStack("last_no_connection_fragment"); //Добавляем в стек
            transaction.commit();
        }
    }

}
