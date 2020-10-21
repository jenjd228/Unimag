package com.example.unimag.email;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// как пример
public class SendEmail {
    /*public void sendEmail(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.143:8080/addEmail") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        Server service = retrofit.create(Server.class);
        ArrayList<String> list = new ArrayList<>();
        list.add("qwe");
        list.add("false");
        list.add("true");
        Call<List<AddEmailResult>> call = service.addEmail(list);
        call.enqueue(new Callback<List<AddEmailResult>>() {
            @Override
            public void onResponse(Call<List<AddEmailResult>> call, Response<List<AddEmailResult>> response) {
                if (response.isSuccessful()) {
                    // запрос выполнился успешно, сервер вернул Status 200
                    System.out.println(1);
                } else {
                    // сервер вернул ошибку
                    System.out.println(2);
                }
            }

            @Override
            public void onFailure(Call<List<AddEmailResult>> call, Throwable t) {
                // ошибка во время выполнения запроса
            }
        });
    }*/
}
