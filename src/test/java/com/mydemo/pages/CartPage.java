package com.mydemo.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class CartPage extends BasePage {
    private final By cartTitle = idd("productTV");
    private final By productList = idd("productRV");
    private final By totalPrice = idd("totalPriceTV");
    private final By removeBtn = idd("removeBt");
    private final By increaseQtyBtn = aid("Increase item quantity");
    private final By decreaseQtyBtn = aid("Decrease item quantity");
    private final By checkoutBtn = idd("cartBt");
    private final By emptyTitle = idd("noItemTitleTV");

    public CartPage(AndroidDriver driver, int timeoutSec) {
        super(driver, timeoutSec);
    }

    public boolean isLoaded() {
        try {
            return isVisible(productList, 10) && "My Cart".equals(text(cartTitle));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmpty() {
        return isVisible(emptyTitle, 3);
    }

    public int itemCount() {
        return countInList("productRV", "titleTV");
    }

    public String total() {
        return text(totalPrice);
    }

    public void increaseQuantity() {
        click(increaseQtyBtn);
    }

    public void decreaseQuantity() {
        click(decreaseQtyBtn);
    }

    public void removeFirstItem() {
        click(removeBtn);
    }

    public void proceedToCheckout() {
        click(checkoutBtn);
    }
}