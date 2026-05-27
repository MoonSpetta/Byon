package com.example.uprak2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IMAGE_URL = "image_url";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_ID + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Tambah ke keranjang
    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Cek apakah produk sudah ada
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        if (cursor.moveToFirst()) {
            // Jika sudah ada, update quantity
            int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, quantity + 1);
            db.update(TABLE_CART, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});
        } else {
            // Jika belum ada, insert baru
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCT_ID, product.getId());
            values.put(COLUMN_NAME, product.getName());
            values.put(COLUMN_PRICE, product.getPrice());
            values.put(COLUMN_QUANTITY, 1);
            values.put(COLUMN_IMAGE_URL, product.getImageUrl());
            db.insert(TABLE_CART, null, values);
        }
        cursor.close();
        db.close();
    }

    // Ambil semua isi keranjang
    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);
        while (cursor.moveToNext()) {
            CartItem item = new CartItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            item.setProductId(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            item.setPrice(cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE)));
            item.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
            item.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL)));
            cartItems.add(item);
        }
        cursor.close();
        db.close();
        return cartItems;
    }

    // Update quantity
    public void updateQuantity(int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (quantity <= 0) {
            db.delete(TABLE_CART, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, quantity);
            db.update(TABLE_CART, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        }
        db.close();
    }

    // Hapus item
    public void removeFromCart(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Kosongkan keranjang setelah checkout
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }
}