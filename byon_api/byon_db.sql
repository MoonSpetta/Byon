-- 1. Membuat database
CREATE DATABASE IF NOT EXISTS `byon_db`;
USE `byon_db`;

-- 2. Membuat tabel `users`
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Membuat tabel `products` (katalog produk)
CREATE TABLE IF NOT EXISTS `products` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `category` ENUM('Fashion', 'Electronics', 'Furniture', 'Beauty') NOT NULL,
    `price` INT NOT NULL,
    `rating` DECIMAL(2,1) DEFAULT 0.0,
    `image_url` TEXT,
    `description` TEXT
);

-- 4. Membuat tabel `carts` untuk menyimpan keranjang belanja
CREATE TABLE IF NOT EXISTS `carts` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `added_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);

-- 5. Menambahkan beberapa data produk contoh
INSERT INTO `products` (`name`, `category`, `price`, `rating`, `image_url`, `description`) VALUES
('Kaos Polos Hitam', 'Fashion', 150000, 4.5, 'kaos_polos_hitam.jpg', 'Kaos katun premium yang nyaman dipakai.'),
('Jaket Denim', 'Fashion', 350000, 4.2, 'jaket_denim.jpg', 'Jaket denim trendi untuk tampilan kasual.'),
('Headset Gaming', 'Electronics', 250000, 4.8, 'headset_gaming.jpg', 'Headset dengan suara jernih dan bass yang mantap.'),
('Smartwatch', 'Electronics', 500000, 4.3, 'smartwatch.jpg', 'Jam tangan pintar dengan berbagai fitur kesehatan.'),
('Meja Belajar', 'Furniture', 750000, 4.0, 'meja_belajar.jpg', 'Meja belajar minimalis dengan desain ergonomis.'),
('Kursi Kantor', 'Furniture', 1200000, 4.6, 'kursi_kantor.jpg', 'Kursi kantor dengan bantalan empuk dan sandaran yang dapat diatur.'),
('Lipstik Matte', 'Beauty', 85000, 4.7, 'lipstik_matte.jpg', 'Lipstik matte dengan warna tahan lama.'),
('Serum Wajah', 'Beauty', 180000, 4.9, 'serum_wajah.jpg', 'Serum vitamin C untuk kulit cerah merata.');