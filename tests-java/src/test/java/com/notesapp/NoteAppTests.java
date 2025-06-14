package com.notesapp;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteAppTests {
    static WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup(); // Automatically matches Chrome version
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    @Order(1)
    void signupUser() {
        driver.get("http://localhost/signup.php");
        driver.findElement(By.name("name")).sendKeys("Test User");
        driver.findElement(By.name("email")).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("login.php"));
    }

    @Test
    @Order(2)
    void loginUser() {
        driver.get("http://localhost/login.php");
        driver.findElement(By.name("email")).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("index.php"));
    }

    @Test
    @Order(3)
    void addNote() {
        loginUser(); // ensure logged in
        driver.findElement(By.name("title")).sendKeys("Test Note");
        driver.findElement(By.name("description")).sendKeys("Test Description");
        driver.findElement(By.cssSelector(".btn-primary")).click();
        List<WebElement> notes = driver.findElements(By.className("note-card"));
        Assertions.assertTrue(notes.stream().anyMatch(n -> n.getText().contains("Test Note")));
    }

    @Test
    @Order(4)
    void editNote() {
        WebElement editBtn = driver.findElement(By.linkText("Edit"));
        editBtn.click();
        WebElement title = driver.findElement(By.name("title"));
        title.clear();
        title.sendKeys("Updated Note");
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Updated Note"));
    }

    @Test
    @Order(5)
    void deleteNote() {
        WebElement deleteBtn = driver.findElement(By.linkText("Delete"));
        deleteBtn.click();
        Assertions.assertFalse(driver.getPageSource().contains("Updated Note"));
    }

    @Test
    @Order(6)
    void logoutUser() {
        driver.findElement(By.linkText("Logout")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("login.php"));
    }

    @Test
    @Order(7)
    void loginInvalidCredentials() {
        driver.get("http://localhost/login.php");
        driver.findElement(By.name("email")).sendKeys("wrong@example.com");
        driver.findElement(By.name("password")).sendKeys("wrongpass");
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Invalid credentials"));
    }

    @Test
    @Order(8)
    void signupDuplicateEmail() {
        driver.get("http://localhost/signup.php");
        driver.findElement(By.name("name")).sendKeys("Test User");
        driver.findElement(By.name("email")).sendKeys("testuser@example.com"); // already used
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Signup failed")); // or whatever your app shows
    }

    @Test
    @Order(9)
    void checkNoteTimestamp() {
        loginUser();
        driver.findElement(By.name("title")).sendKeys("Time Note");
        driver.findElement(By.name("description")).sendKeys("Time test");
        driver.findElement(By.cssSelector(".btn-primary")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Time Note"));
        Assertions.assertTrue(driver.getPageSource().contains("202")); // e.g. 2025
    }

    @Test
    @Order(10)
    void testUIElementsPresent() {
        loginUser();
        Assertions.assertTrue(driver.findElement(By.name("title")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.name("description")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.cssSelector(".btn-primary")).isDisplayed());
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }
}
