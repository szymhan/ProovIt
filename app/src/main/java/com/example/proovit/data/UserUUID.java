package com.example.proovit.data;


import com.google.gson.annotations.SerializedName;

public class UserUUID {
    @SerializedName("userUUID")
    private String UUID;

    public UserUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
