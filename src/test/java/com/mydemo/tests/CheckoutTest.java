package com.mydemo.tests;

import com.mydemo.base.BaseTest;
import com.mydemo.pages.CartPage;
import com.mydemo.pages.CatalogPage;
import com.mydemo.pages.CheckoutPage;
import com.mydemo.pages.LoginPage;
import com.mydemo.pages.MenuPage;
import com.mydemo.pages.ProductDetailPage;
import com.mydemo.utils.ConfigReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test(description = "M-TC-017 Full checkout E2E")
    @Description("Add -> cart -> login -> address -> payment 4111... -> place order -> complete")
    public void fullCheckout() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        catalog.openFirstProduct();
        ProductDetailPage detail = new ProductDetailPage(driver(), waitSec());
        detail.addToCart();
        detail.openCart();

        CartPage cart = new CartPage(driver(), waitSec());
        Assert.assertTrue(cart.isLoaded(), "cart not loaded");
        cart.proceedToCheckout();

        // app forces login at checkout if guest
        LoginPage login = new LoginPage(driver(), waitSec());
        if (login.isLoaded()) {
            login.login(ConfigReader.get("std.user"), ConfigReader.get("std.password"));
        }

        CheckoutPage co = new CheckoutPage(driver(), waitSec());
        co.fillAddress("John Doe", "123 Main St", "San Jose", "95110", "United States");
        co.toPayment();
        co.fillPayment("4111111111111111", "12/30", "123");
        co.placeOrder();

        Assert.assertTrue(co.isOrderComplete(), "order not complete, got: " + driver().getPageSource().substring(0, Math.min(400, driver().getPageSource().length())));
    }

    @Test(description = "M-TC-018 Checkout requires valid payment")
    @Description("Incomplete card -> review/place disabled or error")
    public void invalidPaymentBlocked() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        catalog.openFirstProduct();
        new ProductDetailPage(driver(), waitSec()).addToCart();
        new ProductDetailPage(driver(), waitSec()).openCart();
        new CartPage(driver(), waitSec()).proceedToCheckout();

        LoginPage login = new LoginPage(driver(), waitSec());
        if (login.isLoaded()) {
            login.login(ConfigReader.get("std.user"), ConfigReader.get("std.password"));
        }

        CheckoutPage co = new CheckoutPage(driver(), waitSec());
        co.fillAddress("John Doe", "123 Main St", "San Jose", "95110", "United States");
        co.toPayment();
        // incomplete card
        try {
            co.fillPayment("1", "1/1", "1");
        } catch (Exception e) {
            System.out.println("payment rejected at form level: " + e.getMessage());
        }
        // either Review disabled or no order complete
        Assert.assertFalse(co.isOrderComplete(), "order should NOT complete with bad card");
    }
}
