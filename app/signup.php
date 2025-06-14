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
    } else {
        echo "Signup failed. Try again.";
    }
}
?>
<!DOCTYPE html>
<html>
<head><title>Signup</title></head>
<body>
<h2>Signup</h2>
<form method="POST">
  <input name="name" placeholder="Name" required><br>
  <input name="email" type="email" placeholder="Email" required><br>
  <input name="password" type="password" placeholder="Password" required><br>
  <button>Signup</button>
</form>
<a href="login.php">Already have an account? Login</a>
</body>
</html>
