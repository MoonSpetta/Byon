<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    include_once "db_connection.php";

    $sql = "SELECT id, name, category, price, rating, image_url FROM products";
    $result = $conn->query($sql);
    $products = [];
    while($row = $result->fetch_assoc()) {
        // Jika image_url kosong, kirim null
        if (empty($row['image_url'])) {
            $row['image_url'] = null;
        }
        $products[] = $row;
    }
    echo json_encode(["status"=>"success", "data"=>$products]);
    $conn->close();
?>