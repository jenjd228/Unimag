package com.example.unimag.email;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Server {
    @POST("/addEmail")
    Call<List<AddEmailResult>> addEmail(@Body List<String> emails);
}
