package com.example.uprak2;

import java.util.List;

public class ApiResponse {
    private String status;
    private String message;
    private List<Product> data;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<Product> getData() { return data; }
}