package com.mydemo.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class MenuPage extends BasePage {
    private final By loginItem = aid("Login Menu Item");
    private final By logoutItem = aid("Logout Menu Item");
    private final By logoutConfirm = idd("button1");
    private final By catalogItem = byText("Catalog");
    private final By webViewItem = byText("WebView");
    private final By qrItem = byText("QR Code Scanner");
    private final By geoItem = byText("Geo Location");
    private final By drawingItem = byText("Drawing");
    private final By aboutItem = byText("About");

    public MenuPage(AndroidDriver driver, int timeoutSec) {
        super(driver, timeoutSec);
    }

    public void openLogin() {
        click(loginItem);
    }

    public void openCatalog() {
        click(catalogItem);
    }

    public void openWebView() {
        click(webViewItem);
    }

    public void openQr() {
        click(qrItem);
    }

    public void openGeo() {
        click(geoItem);
    }

    public void openDrawing() {
        click(drawingItem);
    }

    public void openAbout() {
        click(aboutItem);
    }

    public boolean isLogoutVisible() {
        return isVisible(logoutItem, 5);
    }

    public void logout() {
        click(logoutItem);
    }

    public void confirmLogout() {
        click(logoutConfirm);
    }

    public boolean isMenuEntryVisible(String menuEntryText) {
        return textVisible(menuEntryText, 5);
    }
}