package com.mydemo.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ProductDetailPage extends BasePage {
    private final By productTitle = idd("productTV");
    private final By productPrice = idd("priceTV");
    private final By addToCartBtn = idd("cartBt");
    private final By increaseQtyBtn = aid("Increase item quantity");
    private final By decreaseQtyBtn = aid("Decrease item quantity");
    private final By quantityPicker = idd("quantityTV");
    private final By cartIcon = aid("View cart");

    public ProductDetailPage(AndroidDriver driver, int timeoutSec) {
        super(driver, timeoutSec);
    }

    public boolean isLoaded() {
        return isVisible(addToCartBtn, 10);
    }

    public String name() {
        return text(productTitle);
    }

    public String price() {
        return text(productPrice);
    }

    public void addToCart() {
        click(addToCartBtn);
    }

    public void increaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(increaseQtyBtn);
        }
    }

    public void decreaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(decreaseQtyBtn);
        }
    }

    public int getQuantity() {
        return Integer.parseInt(text(quantityPicker));
    }

    public void openCart() {
        click(cartIcon);
    }
}