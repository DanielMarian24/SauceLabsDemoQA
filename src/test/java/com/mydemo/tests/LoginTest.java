package com.mydemo.tests;

import com.mydemo.base.BaseTest;
import com.mydemo.pages.CatalogPage;
import com.mydemo.pages.LoginPage;
import com.mydemo.pages.MenuPage;
import com.mydemo.utils.ConfigReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private void gotoLogin() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        MenuPage menu = new MenuPage(driver(), waitSec());
        catalog.openMenu();
        menu.openLogin();
    }

    @Test(description = "M-TC-005 Login happy path")
    @Description("Login with bob@example.com / 10203040, verify logout appears")
    public void loginHappyPath() {
        gotoLogin();
        LoginPage login = new LoginPage(driver(), waitSec());
        Assert.assertTrue(login.isLoaded(), "login screen not shown");
        login.login(ConfigReader.get("std.user"), ConfigReader.get("std.password"));

        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        Assert.assertTrue(catalog.isLoaded(), "did not land on catalog after login");
        catalog.openMenu();
        MenuPage menu = new MenuPage(driver(), waitSec());
        Assert.assertTrue(menu.isLogoutVisible(), "logout not visible -> login failed");
    }

    @Test(description = "M-TC-006 Locked user shows error")
    @Description("alice@example.com is locked, expect 'locked out' error")
    public void lockedUserError() {
        gotoLogin();
        LoginPage login = new LoginPage(driver(), waitSec());
        login.login(ConfigReader.get("locked.user"), ConfigReader.get("locked.password"));
        Assert.assertTrue(login.isErrorVisible(), "expected error for locked user");
        Assert.assertTrue(login.errorText().toLowerCase().contains("locked"),
                "unexpected error: " + login.errorText());
    }

    @Test(description = "M-TC-007 Non-locked user accepted")
    @Description("Non-existent but non-empty user is accepted (no credential validation except locked users)")
    public void invalidCredentialsError() {
        gotoLogin();
        LoginPage login = new LoginPage(driver(), waitSec());
        login.login("noone@example.com", "wrongpass");

        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        Assert.assertTrue(catalog.isLoaded(),
                "My Demo App accepts any non-empty credentials (only locked users are rejected)");
    }

    @Test(description = "M-TC-008 Empty fields stay on login")
    @Description("Empty login -> app stays on login screen")
    public void emptyFieldsError() {
        gotoLogin();
        LoginPage login = new LoginPage(driver(), waitSec());
        login.login("", "");

        Assert.assertTrue(login.isLoaded(), "empty creds should keep user on login screen");
    }
}
