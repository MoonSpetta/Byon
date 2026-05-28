<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    include_once "db_connection.php";

    $id = isset($_GET['id']) ? (int)$_GET['id'] : 0;

    if ($id <= 0) {
        echo json_encode(["status"=>"error", "message"=>"ID produk tidak valid"]);
        exit;
    }

    $sql = "SELECT id, name, category, price, rating, image_url, description FROM products WHERE id = $id";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $product = $result->fetch_assoc();
        echo json_encode(["status"=>"success", "data"=>$product]);
    } else {
        echo json_encode(["status"=>"error", "message"=>"Produk tidak ditemukan"]);
    }
    $conn->close();
?>