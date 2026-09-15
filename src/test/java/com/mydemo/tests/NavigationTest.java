package com.mydemo.tests;

import com.mydemo.base.BaseTest;
import com.mydemo.pages.CatalogPage;
import com.mydemo.pages.MenuPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

    @Test(description = "M-TC-001 Catalog loads by default")
    @Description("App launches on catalog; products screen visible with items")
    public void catalogLoadsByDefault() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        Assert.assertTrue(catalog.isLoaded(), "products screen not visible");
        Assert.assertTrue(catalog.productCount() > 0, "no store items found");
    }

    @Test(description = "M-TC-002 Open side menu entries")
    @Description("Open menu and verify WebView / QR / Geo / Drawing / About entries exist")
    public void sideMenuEntries() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        MenuPage menu = new MenuPage(driver(), waitSec());
        catalog.openMenu();
        Assert.assertTrue(menu.isMenuEntryVisible("WebView"), "webview entry missing");
        Assert.assertTrue(menu.isMenuEntryVisible("QR Code Scanner"), "qr entry missing");
        Assert.assertTrue(menu.isMenuEntryVisible("Geo Location"), "geo entry missing");
        Assert.assertTrue(menu.isMenuEntryVisible("Drawing"), "drawing entry missing");
        Assert.assertTrue(menu.isMenuEntryVisible("About"), "about entry missing");
    }

    @Test(description = "M-TC-003 Navigate to About via menu")
    @Description("Menu -> About, verify About screen")
    public void navigateToAbout() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        MenuPage menu = new MenuPage(driver(), waitSec());
        catalog.openMenu();
        menu.openAbout();
        Assert.assertTrue(menu.isMenuEntryVisible("about screen") || driver().getPageSource().contains("About"),
                "About screen not shown. Source: " + driver().getPageSource().substring(0, Math.min(500, driver().getPageSource().length())));
    }

    @Test(description = "M-TC-004 Back to catalog from menu")
    @Description("Menu -> Catalog returns to products screen")
    public void backToCatalog() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        MenuPage menu = new MenuPage(driver(), waitSec());
        catalog.openMenu();
        menu.openCatalog();
        Assert.assertTrue(catalog.isLoaded(), "did not return to catalog");
    }
}
