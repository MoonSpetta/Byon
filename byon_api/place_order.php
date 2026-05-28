<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once "db_connection.php";

$data = json_decode(file_get_contents("php://input"), true);
if (!$data) {
    echo json_encode(["success" => false, "message" => "Invalid data"]);
    exit;
}

$user_id = $data['user_id'] ?? 0;
$customer_name = $data['customer_name'];
$address = $data['address'];
$phone = $data['phone'];
$payment_method = $data['payment_method'];
$bank_name = $data['bank_name'] ?? '';
$account_number = $data['account_number'] ?? '';
$total_price = $data['total_price'];
$items = $data['items'];

$order_number = 'ORD' . time() . rand(100, 999);

$conn->begin_transaction();
try {
    $sql = "INSERT INTO orders (user_id, order_number, customer_name, address, phone, payment_method, bank_name, account_number, total_price, status) 
            VALUES ($user_id, '$order_number', '$customer_name', '$address', '$phone', '$payment_method', '$bank_name', '$account_number', $total_price, 'pending')";
    $conn->query($sql);
    $order_id = $conn->insert_id;

    foreach ($items as $item) {
        $product_id = $item['product_id'];
        $name = $conn->real_escape_string($item['name']);
        $price = $item['price'];
        $qty = $item['quantity'];
        $sql2 = "INSERT INTO order_items (order_id, product_id, product_name, price, quantity) VALUES ($order_id, $product_id, '$name', $price, $qty)";
        $conn->query($sql2);
    }
    $conn->commit();
    echo json_encode(["success" => true, "order_id" => $order_id, "order_number" => $order_number]);
} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(["success" => false, "message" => $e->getMessage()]);
}
$conn->close();
?>