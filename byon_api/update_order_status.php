<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";
$data = json_decode(file_get_contents("php://input"), true);
$order_id = $data['order_id'];
$status = $data['status'];
$sql = "UPDATE orders SET status='$status' WHERE id=$order_id";
if($conn->query($sql)) {
    echo json_encode(["success"=>true]);
} else {
    echo json_encode(["success"=>false, "message"=>$conn->error]);
}
?>