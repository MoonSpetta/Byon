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
