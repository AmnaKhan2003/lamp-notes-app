<?php
include 'db.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_POST['name'];
    $email = $_POST['email'];
    $pass = password_hash($_POST['password'], PASSWORD_BCRYPT);

    $stmt = $conn->prepare("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $name, $email, $pass);
    if ($stmt->execute()) {
        header("Location: login.php");
        exit();
    } else {
        $error = "Signup failed. Try again.";
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Signup</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
  <div class="card p-4 shadow" style="width: 100%; max-width: 400px;">
    <h3 class="text-center mb-3">Signup</h3>

    <?php if (!empty($error)): ?>
      <div class="alert alert-danger text-center"><?= $error ?></div>
    <?php endif; ?>

    <form method="POST">
      <div class="mb-3">
        <input name="name" class="form-control" placeholder="Name" required>
      </div>
      <div class="mb-3">
        <input name="email" type="email" class="form-control" placeholder="Email" required>
      </div>
      <div class="mb-3">
        <input name="password" type="password" class="form-control" placeholder="Password" required>
      </div>
      <button class="btn btn-success w-100">Signup</button>
    </form>
    <div class="text-center mt-3">
      <a href="login.php">Already have an account? Login</a>
    </div>
  </div>
</div>
</body>
</html>
