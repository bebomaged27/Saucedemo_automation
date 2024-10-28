package org.example.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ProductsPage {
    private WebDriver driver;

    // Locators
    private By productsList = By.className("inventory_item");
    private By productName = By.className("inventory_item_name");
    private By productPrice = By.className("inventory_item_price");
    private By addToCartButton = By.className("btn_inventory");
    private By cartIcon = By.id("shopping_cart_container");
    private By cartItemCount = By.className("shopping_cart_badge");
    private By cartRemoveButton = By.id("remove");
    private By sortByDropdown = By.className("product_sort_container");
    private By productDetailsLink = By.className("inventory_item_name");
    private By backButton = By.id("back-to-products");
    private By checkoutButton = By.className("checkout_button");
    private By quantityField = By.className("cart_quantity");
    private By productImage = By.className("inventory_item_img");
    private By cartItem = By.className("cart_item");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public int getProductsCount() {
        return driver.findElements(productsList).size();
    }

    public void clickAddToCartForProduct(int productIndex) {
        driver.findElements(addToCartButton).get(productIndex).click();
    }

    public void clickRemoveFromCartForProduct(int productIndex) {
        driver.findElements(cartRemoveButton).get(productIndex).click();
    }

    public void clickCartIcon() {
        driver.findElement(cartIcon).click();
    }

    public void selectSortingOption(String option) {
        Select sortDropdown = new Select(driver.findElement(sortByDropdown));
        sortDropdown.selectByVisibleText(option);
    }

    public boolean isSortedByNameAToZ() {
        List<String> names = driver.findElements(productName).stream()
                .map(WebElement::getText).collect(Collectors.toList());
        List<String> sortedNames = names.stream().sorted().collect(Collectors.toList());
        return names.equals(sortedNames);
    }

    public boolean isSortedByNameZToA() {
        List<String> names = driver.findElements(productName).stream()
                .map(WebElement::getText).collect(Collectors.toList());
        List<String> sortedNames = names.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
        return names.equals(sortedNames);
    }

    public boolean isSortedByPriceLowToHigh() {
        List<Double> prices = driver.findElements(productPrice).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
        List<Double> sortedPrices = prices.stream().sorted().collect(Collectors.toList());
        return prices.equals(sortedPrices);
    }

    public boolean isSortedByPriceHighToLow() {
        List<Double> prices = driver.findElements(productPrice).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
        List<Double> sortedPrices = prices.stream().sorted((a, b) -> Double.compare(b, a)).collect(Collectors.toList());
        return prices.equals(sortedPrices);
    }

    public boolean isProductInCart(int productIndex) {
        WebElement product = driver.findElements(addToCartButton).get(productIndex);
        return product.getText().equalsIgnoreCase("Remove");
    }

    public int getCartItemCount() {
        String countText = driver.findElement(cartItemCount).getText();
        return countText.isEmpty() ? 0 : Integer.parseInt(countText);
    }

    public void clickProductDetails(int productIndex) {
        driver.findElements(productDetailsLink).get(productIndex).click();
    }

    public boolean isProductDetailsDisplayed(int productIndex) {
        List<WebElement> products = driver.findElements(productName); // Get the list of products

        if (products.size() == 0) {
            System.out.println("No products found on the page.");
            return false; // Return false if no products are found
        }

        if (productIndex >= products.size()) {
            System.out.println("Invalid product index: " + productIndex);
            return false; // Handle invalid product index
        }

        String productNameText = products.get(productIndex).getText(); // Get product name text
        String productDetailsText = driver.findElement(productDetailsLink).getText(); // Get product details link text

        return productDetailsText.equals(productNameText); // Compare product details with product name
    }


    public boolean isOnCartPage() {
        return driver.getCurrentUrl().contains("cart");
    }

    public boolean isOnProductsPage() {
        return driver.getCurrentUrl().contains("inventory");
    }

    public void clickContinueShoppingButton() {
        driver.findElement(By.id("continue-shopping")).click();
    }

    public boolean isInventoryPriceAccurate() {
        // Placeholder logic
        return true;
    }

    public boolean isResponsive() {
        // Placeholder logic
        return true;
    }

    public void clickBackButton() {
        driver.findElement(backButton).click();
    }

    public boolean isProductDescriptionAccurate() {
        // Placeholder logic
        return true;
    }

    public void clickAddToCartOnDetailsPage() {
        driver.findElement(addToCartButton).click();
    }

    public void removeProductFromCartOnDetailsPage() {
        driver.findElement(cartRemoveButton).click();
    }

    public void updateProductQuantity(int productIndex, int quantity) {
        WebElement quantityField = driver.findElements(this.quantityField).get(productIndex);
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
    }

    public double getProductPrice(int productIndex) {
        String priceText = driver.findElements(productPrice).get(productIndex).getText().replace("$", "");
        return Double.parseDouble(priceText);
    }

    public double getCartTotal() {
        // Add logic to retrieve and sum the total cart value
        return 0.0;
    }

    public void clickCheckoutButton() {
        driver.findElement(checkoutButton).click();
    }

    public boolean isOnCheckoutPage() {
        return driver.getCurrentUrl().contains("checkout");
    }

    public void clickProductImage(int productIndex) {
        driver.findElements(productImage).get(productIndex).click();
    }

    // New Method: removeProductFromCart
    public void removeProductFromCart(int productIndex) {
        WebElement product = driver.findElements(cartItem).get(productIndex);
        product.findElement(cartRemoveButton).click();
    }
    public void removeProduct() {
        driver.findElement(By.id("remove")).click();
    }


    public void selectSortingOptionByValue(String value) {
        Select dropdown = new Select(driver.findElement(By.className("product_sort_container")));
        dropdown.selectByValue(value); // Using value instead of text
    }


    // New Method: getCartBadgeQuantity
    // New Method: getCartBadgeQuantity with Explicit Wait
    public int getCartBadgeQuantity() {
        try {
            // Locate the cart badge with dynamic quantity
            WebElement cartBadge = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]/a[1]/span[1]"));
            // Get the quantity as an integer
            return Integer.parseInt(cartBadge.getText());
        } catch (NoSuchElementException e) {
            // Return 0 if the badge is not found or no items are in the cart
            return 0;
        }
    }


}
