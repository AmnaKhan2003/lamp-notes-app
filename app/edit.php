<?php
include 'db.php';

$id = $_GET['id'];
$note = $conn->query("SELECT * FROM notes WHERE id=$id")->fetch_assoc();

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $title = $_POST['title'];
    $desc = $_POST['description'];
    $conn->query("UPDATE notes SET title='$title', description='$desc' WHERE id=$id");
    header("Location: index.php");
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Note</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container mt-5">
    <h3>Edit Note</h3>
    <form method="POST">
      <div class="mb-3">
        <label class="form-label">Title</label>
        <input name="title" class="form-control" value="<?= htmlspecialchars($note['title']) ?>" required>
      </div>
      <div class="mb-3">
        <label class="form-label">Description</label>
        <textarea name="description" class="form-control" rows="3" required><?= htmlspecialchars($note['description']) ?></textarea>
      </div>
      <button class="btn btn-success">Update</button>
      <a href="index.php" class="btn btn-secondary">Back</a>
    </form>
  </div>
</body>
</html>
