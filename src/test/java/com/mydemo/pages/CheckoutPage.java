package com.mydemo.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class CheckoutPage extends BasePage {
    private String cardHolderName = "John Doe";
    // Shipping / address screen
    private final By fullNameET = idd("fullNameET");
    private final By address1ET = idd("address1ET");
    private final By address2ET = idd("address2ET");
    private final By cityET = idd("cityET");
    private final By zipET = idd("zipET");
    private final By stateET = idd("stateET");
    private final By countryET = idd("countryET");
    private final By toPaymentBtn = buttonWithText("To Payment");

    // Payment screen
    private final By cardNameET = idd("nameET");
    private final By cardNumberET = idd("cardNumberET");
    private final By expirationDateET = idd("expirationDateET");
    private final By securityCodeET = idd("securityCodeET");
    private final By reviewBtn = buttonWithText("Review Order");

    // Review / complete
    private final By placeOrderBtn = buttonWithText("Place Order");
    private final By completeHeader = idd("completeTV");

    public CheckoutPage(AndroidDriver driver, int timeoutSec) {
        super(driver, timeoutSec);
    }

    public boolean isAddressScreenLoaded() {
        return isVisible(buttonWithText("To Payment"), 10);
    }

    public void fillAddress(String name, String addr, String cityV, String zipV, String countryV) {
        cardHolderName = name;
        type(fullNameET, name);
        type(address1ET, addr);
        type(cityET, cityV);
        type(zipET, zipV);
        type(countryET, countryV);
        hideKeyboard();
    }

    public void toPayment() {
        click(toPaymentBtn);
    }

    public boolean isPaymentScreenLoaded() {
        return isVisible(buttonWithText("Review Order"), 10);
    }

    public void fillPayment(String card, String exp, String cvv) {
        fillPayment(cardHolderName, card, exp, cvv);
    }

    public void fillPayment(String name, String card, String exp, String cvv) {
        type(cardNameET, name);
        type(cardNumberET, card);
        type(expirationDateET, exp);
        type(securityCodeET, cvv);
        hideKeyboard();
        click(reviewBtn);
    }

    public void placeOrder() {
        click(placeOrderBtn);
    }

    public boolean isOrderComplete() {
        return isVisible(completeHeader, 10);
    }

    public String completeText() {
        return text(completeHeader);
    }

    public boolean isReviewOrderEnabled() {
        try {
            return visible(reviewBtn).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    private By buttonWithText(String buttonText) {
        return AppiumBy.xpath("//*[@resource-id='" + RID + "paymentBtn' and @text='" + buttonText + "']");
    }
}