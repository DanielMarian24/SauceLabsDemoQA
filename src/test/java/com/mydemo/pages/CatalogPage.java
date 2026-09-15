package com.mydemo.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class CatalogPage extends BasePage {
    private final By productsTitle = idd("productTV");
    private final By sortButton = aid("Shows current sorting order and displays available sorting options");
    private final By cartIcon = aid("View cart");
    private final By cartBadge = idd("cartTV");
    private final By openMenu = aid("View menu");
    private final By productList = idd("productRV");

    public CatalogPage(AndroidDriver driver, int timeoutSec) {
        super(driver, timeoutSec);
    }

    public boolean isLoaded() {
        try {
            return isVisible(productsTitle, 10) && "Products".equals(text(productsTitle));
        } catch (Exception e) {
            return false;
        }
    }

    public void openMenu() {
        click(openMenu);
    }

    public void openSort() {
        click(sortButton);
    }

    public void selectSort(String key) {
        // NameAsc | NameDesc | PriceAsc | PriceDesc
        String rid = switch (key) {
            case "NameDesc" -> "nameDesCL";
            case "PriceAsc" -> "priceAscCL";
            case "PriceDesc" -> "priceDesCL";
            default -> "nameAscCL";
        };
        click(idd(rid));
    }

    public int productCount() {
        return countInList("productRV", "titleTV");
    }

    public String firstProductName() {
        return rowTextInList("productRV", "titleTV", 0);
    }

    public void openFirstProduct() {
        clickRowInList("productRV", "productIV", 0);
    }

    public void openProductByIndex(int index) {
        clickRowInList("productRV", "productIV", index);
    }

    public String cartBadgeCount() {
        if (!isVisible(cartBadge, 3)) {
            return "0";
        }
        return text(cartBadge);
    }

    public void openCart() {
        click(cartIcon);
    }
}