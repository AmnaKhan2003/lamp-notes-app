<?php
$host = 'db';
$user = 'root';
$pass = 'root';
$db = 'notesdb';

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>
