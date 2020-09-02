//Класс, ялвяющийся аналогом всех классов из папки res/values
//Нужен для того, чтобы все классы, не наследованные от Activity/Application/Fragment могли поучить доступ к некоторым ресурсам
package com.example.unimag.ui;

public class GlobalVar {

    public static String ip = "192.168.1.71"; //IP сервера
    public static boolean isBuy = false; //ВРЕМЕННО куплена ли хоть одна подписка на партнера

}
