package org.example.Pages;// CheckoutPage.java
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
    private WebDriver driver;

    // Locators
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By postalCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By errorMessage = By.cssSelector("h3[data-test='error']");

    // Constructor
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to enter first name
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    // Method to enter last name
    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    // Method to enter postal code
    public void enterPostalCode(String postalCode) {
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    // Method to click continue
    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    // Method to get the error message
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    // Method to check if the checkout is successful (you can modify this based on your success criteria)
    public boolean isCheckoutSuccessful() {
        // Implement a method to check if the checkout was successful, for example, by checking the next page elements
        return driver.getCurrentUrl().contains("checkout-complete");
    }
}
