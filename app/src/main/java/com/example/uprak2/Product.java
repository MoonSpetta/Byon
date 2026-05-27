package com.example.uprak2;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private int price;

    @SerializedName("rating")
    private double rating;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("description")
    private String description;

    // Constructor lama (tanpa description) bisa tetap dipakai, tapi tambahkan yang baru
    public Product(int id, String name, String category, int price, double rating, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Constructor lengkap (opsional)
    public Product(int id, String name, String category, int price, double rating, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public double getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}