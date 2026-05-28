<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";
$sql = "SELECT * FROM orders ORDER BY created_at DESC";
$result = $conn->query($sql);
$orders = [];
while($row = $result->fetch_assoc()) {
    $orders[] = $row;
}
echo json_encode(["status"=>"success", "data"=>$orders]);
?>