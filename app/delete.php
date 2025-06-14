<?php
include 'db.php';
if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$id = $_GET['id'];
$uid = $_SESSION['user_id'];
$conn->query("DELETE FROM notes WHERE id=$id AND user_id=$uid");
header("Location: index.php");
?>
