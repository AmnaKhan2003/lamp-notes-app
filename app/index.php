<?php
include 'db.php';

// Handle Note Addition
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $title = $_POST['title'];
    $desc = $_POST['description'];
    $stmt = $conn->prepare("INSERT INTO notes (title, description) VALUES (?, ?)");
    $stmt->bind_param("ss", $title, $desc);
    $stmt->execute();
}

// Fetch Notes
$notes = $conn->query("SELECT * FROM notes ORDER BY created_at DESC");
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Note Taking App</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { background-color: #f0f8ff; }
    .container { max-width: 800px; margin-top: 40px; }
    .note-card { border-left: 6px solid #007bff; padding: 15px; background: white; margin-bottom: 15px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
    .note-actions a { margin-right: 10px; }
  </style>
</head>
<body>
  <div class="container">
    <h2 class="mb-4 text-center">📝 My Notes</h2>
    
    <form method="POST" class="mb-5">
      <div class="mb-3">
        <label class="form-label">Title</label>
        <input name="title" class="form-control" required>
      </div>
      <div class="mb-3">
        <label class="form-label">Description</label>
        <textarea name="description" class="form-control" rows="3" required></textarea>
      </div>
      <button class="btn btn-primary">Add Note</button>
    </form>

    <?php while($row = $notes->fetch_assoc()): ?>
    <div class="note-card">
      <h5><?= htmlspecialchars($row['title']) ?></h5>
      <p><?= nl2br(htmlspecialchars($row['description'])) ?></p>
      <div class="note-actions">
        <a href="edit.php?id=<?= $row['id'] ?>" class="btn btn-sm btn-outline-secondary">Edit</a>
        <a href="delete.php?id=<?= $row['id'] ?>" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure?')">Delete</a>
      </div>
    </div>
    <?php endwhile; ?>
  </div>
</body>
</html>
