<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";
$sql = "SELECT id, name, category, price, rating, image_url FROM products ORDER BY RAND() LIMIT 4";
$result = $conn->query($sql);
$products = [];
while($row = $result->fetch_assoc()) {
    $products[] = $row;
}
echo json_encode(["status"=>"success", "data"=>$products]);
?>