package com.mydemo.api;

import com.mydemo.utils.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * BE track: PracticeSoftwareTesting API
 * Mirrors LevelUp web BE tests 1:1. Logs method, URL, body, status, time.
 */
public class AuthApiTest {
    private String base;
    private String email;
    private String password;

    @BeforeClass
    public void setup() {
        base = ConfigReader.get("api.base.url");
        email = ConfigReader.get("api.user.email");
        password = ConfigReader.get("api.user.password");
        RestAssured.filters(new AllureRestAssured());
    }

    private Response logAndPost(String path, Object body) {
        long t0 = System.currentTimeMillis();
        System.out.println("-> POST " + base + path);
        System.out.println("  Request Body: " + body);
        Response r = given().contentType(ContentType.JSON).body(body).post(base + path);
        long ms = System.currentTimeMillis() - t0;
        System.out.println("<- " + r.getStatusCode() + " POST " + path + " (" + ms + "ms)");
        System.out.println("  Response Body: " + r.getBody().asString().substring(0, Math.min(500, r.getBody().asString().length())));
        return r;
    }

    @Test(description = "M-API-001 Register new user 201")
    public void registerNewUser() {
        String unique = "apitest" + System.currentTimeMillis() + "@example.com";
        Map<String, Object> data = new HashMap<>();
        data.put("email", unique);
        data.put("password", "Xk9#mQ2$vL");
        data.put("first_name", "API");
        data.put("last_name", "Test");
        Response r = logAndPost("/users/register", data);
        Assert.assertEquals(r.getStatusCode(), 201, r.asString());
        Assert.assertNotNull(r.jsonPath().getString("id"));
        Assert.assertEquals(r.jsonPath().getString("email"), unique);
    }

    @Test(description = "M-API-002 Register existing email 409")
    public void registerExistingEmail() {
        Map<String, Object> data = Map.of("email", email, "password", "Xk9#mQ2$vL",
                "first_name", "API", "last_name", "Test");
        Response r = logAndPost("/users/register", data);
        Assert.assertEquals(r.getStatusCode(), 409, r.asString());
        Assert.assertNotNull(r.jsonPath().get("email"));
    }

    @Test(description = "M-API-003 Weak password 422")
    public void weakPassword() {
        Map<String, Object> data = Map.of("email", "apitest" + System.currentTimeMillis() + "@example.com",
                "password", "123", "first_name", "API", "last_name", "Test");
        Response r = logAndPost("/users/register", data);
        Assert.assertEquals(r.getStatusCode(), 422, r.asString());
        Assert.assertNotNull(r.jsonPath().get("password"));
    }

    @Test(description = "M-API-004 Login happy path 200 + token")
    public void loginHappyPath() {
        Map<String, Object> data = Map.of("email", email, "password", password);
        Response r = logAndPost("/users/login", data);
        Assert.assertEquals(r.getStatusCode(), 200, r.asString());
        Assert.assertNotNull(r.jsonPath().getString("access_token"));
        Assert.assertEquals(r.jsonPath().getString("token_type"), "bearer");
    }

    @Test(description = "M-API-005 Login invalid 401")
    public void loginInvalid() {
        Map<String, Object> data = Map.of("email", email, "password", "wrongpassword");
        Response r = logAndPost("/users/login", data);
        Assert.assertEquals(r.getStatusCode(), 401, r.asString());
        Assert.assertNotNull(r.jsonPath().get("error"));
    }

    @Test(description = "M-API-006 Login empty 401")
    public void loginEmpty() {
        Map<String, Object> data = Map.of("email", "", "password", "");
        Response r = logAndPost("/users/login", data);
        Assert.assertEquals(r.getStatusCode(), 401, r.asString());
    }

    private String token() {
        Map<String, Object> data = Map.of("email", email, "password", password);
        return given().contentType(ContentType.JSON).body(data).post(base + "/users/login")
                .jsonPath().getString("access_token");
    }

    @Test(description = "M-API-007 Get profile 200")
    public void getProfile() {
        String t = token();
        long t0 = System.currentTimeMillis();
        Response r = given().header("Authorization", "Bearer " + t).get(base + "/users/me");
        System.out.println("<- " + r.getStatusCode() + " GET /users/me (" + (System.currentTimeMillis() - t0) + "ms)");
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertEquals(r.jsonPath().getString("email"), email);
        Assert.assertNotNull(r.jsonPath().getString("first_name"));
        Assert.assertNotNull(r.jsonPath().getString("last_name"));
    }

    @Test(description = "M-API-008 Profile invalid token 401")
    public void profileInvalidToken() {
        Response r = given().header("Authorization", "Bearer invalidtoken123").get(base + "/users/me");
        Assert.assertEquals(r.getStatusCode(), 401);
    }

    @Test(description = "M-API-009 Profile missing token 401")
    public void profileMissingToken() {
        Response r = given().get(base + "/users/me");
        Assert.assertEquals(r.getStatusCode(), 401);
    }
}
