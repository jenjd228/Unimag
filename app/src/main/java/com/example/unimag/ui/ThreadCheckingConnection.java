package com.example.unimag.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ThreadCheckingConnection extends Thread {

    public ThreadCheckingConnection(FragmentManager fragmentManager, Context context) { //Констуркутор класса

        //Если нет подключения к интернету
        if (!isConnect(context)) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(fragmentManager.getFragments().get(0).getId(), new NoConnectionFragment()); //Переходим на новый фрагмент
            transaction.addToBackStack("last_no_connection_fragment"); //Добавляем в стек
            transaction.commit();
        }
    }


    //Проверка подключения к интернету
    public static boolean isConnect(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void goToTechnicalWorkFragment(FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentManager.getFragments().get(0).getId(), new TechnicalWorkFragment()); //Переходим на новый фрагмент
        transaction.commit();
    }
}
