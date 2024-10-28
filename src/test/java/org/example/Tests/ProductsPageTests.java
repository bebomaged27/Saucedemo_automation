package org.example.Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.Pages.BaseTest;
import org.example.Pages.LoginPage;
import org.example.Pages.ProductsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class ProductsPageTests extends BaseTest {
    private ProductsPage productsPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setupPage() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");

        // Initialize pages
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);

        // Perform login
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void testProductsAreDisplayed() {
        int productCount = productsPage.getProductsCount();
        Assert.assertTrue(productCount > 0, "Products are not displayed.");
    }

    @Test
    public void testSortingByNameAToZ() {
        productsPage.selectSortingOptionByValue("az");
        // Add validation to check products are sorted alphabetically A-Z
        Assert.assertTrue(productsPage.isSortedByNameAToZ(), "Products are not sorted A to Z.");
    }

    @Test
    public void testSortingByNameZToA() {
        productsPage.selectSortingOptionByValue("za");
        // Add validation to check products are sorted alphabetically Z-A
        Assert.assertTrue(productsPage.isSortedByNameZToA(), "Products are not sorted Z to A.");
    }

    @Test
    public void testSortingByPriceLowToHigh() {
        productsPage.selectSortingOptionByValue("lohi");
        // Add validation to check products are sorted by price Low to High
        Assert.assertTrue(productsPage.isSortedByPriceLowToHigh(), "Products are not sorted by price Low to High.");
    }

    @Test
    public void testSortingByPriceHighToLow() {
        productsPage.selectSortingOptionByValue("hilo");
        // Add validation to check products are sorted by price High to Low
        Assert.assertTrue(productsPage.isSortedByPriceHighToLow(), "Products are not sorted by price High to Low.");
    }

    @Test
    public void testAddToCartSingleProduct() {
        productsPage.clickAddToCartForProduct(0); // Adding the first product to the cart
        Assert.assertTrue(productsPage.isProductInCart(0), "Product is not added to the cart.");
    }

    @Test
    public void testAddToCartMultipleProducts() {
        productsPage.clickAddToCartForProduct(0);
        productsPage.clickAddToCartForProduct(1);
        Assert.assertTrue(productsPage.isProductInCart(0) && productsPage.isProductInCart(1),
                "Multiple products are not added to the cart.");
    }

    @Test
    public void testRemoveFromCart() throws InterruptedException {
        productsPage.clickAddToCartForProduct(0);
        Thread.sleep(2000);
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        //productsPage.removeProductFromCart(0);
        Thread.sleep(2000);
        Assert.assertFalse(productsPage.isProductInCart(0), "Product is not removed from the cart.");
    }

    @Test
    public void testViewProductDetails() {
        productsPage.clickProductDetails(0); // Viewing the first product details
        Assert.assertTrue(productsPage.isProductDetailsDisplayed(0), "Product details are not displayed.");
    }

    @Test
    public void testCartIconNavigatesToCartPage() {
        productsPage.clickCartIcon();
        Assert.assertTrue(productsPage.isOnCartPage(), "Cart page is not displayed.");
    }

    @Test
    public void testContinueShoppingButtonOnCartPage() throws InterruptedException {
        productsPage.clickCartIcon();
        productsPage.clickContinueShoppingButton();
        Thread.sleep(2000);
        Assert.assertTrue(productsPage.isOnProductsPage(), "Continue shopping button did not navigate to products page.");
    }

    @Test
    public void testInventoryPriceAccuracy() {
        Assert.assertTrue(productsPage.isInventoryPriceAccurate(), "Inventory price is not accurate.");
    }

    @Test
    public void testProductsPageResponsiveness() {
        Assert.assertTrue(productsPage.isResponsive(), "Products page is not responsive.");
    }

    @Test
    public void testBackButtonNavigationFromProductDetails() {
        productsPage.clickProductDetails(0);
        productsPage.clickBackButton();
        Assert.assertTrue(productsPage.isOnProductsPage(), "Back button did not navigate to products page.");
    }

    @Test
    public void testProductDescriptionAccuracy() {
        Assert.assertTrue(productsPage.isProductDescriptionAccurate(), "Product description is not accurate.");
    }

    @Test
    public void testAddingAndRemovingProductsOnDetailsPage() {
        productsPage.clickProductDetails(0);

        // Wait for the button to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart")));

        productsPage.clickAddToCartOnDetailsPage();
        Assert.assertTrue(productsPage.isProductInCart(0), "Product not added from details page.");

        // Ensure product is removed from cart
        productsPage.removeProductFromCartOnDetailsPage();
        Assert.assertFalse(productsPage.isProductInCart(0), "Product not removed from details page.");
    }


    @Test
    public void testCartIconUpdatesAfterRemovingItems() {
        // Add product to cart
        productsPage.clickAddToCartForProduct(0);

        // Validate that the product was added successfully
        Assert.assertTrue(productsPage.isProductInCart(0), "Product was not added to the cart.");

        // Explicit wait for the cart item count to update
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge"))); // Adjust the selector as needed

        // Remove the product from cart
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        // Wait for the cart item count to update after removal (use By.className instead of WebElement)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("shopping_cart_badge")));

        // Assert that the cart badge is not displayed anymore
        List<WebElement> cartBadges = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(cartBadges.isEmpty(), "Cart icon did not update after removing the item.");
    }




    @Test
    public void testTotalCartQuantity() {
        // Add products to the cart
        productsPage.clickAddToCartForProduct(0); // Add first product
        productsPage.clickAddToCartForProduct(1); // Add second product

        // Verify the cart badge displays the correct quantity
        Assert.assertEquals(productsPage.getCartBadgeQuantity(), 2, "Total cart quantity is incorrect.");
    }


    @Test
    public void testPriceCalculationForMultipleQuantities() {
        productsPage.clickAddToCartForProduct(0);
        productsPage.updateProductQuantity(0, 3);
        double expectedPrice = productsPage.getProductPrice(0) * 3;
        Assert.assertEquals(productsPage.getCartTotal(), expectedPrice, "Price calculation for multiple quantities is incorrect.");
    }

    @Test
    public void testSortingOptionsPersistAfterNavigation() {
        productsPage.selectSortingOptionByValue("lohi");
        productsPage.clickProductDetails(0);
        productsPage.clickBackButton();
        Assert.assertTrue(productsPage.isSortedByPriceLowToHigh(), "Sorting option did not persist after navigation.");
    }

    @Test
    public void testNavigationToCheckoutPage() {
        productsPage.clickCartIcon();
        productsPage.clickCheckoutButton();
        Assert.assertTrue(productsPage.isOnCheckoutPage(), "Did not navigate to checkout page.");
    }

    @Test
    public void testProductImagesAreClickable() throws InterruptedException {
        productsPage.clickProductImage(0);
        Thread.sleep(2000);
        Assert.assertFalse(productsPage.isProductDetailsDisplayed(0), "Product image did not navigate to product details page.");
    }
}

