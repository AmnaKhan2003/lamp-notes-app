<?php
include 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$uid = $_SESSION['user_id'];

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $title = $_POST['title'];
    $desc = $_POST['description'];
    $stmt = $conn->prepare("INSERT INTO notes (user_id, title, description) VALUES (?, ?, ?)");
    $stmt->bind_param("iss", $uid, $title, $desc);
    $stmt->execute();
}

$notes = $conn->prepare("SELECT * FROM notes WHERE user_id = ? ORDER BY created_at DESC");
$notes->bind_param("i", $uid);
$notes->execute();
$results = $notes->get_result();
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Note Taking App</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container">
    <h2 class="mb-4 text-center">📝 My Notes</h2>
    
    <form method="POST" class="mb-5">
      <div class="mb-3">
        <label class="form-label">Title:</label>
        <input name="title" class="form-control" required>
      </div>
      <div class="mb-3">
        <label class="form-label">Description :</label>
        <textarea name="description" class="form-control" rows="3" required></textarea>
      </div>
      <button class="btn btn-primary">Add Note</button>
    </form>

  <?php while($row = $results->fetch_assoc()): ?>
  <div class="note-card p-3 mb-3 bg-light rounded">
    <h5><?= htmlspecialchars($row['title']) ?></h5>
    <p><?= nl2br(htmlspecialchars($row['description'])) ?></p>
    <a href="edit.php?id=<?= $row['id'] ?>" class="btn btn-sm btn-secondary">Edit</a>
    <a href="delete.php?id=<?= $row['id'] ?>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
  </div>
  <?php endwhile; ?>
</div>
</body>
</html>
