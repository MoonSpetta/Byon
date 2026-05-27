package com.example.uprak2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AdminDashboardActivity extends AppCompatActivity {

    private EditText etName, etPrice, etDescription;
    private Spinner spinnerCategory;
    private ImageView ivProductImage;
    private Button btnSelectImage, btnUploadProduct;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Produk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, AdminHomeActivity.class));
            finish();
        });

        etName = findViewById(R.id.etProductName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etPrice = findViewById(R.id.etProductPrice);
        etDescription = findViewById(R.id.etProductDescription);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUploadProduct = findViewById(R.id.btnUploadProduct);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> checkPermissionAndOpenGallery());
        btnUploadProduct.setOnClickListener(v -> uploadProduct());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefs.getBoolean("isAdminLoggedIn", false)) {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        }
    }

    private void checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Izin diperlukan untuk memilih gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membuka galeri: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivProductImage.setImageURI(selectedImageUri);
        } else {
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProduct() {
        String name = etName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Harap lengkapi semua data dan pilih gambar", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = getFileFromUri(selectedImageUri);
        if (imageFile == null) {
            Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody categoryPart = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody descPart = RequestBody.create(MediaType.parse("text/plain"), description);

        ApiService apiService = RetrofitClient.getApiService();
        Call<UploadResponse> call = apiService.uploadProduct(namePart, categoryPart, pricePart, descPart, body);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(AdminDashboardActivity.this, "Produk berhasil ditambahkan!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AdminDashboardActivity.this, AdminHomeActivity.class));
                    finish();
                } else {
                    String msg = (response.body() != null) ? response.body().getMessage() : "Unknown error";
                    Toast.makeText(AdminDashboardActivity.this, "Upload gagal: " + msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Toast.makeText(AdminDashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File tempFile = new File(getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}