package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.SelectOption;

public class LoginPage {

    private Page page;

    // Locators
    private Locator username;
    private Locator password;
    private Locator loginButton;

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
        // Initialize locators
        this.username = page.locator("#user-name");
        this.password = page.locator("#password");
        this.loginButton = page.locator("#login-button");
    }

    // Actions
    public LoginPage fillUserName(String username){
        this.username.fill(username);
        return this;
    }
    public LoginPage fillPassword(String password){
        this.password.fill(password);
        return this;
    }
    public LoginPage clickLoginButton(){
        loginButton.click();
        return this;
    }

}
