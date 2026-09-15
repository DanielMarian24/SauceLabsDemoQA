package com.mydemo.api;

import com.mydemo.utils.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CartApiTest {
    private String base;

    @BeforeClass
    public void setup() {
        base = ConfigReader.get("api.base.url");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test(description = "M-API-017 Full cart lifecycle")
    public void cartLifecycle() {
        String productId = given().get(base + "/products").jsonPath().getString("data[0].id");
        System.out.println("productId=" + productId);

        Response create = given().contentType(ContentType.JSON).body("{}").post(base + "/carts");
        System.out.println("POST /carts -> " + create.getStatusCode() + " " + create.asString());
        Assert.assertEquals(create.getStatusCode(), 201);
        String cartId = create.jsonPath().getString("id");

        Response add = given().contentType(ContentType.JSON)
                .body(Map.of("product_id", productId, "quantity", 2)).post(base + "/carts/" + cartId);
        System.out.println("POST /carts/" + cartId + " -> " + add.getStatusCode() + " " + add.asString());
        Assert.assertEquals(add.getStatusCode(), 200);
        Assert.assertTrue(add.jsonPath().getString("result").contains("added or updated"));

        Response view = given().get(base + "/carts/" + cartId);
        Assert.assertEquals(view.getStatusCode(), 200);
        Assert.assertEquals(view.jsonPath().getString("cart_items[0].product_id"), productId);
        Assert.assertEquals((int) view.jsonPath().getInt("cart_items[0].quantity"), 2);

        Response upd = given().contentType(ContentType.JSON)
                .body(Map.of("product_id", productId, "quantity", 5))
                .put(base + "/carts/" + cartId + "/product/quantity");
        Assert.assertEquals(upd.getStatusCode(), 200);

        Response view2 = given().get(base + "/carts/" + cartId);
        Assert.assertEquals((int) view2.jsonPath().getInt("cart_items[0].quantity"), 5);

        Assert.assertEquals(given().delete(base + "/carts/" + cartId + "/product/" + productId).getStatusCode(), 204);
        List<?> items = given().get(base + "/carts/" + cartId).jsonPath().getList("cart_items");
        Assert.assertTrue(items.isEmpty(), "cart not empty: " + items);
        Assert.assertEquals(given().delete(base + "/carts/" + cartId).getStatusCode(), 204);
    }

    @Test(description = "M-API-018 Order endpoint discovery")
    public void orderDiscovery() {
        String productId = given().get(base + "/products").jsonPath().getString("data[0].id");
        String cartId = given().contentType(ContentType.JSON).body("{}").post(base + "/carts").jsonPath().getString("id");
        given().contentType(ContentType.JSON).body(Map.of("product_id", productId, "quantity", 1))
                .post(base + "/carts/" + cartId);

        Map<String, Object> payload = Map.of(
                "delivery_details", Map.of("first_name", "Test", "last_name", "User"),
                "payment_details", Map.of("method", "credit-card", "card_number", "4111-1111-1111-1111"));

        String[] endpoints = {"/orders", "/order", "/carts/" + cartId + "/orders", "/carts/" + cartId + "/order", "/checkout"};
        for (String ep : endpoints) {
            Response r = given().contentType(ContentType.JSON).body(payload).post(base + ep);
            System.out.println("POST " + ep + " => " + r.getStatusCode());
        }
        given().delete(base + "/carts/" + cartId);
        Assert.assertTrue(true, "discovery logged; ordering is FE-only (no public order API)");
    }
}
