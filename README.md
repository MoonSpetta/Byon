Berikut adalah deskripsi proyek yang bisa kamu gunakan di repository GitHub (README.md). Isinya mencakup fitur utama, teknologi yang digunakan, dan sedikit panduan.

---

```markdown
# 🛍️ Byon – Aplikasi E-Commerce Mobile + Admin Panel

**Byon** adalah aplikasi belanja online berbasis Android yang dibangun dengan **Java** (XML layout) dan backend **PHP (Native)** dengan database **MySQL**. Aplikasi ini memiliki dua antarmuka: **User** untuk pembeli dan **Admin** untuk pengelola produk & pesanan.

---

## ✨ Fitur Utama

### 👤 Pengguna (User)
- Login & Registrasi (email & password)
- Lihat daftar produk (produk populer, flash sale)
- Filter produk berdasarkan kategori, harga, rating
- Pencarian produk
- Keranjang belanja (tambah, ubah jumlah, hapus)
- Checkout dengan data pengiriman & metode pembayaran (COD / Transfer Bank)
- Riwayat pesanan dan detail status (pending, processing, shipped, delivered, cancelled)
- Logout

### 👑 Admin
- Login khusus (email: `admin@gmail.com`, pass: `admin123`)
- Dashboard admin dengan akses rahasia (klik 5x pada splash screen)
- Tambah produk baru (nama, kategori, harga, deskripsi, upload gambar)
- Edit produk (ubah data & gambar)
- Hapus produk
- Lihat semua pesanan dari pengguna
- Ubah status pesanan (pending → processing → shipped → delivered / cancelled)
- Logout

---

## 🛠️ Teknologi yang Digunakan

### Android (Client)
- **Bahasa**: Java
- **Layout**: XML
- **Arsitektur**: Retrofit 2 (HTTP Client), Glide (loading gambar)
- **Database Lokal**: SQLite (untuk keranjang belanja)
- **Minimum SDK**: API 26 (Android 8.0)

### Backend (Server Lokal)
- **Web Server**: Laragon (Apache + MySQL)
- **Bahasa**: PHP Native (tanpa framework)
- **Database**: MySQL (tabel: users, products, orders, order_items)
- **Autentikasi**: SharedPreferences (session sederhana)

---

## 🚀 Cara Menjalankan Proyek

### 1. Clone Repository
```bash
git clone https://github.com/username/byon-app.git
```

### 2. Setup Backend (Laragon)
- Install Laragon dan pastikan Apache & MySQL berjalan.
- Buat database baru dengan nama `byon_db`.
- Import file SQL yang tersedia di folder `database/byon_db.sql`.
- Letakkan folder `byon_api` di dalam `C:\laragon\www\`.
- Sesuaikan `BASE_URL` di `RetrofitClient.java` dengan IP komputer atau `10.0.2.2` (untuk emulator).

### 3. Jalankan Aplikasi Android
- Buka proyek dengan Android Studio.
- Sync Gradle dan Build.
- Install ke HP/emulator.

### 4. Login
- **User**: registrasi terlebih dahulu.
- **Admin**: klik logo atau nama aplikasi 5x pada splash screen, lalu login dengan `admin@gmail.com` / `admin123`.

---

## 📁 Struktur Proyek (Singkat)

```
byon-app/
├── app/
│   ├── src/main/java/com/example/uprak2/
│   │   ├── activities/   (Login, Register, Home, Cart, Checkout, MyOrder, dll)
│   │   ├── adapters/     (ProductAdapter, CartAdapter, OrderAdapter)
│   │   ├── models/       (Product, CartItem, Order, ApiResponse, dll)
│   │   ├── network/      (ApiService, RetrofitClient)
│   │   └── utils/        (CartDatabaseHelper, UploadResponse)
│   └── res/layout/       (Semua layout XML)
├── backend/ (byon_api)
│   ├── db_connection.php
│   ├── get_products.php, search_products.php
│   ├── add_product.php, update_product.php, delete_product.php
│   ├── place_order.php, get_my_orders.php, get_all_orders.php
│   ├── update_order_status.php
│   └── uploads/ (folder untuk gambar produk)
└── README.md
```

---

## 📸 Screenshot (Contoh)

> *Tambahkan beberapa screenshot hasil running di sini*

| Halaman User | Halaman Admin |
|--------------|----------------|
| Home         | Dashboard      |
| Keranjang    | Tambah Produk  |
| Checkout     | Kelola Pesanan |

---

## 🙏 Catatan

- Proyek ini dibuat untuk keperluan pembelajaran dan pengembangan.
- Untuk lingkungan produksi, sebaiknya gunakan HTTPS dan sistem autentikasi yang lebih aman (token JWT, dll).
- Fitur transfer bank hanya simulasi; tidak terhubung dengan payment gateway sesungguhnya.

---

## 📄 Lisensi

[MIT](LICENSE) – bebas digunakan dan dikembangkan lebih lanjut.

---

**Dibuat dengan ❤️ untuk tugas akhir / portofolio**
```
