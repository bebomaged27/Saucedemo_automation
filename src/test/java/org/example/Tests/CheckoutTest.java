package org.example.Tests;

import org.example.Pages.CheckoutPage;
import org.example.Pages.LoginPage;
import org.example.Pages.ProductsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTest {
    private WebDriver driver;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setup() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        checkoutPage = new CheckoutPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);

        // Perform login
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();

        // Add a product to the cart and navigate to the checkout page
        productsPage.clickAddToCartForProduct(0); // Adding first product to cart
        Thread.sleep(2000); // Wait for the cart update
        productsPage.clickCartIcon(); // Go to the cart page
        driver.findElement(By.id("checkout")).click(); // Click checkout button
    }

    @DataProvider(name = "checkoutDataProvider")
    public Object[][] checkoutDataProvider() {
        return new Object[][]{
                {"John", "Doe", "12345", true},   // Valid input
                {"", "Doe", "12345", false},      // Missing first name
                {"John", "", "12345", false},     // Missing last name
                {"John", "Doe", "", false},       // Missing postal code
                {"John", "Doe", "1234", false},   // Postal code too short
                {"John", "Doe", "ABCDE", false},  // Postal code non-numeric
                {"John", "Doe", "123456", false}, // Postal code too long
                {"John!", "Doe", "12345", false}, // Invalid first name
                {"John", "Doe!", "12345", false}, // Invalid last name
                {"J", "Doe", "12345", false},     // First name too short
                {"John", "D", "12345", false},    // Last name too short
                {"", "", "", false},              // All fields empty
                {"John", "Doe", "12", false}      // Postal code too short
        };
    }

    @Test(dataProvider = "checkoutDataProvider")
    public void testCheckout(String firstName, String lastName, String postalCode, boolean shouldPass) {
        checkoutPage.enterFirstName(firstName);
        checkoutPage.enterLastName(lastName);
        checkoutPage.enterPostalCode(postalCode);
        checkoutPage.clickContinue();

        if (shouldPass) {
            // Check if the user proceeds to the next step (e.g., overview or confirmation page)
            String expectedUrl = "https://www.saucedemo.com/checkout-step-two.html"; // Adjust URL as per the next step
            Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "The user did not proceed to the next step.");
        } else {
            // Assert that an error message is shown if the input is invalid
            String expectedErrorMessage = getExpectedErrorMessage(firstName, lastName, postalCode);
            Assert.assertEquals(checkoutPage.getErrorMessage(), expectedErrorMessage, "Error message did not match the expected outcome.");
        }

        // Refresh the page to reset fields for the next iteration
        driver.navigate().refresh();
    }

    // Helper method to determine the expected error message based on the input
    private String getExpectedErrorMessage(String firstName, String lastName, String postalCode) {
        if (firstName.isEmpty()) {
            return "Error: First Name is required";
        } else if (lastName.isEmpty()) {
            return "Error: Last Name is required";
        } else if (postalCode.isEmpty()) {
            return "Error: Postal Code is required";
        } else if (postalCode.length() < 5) {
            return "Error: Postal Code is too short";
        } else if (postalCode.length() > 5) {
            return "Error: Postal Code is too long";
        } else if (!postalCode.matches("\\d+")) {
            return "Error: Postal Code must be numeric";
        } else if (!firstName.matches("[a-zA-Z]+")) {
            return "Error: First Name can only contain alphabetical characters";
        } else if (!lastName.matches("[a-zA-Z]+")) {
            return "Error: Last Name can only contain alphabetical characters";
        } else {
            return ""; // No error expected
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
