//Класс, ялвяющийся аналогом всех классов из папки res/values
//Нужен для того, чтобы все классы, не наследованные от Activity/Application/Fragment могли поучить доступ к некоторым ресурсам
package com.example.unimag.ui;

public class GlobalVar {

    public static String ip = "http://192.168.0.102:8080"; //IP сервераhttps://unimagserver.herokuapp.com
    public static boolean isBuy = false; //ВРЕМЕННО куплена ли хоть одна подписка на партнера


}
