<?php
$host = 'localhost'; // Change according to your settings
$dbname = 'iiit_guwahati';
$username = 'root'; // Change according to your settings
$password = ''; // Change according to your settings

try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    session_start();

    if (isset($_POST['login'])) {
        $user = $_POST['username'];
        $pass = $_POST['password'];
        $stmt = $conn->prepare('SELECT * FROM users WHERE username = :username');
        $stmt->execute(['username' => $user]);
        $result = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($result && password_verify($pass, $result['password'])) {
            $_SESSION['username'] = $user;
            header('Location: notes.php');
        } else {
            $error = 'Invalid login credentials!';
        }
    }

    if (isset($_POST['register'])) {
        $user = $_POST['username'];
        $pass = password_hash($_POST['password'], PASSWORD_BCRYPT);
        $stmt = $conn->prepare('INSERT INTO users (username, password) VALUES (:username, :password)');
        $stmt->execute(['username' => $user, 'password' => $pass]);
        $success = 'Account created successfully!';
    }
} catch (PDOException $e) {
    echo "Connection failed: " . $e->getMessage();
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notes - IIIT Guwahati</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <header>
        <h1>IIIT Guwahati Notes</h1>
        <nav>
            <ul>
                <li><a href="index.html">Home</a></li>
                <li><a href="education.html">Education</a></li>
                <li><a href="notes.php">Notes</a></li>
            </ul>
        </nav>
    </header>

    <div class="container">
        <?php if (isset($_SESSION['username'])): ?>
            <h2>Welcome, <?php echo $_SESSION['username']; ?>!</h2>
            <p>Here are your notes...</p>
            <!-- Display notes content here -->
        <?php else: ?>
            <h2>Sign In</h2>
            <form method="POST" action="notes.php">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required><br>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required><br>
                <input type="submit" name="login" value="Sign In">
            </form>
            <h2>Sign Up</h2>
            <?php if (isset($success)) echo "<p>$success</p>"; ?>
            <form method="POST" action="notes.php">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required><br>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required><br>
                <input type="submit" name="register" value="Sign Up">
            </form>
            <?php if (isset($error)) echo "<p>$error</p>"; ?>
        <?php endif; ?>
    </div>
</body>
</html>
