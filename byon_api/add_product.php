<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";

$name = $_POST['name'];
$category = $_POST['category'];
$price = $_POST['price'];
$description = $_POST['description'];

$target_dir = "uploads/";
if (!file_exists($target_dir)) mkdir($target_dir, 0777, true);

$image_name = time() . "_" . basename($_FILES["image"]["name"]);
$target_file = $target_dir . $image_name;
$image_url = "http://" . $_SERVER['HTTP_HOST'] . "/byon_api/" . $target_file;

if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
    $sql = "INSERT INTO products (name, category, price, rating, image_url, description) 
            VALUES ('$name', '$category', $price, 0.0, '$image_url', '$description')";
    if ($conn->query($sql) === TRUE) {
        echo json_encode(["success" => true, "message" => "Produk berhasil ditambahkan"]);
    } else {
        echo json_encode(["success" => false, "message" => "Database error: " . $conn->error]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Gagal upload gambar"]);
}
$conn->close();
?>