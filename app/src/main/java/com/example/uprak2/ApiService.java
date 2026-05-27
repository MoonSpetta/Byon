package com.example.uprak2;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("get_products.php")
    Call<ApiResponse> getProducts();

    @GET("search_products.php")
    Call<ApiResponse> searchProducts(@Query("keyword") String keyword,
                                     @Query("category") String category,
                                     @Query("min_price") int minPrice,
                                     @Query("max_price") int maxPrice);

    @GET("get_product_detail.php")
    Call<ProductDetailResponse> getProductDetail(@Query("id") int productId);

    @GET("get_flash_sale_products.php")
    Call<ApiResponse> getFlashSaleProducts();

    @Multipart
    @POST("add_product.php")
    Call<UploadResponse> uploadProduct(@Part("name") RequestBody name,
                                       @Part("category") RequestBody category,
                                       @Part("price") RequestBody price,
                                       @Part("description") RequestBody description,
                                       @Part MultipartBody.Part image);

    @Multipart
    @POST("update_product.php")
    Call<UploadResponse> updateProduct(@Part("id") RequestBody id,
                                       @Part("name") RequestBody name,
                                       @Part("category") RequestBody category,
                                       @Part("price") RequestBody price,
                                       @Part("description") RequestBody description,
                                       @Part MultipartBody.Part image);

    @POST("delete_product.php")
    Call<UploadResponse> deleteProduct(@Part("id") RequestBody id);

    @GET("get_my_orders.php")
    Call<OrdersResponse> getMyOrders(@Query("user_id") int userId);

    @GET("get_order_detail.php")
    Call<OrderDetailResponse> getOrderDetail(@Query("order_id") int orderId);

    @POST("place_order.php")
    Call<PlaceOrderResponse> placeOrder(@Body PlaceOrderRequest request);

    @GET("get_all_orders.php")
    Call<OrdersResponse> getAllOrders();

    @POST("update_order_status.php")
    Call<UpdateStatusResponse> updateOrderStatus(@Body UpdateStatusRequest request);
}