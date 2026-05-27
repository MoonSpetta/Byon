package com.example.uprak2;
import java.util.List;

public class PlaceOrderRequest {
    public int user_id;
    public String customer_name;
    public String address;
    public String phone;
    public String payment_method;
    public String bank_name;
    public String account_number;
    public int total_price;
    public List<PlaceOrderItem> items;

    public static class PlaceOrderItem {
        public int product_id;
        public String name;
        public int price;
        public int quantity;
    }
}