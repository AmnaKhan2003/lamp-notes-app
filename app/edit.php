<?php
include 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$id = $_GET['id'];
$uid = $_SESSION['user_id'];
$note = $conn->query("SELECT * FROM notes WHERE id=$id AND user_id=$uid")->fetch_assoc();

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $title = $_POST['title'];
    $desc = $_POST['description'];
    $conn->query("UPDATE notes SET title='$title', description='$desc' WHERE id=$id AND user_id=$uid");
    header("Location: index.php");
}
?>
<!DOCTYPE html>
<html>
<head><title>Edit Note</title></head>
<body>
<h2>Edit Note</h2>
<form method="POST">
  <input name="title" value="<?= htmlspecialchars($note['title']) ?>" required><br>
  <textarea name="description" required><?= htmlspecialchars($note['description']) ?></textarea><br>
  <button>Update</button>
  <a href="index.php">Back</a>
</form>
</body>
</html>
