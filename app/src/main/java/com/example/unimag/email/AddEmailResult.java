package com.example.unimag.email;

import com.google.gson.annotations.SerializedName;

public class AddEmailResult {
    @SerializedName("email")
    public String email;
    @SerializedName("primary")
    public Boolean primary;
    @SerializedName("verified")
    public Boolean verified;
}
