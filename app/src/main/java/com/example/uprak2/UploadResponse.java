package com.example.uprak2;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}