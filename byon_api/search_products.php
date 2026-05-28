<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    include_once "db_connection.php";

    $keyword = isset($_GET['keyword']) ? $_GET['keyword'] : '';
    $category = isset($_GET['category']) ? $_GET['category'] : '';
    $minPrice = isset($_GET['min_price']) ? (int)$_GET['min_price'] : 0;
    $maxPrice = isset($_GET['max_price']) ? (int)$_GET['max_price'] : 999999999;

    $sql = "SELECT id, name, category, price, rating, image_url FROM products WHERE 1=1";
    if(!empty($keyword)) $sql .= " AND name LIKE '%".$conn->real_escape_string($keyword)."%'";
    if(!empty($category) && $category!='Semua') $sql .= " AND category = '".$conn->real_escape_string($category)."'";
    if($minPrice>0) $sql .= " AND price >= $minPrice";
    if($maxPrice<999999999) $sql .= " AND price <= $maxPrice";

    $result = $conn->query($sql);
    $products = [];
    while($row = $result->fetch_assoc()) $products[] = $row;
    echo json_encode(["status"=>"success", "data"=>$products]);
    $conn->close();
?>