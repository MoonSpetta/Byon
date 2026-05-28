<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";

$id = $_POST['id'];
$name = $_POST['name'];
$category = $_POST['category'];
$price = $_POST['price'];
$description = $_POST['description'];

$image_url = null;
if (isset($_FILES['image']) && $_FILES['image']['error'] == 0) {
    $target_dir = "uploads/";
    if (!file_exists($target_dir)) mkdir($target_dir, 0777, true);
    $image_name = time() . "_" . basename($_FILES["image"]["name"]);
    $target_file = $target_dir . $image_name;
    if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
        $image_url = "http://" . $_SERVER['HTTP_HOST'] . "/byon_api/" . $target_file;
    }
}

if ($image_url) {
    $sql = "UPDATE products SET name='$name', category='$category', price=$price, description='$description', image_url='$image_url' WHERE id=$id";
} else {
    $sql = "UPDATE products SET name='$name', category='$category', price=$price, description='$description' WHERE id=$id";
}

if ($conn->query($sql) === TRUE) {
    echo json_encode(["success" => true, "message" => "Produk berhasil diupdate"]);
} else {
    echo json_encode(["success" => false, "message" => "Database error: " . $conn->error]);
}
$conn->close();
?>