package org.example.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Cookie;

public class BaseTest {
    protected WebDriver driver;

    public void setUp() {
        // Set up the ChromeDriver and initialize it
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        // Load the products page
        driver.get("https://www.saucedemo.com/inventory.html");

        // Add saved cookies (replace with actual cookie values obtained after login)
        driver.manage().addCookie(new Cookie("session-username", "standard_user"));
        driver.manage().addCookie(new Cookie("session-id", "your-session-id"));

        // Refresh to apply cookies
        driver.navigate().refresh();
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
