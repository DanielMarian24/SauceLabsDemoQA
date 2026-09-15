package com.mydemo.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private final By loginTitle = idd("loginTV");
    private final By username = idd("nameET");
    private final By password = idd("passwordET");
    private final By loginBtn = idd("loginBtn");
    private final By errorMsg = idd("passwordErrorTV");
    private final By savedUser1 = idd("username1TV");  // bod@example.com
    private final By savedUser2 = idd("username2TV");  // alice@example.com (locked out)
    private final By savedUser3 = idd("username3TV");  // visual@example.com

    public LoginPage(AndroidDriver driver, int timeoutSec) {
        super(driver, timeoutSec);
    }

    public boolean isLoaded() {
        return isVisible(loginTitle, 10);
    }

    public void login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginBtn);
    }

    public void loginAsSavedUser(String userKey) {
        By row = switch (userKey) {
            case "user1" -> savedUser1;
            case "user2" -> savedUser2;
            case "user3" -> savedUser3;
            default -> savedUser1;
        };
        click(row);
        click(loginBtn);
    }

    public String errorText() {
        return isVisible(errorMsg, 3) ? text(errorMsg) : "";
    }

    public boolean isErrorVisible() {
        return isVisible(errorMsg, 5);
    }
}