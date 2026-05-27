package com.example.uprak2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditProductActivity extends AppCompatActivity {

    private EditText etName, etPrice, etDescription;
    private Spinner spinnerCategory;
    private ImageView ivProductImage;
    private Button btnSelectImage, btnUpdate, btnCancel;
    private Uri selectedImageUri;
    private int productId;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Produk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        productId = getIntent().getIntExtra("product_id", 0);
        if (productId == 0) {
            Toast.makeText(this, "ID produk tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etName = findViewById(R.id.etEditProductName);
        etPrice = findViewById(R.id.etEditProductPrice);
        etDescription = findViewById(R.id.etEditProductDescription);
        spinnerCategory = findViewById(R.id.spinnerEditCategory);
        ivProductImage = findViewById(R.id.ivEditProductImage);
        btnSelectImage = findViewById(R.id.btnEditSelectImage);
        btnUpdate = findViewById(R.id.btnUpdateProduct);
        btnCancel = findViewById(R.id.btnCancelEdit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> checkPermissionAndOpenGallery());
        btnUpdate.setOnClickListener(v -> updateProduct());
        btnCancel.setOnClickListener(v -> finish());

        loadProductData();
    }

    private void loadProductData() {
        RetrofitClient.getApiService().getProductDetail(productId).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Product p = response.body().getData();
                    etName.setText(p.getName());
                    etPrice.setText(String.valueOf(p.getPrice()));
                    etDescription.setText(p.getDescription());
                    // set spinner
                    String[] categories = getResources().getStringArray(R.array.categories_array);
                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equalsIgnoreCase(p.getCategory())) {
                            spinnerCategory.setSelection(i);
                            break;
                        }
                    }
                    if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
                        Glide.with(EditProductActivity.this).load(p.getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(ivProductImage);
                    }
                } else {
                    Toast.makeText(EditProductActivity.this, "Gagal memuat data produk", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            } else openGallery();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Izin diperlukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivProductImage.setImageURI(selectedImageUri);
        }
    }

    private void updateProduct() {
        String name = etName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Nama dan harga harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            File file = getFileFromUri(selectedImageUri);
            if (file != null) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                imagePart = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            }
        }

        RequestBody idPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productId));
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody catPart = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody descPart = RequestBody.create(MediaType.parse("text/plain"), description);

        Call<UploadResponse> call = RetrofitClient.getApiService().updateProduct(idPart, namePart, catPart, pricePart, descPart, imagePart);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(EditProductActivity.this, "Produk berhasil diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Update gagal";
                    Toast.makeText(EditProductActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            File temp = new File(getCacheDir(), "temp_edit_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream os = new FileOutputStream(temp);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) os.write(buffer, 0, len);
            os.close();
            is.close();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}