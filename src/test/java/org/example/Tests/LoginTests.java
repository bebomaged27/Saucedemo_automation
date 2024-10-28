package org.example.Tests;

import org.example.Pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTests {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    // DataProvider for login tests
    @DataProvider(name = "loginDataProvider")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"standard_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"}, // Valid login
                {"invalid_user", "secret_sauce", "Epic sadface: Username and password do not match any user in this service"}, // Invalid username
                {"standard_user", "invalid_password", "Epic sadface: Username and password do not match any user in this service"}, // Invalid password
                {"", "", "Epic sadface: Username is required"}, // Blank username and password
                {"STANDARD_USER", "secret_sauce", "Epic sadface: Username and password do not match any user in this service"}, // Uppercase username
                {"standard_user", "SECRET_SAUCE", "Epic sadface: Username and password do not match any user in this service"}, // Uppercase password
                {"standard_user", "", "Epic sadface: Password is required"}, // Blank password
                {"", "secret_sauce", "Epic sadface: Username is required"}, // Blank username
                {" standard_user ", "secret_sauce", "https://www.saucedemo.com/inventory.html"}, // Username with spaces
                {"standard_user", " secret_sauce ", "Epic sadface: Username and password do not match any user in this service"}, // Password with spaces
                {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."}, // Locked-out user
                {"problem_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"}, // Problematic user
                {"performance_glitch_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"} // Performance glitch user
        };
    }

    @Test(dataProvider = "loginDataProvider")
    public void testLogin(String username, String password, String expectedOutcome) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        // Check the outcome based on expected results
        if (expectedOutcome.equals("https://www.saucedemo.com/inventory.html")) {
            Assert.assertEquals(driver.getCurrentUrl(), expectedOutcome);
        } else {
            Assert.assertTrue(loginPage.getErrorMessage().contains(expectedOutcome));
        }
    }
    @Test
    public void testPasswordFieldMasking() {
        Assert.assertTrue(loginPage.isPasswordMasked(), "Password is not masked");
    }

}
