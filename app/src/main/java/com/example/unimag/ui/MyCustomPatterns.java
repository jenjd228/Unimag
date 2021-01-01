package com.example.unimag.ui;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class MyCustomPatterns {

    private static MyCustomPatterns instance;

    private MyCustomPatterns() {

    }

    public static MyCustomPatterns getInstance() {
        if (instance == null) {
            instance = new MyCustomPatterns();
        }
        return instance;
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.find();
    }

    public boolean isValidPassword(String password) {
        //String passwordPravilo = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^\\w\\s]).{6,}$";
        String passwordPravilo = "^[a-zA-Z0-9_]{6,}$";
        Pattern pattern = Pattern.compile(passwordPravilo);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public boolean isValidString(String string) {
        String stringPravilo = "^[a-zа-яA-ZА-Я]+$";
        Pattern pattern = Pattern.compile(stringPravilo);
        Matcher matcher = pattern.matcher(string);

        return matcher.find() && !string.isEmpty();
    }

    public String firstCharToUpperCase(String string){
        if (Character.isLowerCase(string.charAt(0))) {
            string = string.substring(0, 1).toUpperCase() + string.substring(1);
            return string;
        }
        return string;
    }


}
