package testcases;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.nio.file.Paths;

public class LoginTest extends BaseTest {
    private String username = "standard_user";
    private String password = "secret_sauce";

    @Test(priority = 1, description = "Validate Login Function to Swag-Labs Wen App")
    public void testLogin() {
        test.log(Status.INFO, "Starting login test");

        // Use the Playwright page object to interact with elements
        new LoginPage(page).fillUserName(username).fillPassword(password).clickLoginButton();

        // Take a screenshot after login
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/login_with_video.png")).setFullPage(true));


        // Assertion 1: Check if URL is correct after login
        String expectedUrl = "https://www.saucedemo.com/v1/inventory.html";
        page.waitForURL(expectedUrl);  // Playwright waits until URL matches
        Assert.assertEquals(page.url(), expectedUrl, "The URL after login is incorrect.");

        //Assertion 2: Check if a specific element (e.g., a logout button) is visible after login
        Locator shopping_cart = page.locator("#shopping_cart_container");
        Assert.assertTrue(shopping_cart.isVisible(), "shopping_cart button should be visible after login.");

        // Assertion 3: Check if the page title is correct after login
        String expectedTitle = "Swag Labs";
        String actualTitle = page.title();  // Fetches the page title
        Assert.assertEquals(actualTitle, expectedTitle, "The page title after login is incorrect.");

        // Log success
        test.log(Status.PASS, "Login test completed successfully");
    }
}

