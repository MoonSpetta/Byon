<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";
$order_id = $_GET['order_id'];
$sql = "SELECT * FROM orders WHERE id = $order_id";
$result = $conn->query($sql);
$order = $result->fetch_assoc();
$items_sql = "SELECT * FROM order_items WHERE order_id = $order_id";
$items_result = $conn->query($items_sql);
$items = [];
while($row = $items_result->fetch_assoc()) {
    $items[] = $row;
}
echo json_encode(["status"=>"success", "order"=>$order, "items"=>$items]);
?>