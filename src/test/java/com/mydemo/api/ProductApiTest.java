package com.mydemo.api;

import com.mydemo.utils.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProductApiTest {
    private String base;

    @BeforeClass
    public void setup() {
        base = ConfigReader.get("api.base.url");
        RestAssured.filters(new AllureRestAssured());
    }

    private Response logGet(String path) {
        long t0 = System.currentTimeMillis();
        System.out.println("-> GET " + base + path);
        Response r = given().get(base + path);
        System.out.println("<- " + r.getStatusCode() + " GET " + path + " (" + (System.currentTimeMillis() - t0) + "ms)");
        return r;
    }

    @Test(description = "M-API-010 Product list paginated 200")
    public void productList() {
        Response r = logGet("/products");
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertTrue(r.jsonPath().getList("data").size() > 0);
        Assert.assertNotNull(r.jsonPath().get("total"));
        Assert.assertNotNull(r.jsonPath().get("current_page"));
        Assert.assertNotNull(r.jsonPath().getString("data[0].id"));
        Assert.assertNotNull(r.jsonPath().getString("data[0].name"));
    }

    @Test(description = "M-API-011 Product structure types")
    public void productStructure() {
        Response r = logGet("/products");
        Assert.assertEquals(r.getStatusCode(), 200);
        Object price = r.jsonPath().get("data[0].price");
        Assert.assertTrue(price instanceof Number, "price not a number: " + price);
        Assert.assertNotNull(r.jsonPath().get("data[0].category"));
        Assert.assertNotNull(r.jsonPath().get("data[0].brand"));
    }

    @Test(description = "M-API-012 Product by ID 200")
    public void productById() {
        String id = logGet("/products").jsonPath().getString("data[0].id");
        Response r = logGet("/products/" + id);
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertEquals(r.jsonPath().getString("id"), id);
        Assert.assertNotNull(r.jsonPath().getString("name"));
        Assert.assertNotNull(r.jsonPath().getString("description"));
    }

    @Test(description = "M-API-013 Product 404")
    public void product404() {
        Assert.assertEquals(logGet("/products/nonexistent123").getStatusCode(), 404);
    }

    @Test(description = "M-API-014 Search Plier")
    public void searchPlier() {
        Response r = logGet("/products?name=Plier");
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertTrue(r.jsonPath().getList("data").size() > 0);
        Assert.assertTrue(r.jsonPath().getString("data[0].name").toLowerCase().contains("plier"));
    }

    @Test(description = "M-API-015 Search non-matching still 200")
    public void searchNoMatch() {
        Response r = logGet("/products?name=zzzzNonExistentProduct");
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertNotNull(r.jsonPath().getList("data"));
    }

    @Test(description = "M-API-016 Search multiple terms")
    public void searchMultiple() {
        for (String term : new String[]{"Hammer", "Plier", "Saw"}) {
            Assert.assertEquals(logGet("/products?name=" + term).getStatusCode(), 200);
        }
    }
}
