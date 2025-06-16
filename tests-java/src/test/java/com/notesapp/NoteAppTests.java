package com.notesapp;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

import java.time.Duration;
import java.util.List;

public class NoteAppTests {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Headless mode
        options.addArguments("--disable-gpu"); // For Windows compatibility (optional)
        options.addArguments("--window-size=1920,1080"); // Ensure full layout loads
        options.addArguments("--no-sandbox"); // Optional for Linux environments
        options.addArguments("--disable-dev-shm-usage"); // Fix for limited resource environments

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String generateRandomEmail() {
        long timestamp = System.currentTimeMillis();
        return "testuser" + timestamp + "@example.com";
    }

    @Test
    public void signupUser() {
        String email = generateRandomEmail();

        driver.get("http://localhost/signup.php");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys("testuser1");
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("password");

        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Signup')]"));
        signupButton.click();

        wait.until(ExpectedConditions.urlContains("login.php"));
        Assertions.assertTrue(driver.getPageSource().contains("Login"), "Signup failed or no redirect to login");
    }

    @Test
    public void loginUser() {
        driver.get("http://localhost/login.php");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys("testuser1@example.com");
        driver.findElement(By.name("password")).sendKeys("password");

        // Updated button locator using XPath
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
        loginButton.click();

        // Validate login successful
        wait.until(ExpectedConditions.urlContains("index.php"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("index.php"), "Login failed");
    }

    public void login() {
        driver.get("http://localhost/login.php");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys("testuser1@example.com");
        driver.findElement(By.name("password")).sendKeys("password");
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
        loginButton.click();
        wait.until(ExpectedConditions.urlContains("index.php"));
    }

    @Test
    public void addNote() throws InterruptedException {
        login();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys("Test Note");
        Thread.sleep(1000);
        driver.findElement(By.name("description")).sendKeys("This is a test note.");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[contains(text(),'Add Note')]")).click();
        Thread.sleep(1000);

        // Wait for note to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("note-card")));

        // Wait 3 seconds so you can visually see it before test continues
        Thread.sleep(3000); // time in milliseconds

        List<WebElement> notes = driver.findElements(By.className("note-card"));
        boolean found = notes.stream().anyMatch(note -> note.getText().contains("Test Note"));

        Assertions.assertTrue(found, "Note was not added successfully");
    }

    @Test
    public void deleteNote() throws InterruptedException {
        login();

        // Step 1: First add a note to delete
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys("Delete Me");
        Thread.sleep(1000); // 1 second pause

        driver.findElement(By.name("description")).sendKeys("This note will be deleted.");
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[contains(text(),'Add Note')]")).click();
        Thread.sleep(1000);

        // Step 2: Wait for the note to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("note-card")));
        Thread.sleep(1000);

        // Step 3: Find the specific note and delete
        List<WebElement> notes = driver.findElements(By.className("note-card"));
        boolean found = false;

        for (WebElement note : notes) {
            if (note.getText().contains("Delete Me")) {
                WebElement deleteButton = note.findElement(By.xpath(".//a[contains(text(),'Delete')]"));
                deleteButton.click();
                Thread.sleep(1000);

                // VERY IMPORTANT: wait for alert to appear
                WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                alertWait.until(ExpectedConditions.alertIsPresent());

                // Handle the confirmation popup
                Alert alert = driver.switchTo().alert();
                alert.accept();
                Thread.sleep(1000);

                found = true;
                break;
            }
        }

        Assertions.assertTrue(found, "Note to delete not found!");

        // Step 4: Wait and verify that the note is deleted
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'note-card') and contains(.,'Delete Me')]")));

        Thread.sleep(3000); // final longer wait for visual confirmation

        notes = driver.findElements(By.className("note-card"));
        boolean stillExists = notes.stream().anyMatch(n -> n.getText().contains("Delete Me"));
        Assertions.assertFalse(stillExists, "Note was not deleted successfully!");
    }

    @Test
    public void logoutUser() {
        login();
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Logout")));
        logoutLink.click();

        wait.until(ExpectedConditions.urlContains("login.php"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("login.php"), "Logout failed or no redirect to login");
    }

    @Test
    public void editNote() throws InterruptedException {
        login();

        // Add note first
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys("Original Title");
        driver.findElement(By.name("description")).sendKeys("Original Description");
        driver.findElement(By.xpath("//button[contains(text(),'Add Note')]")).click();
        Thread.sleep(1000);

        // Edit the added note
        List<WebElement> notes = driver.findElements(By.className("note-card"));
        for (WebElement note : notes) {
            if (note.getText().contains("Original Title")) {
                WebElement editBtn = note.findElement(By.linkText("Edit"));
                editBtn.click();
                break;
            }
        }

        // Edit form
        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        titleInput.clear();
        titleInput.sendKeys("Edited Title");

        WebElement descInput = driver.findElement(By.name("description"));
        descInput.clear();
        descInput.sendKeys("Edited Description");

        driver.findElement(By.xpath("//button[contains(text(),'Update')]")).click();

        wait.until(ExpectedConditions.urlContains("index.php"));
        Assertions.assertTrue(driver.getPageSource().contains("Edited Title"), "Edit note failed");
    }

    @Test
    public void preventEmptyNoteSubmission() {
        login();

        driver.findElement(By.name("title")).clear();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.xpath("//button[contains(text(),'Add Note')]")).click();

        // Since fields are required, user should stay on same page
        Assertions.assertTrue(driver.getCurrentUrl().contains("index.php"), "Empty note submitted incorrectly");
    }

    @Test
    public void preventLoginWithWrongPassword() {
        driver.get("http://localhost/login.php");

        driver.findElement(By.name("email")).sendKeys("testuser1@example.com");
        driver.findElement(By.name("password")).sendKeys("wrongpassword");

        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        Assertions.assertTrue(driver.getPageSource().contains("Invalid credentials"), "Invalid login not handled");
    }

    @Test
    public void duplicateSignupShouldFail() {
        driver.get("http://localhost/signup.php");

        driver.findElement(By.name("name")).sendKeys("testuser1");
        driver.findElement(By.name("email")).sendKeys("testuser1@example.com"); // already exists
        driver.findElement(By.name("password")).sendKeys("password");

        driver.findElement(By.xpath("//button[contains(text(),'Signup')]")).click();

        // Assuming your app displays an error for duplicate email
        Assertions.assertTrue(driver.getPageSource().contains("already exists") ||
                driver.getCurrentUrl().contains("signup.php"),
                "Duplicate signup passed unexpectedly");
    }

    @Test
    public void sessionPersistenceAfterRefresh() throws InterruptedException {
        login();

        // Refresh the page
        driver.navigate().refresh();
        Thread.sleep(2000);

        // Check if user is still on the dashboard
        Assertions.assertTrue(driver.getCurrentUrl().contains("index.php"), "Session did not persist after refresh");
    }

}