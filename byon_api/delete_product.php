<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";

$id = $_POST['id'];
$sql = "DELETE FROM products WHERE id=$id";
if ($conn->query($sql) === TRUE) {
    echo json_encode(["success" => true, "message" => "Produk dihapus"]);
} else {
    echo json_encode(["success" => false, "message" => "Gagal hapus: " . $conn->error]);
}
$conn->close();
?>