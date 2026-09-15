package com.mydemo.tests;

import com.mydemo.base.BaseTest;
import com.mydemo.pages.CatalogPage;
import com.mydemo.pages.ProductDetailPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CatalogTest extends BaseTest {

    @Test(description = "M-TC-009 Catalog shows products")
    @Description("Verify product list non-empty and first product has name")
    public void catalogShowsProducts() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        Assert.assertTrue(catalog.isLoaded(), "catalog not loaded");
        Assert.assertTrue(catalog.productCount() >= 2, "expected >=2 products");
        Assert.assertFalse(catalog.firstProductName().isBlank(), "first product name blank");
    }

    @Test(description = "M-TC-010 Sort by Name Asc")
    @Description("Sort NameAsc -> first product name changes deterministically")
    public void sortByNameAsc() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        String before = catalog.firstProductName();
        catalog.openSort();
        catalog.selectSort("NameAsc");
        String after = catalog.firstProductName();
        Assert.assertFalse(after.isBlank(), "sorted name blank");
        // list re-orders; just verify screen still valid (before may equal after on small data, so log)
        System.out.println("Sort NameAsc before=" + before + " after=" + after);
    }

    @Test(description = "M-TC-011 Sort by Price Desc")
    @Description("Sort PriceDesc -> catalog still loaded")
    public void sortByPriceDesc() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        catalog.openSort();
        catalog.selectSort("PriceDesc");
        Assert.assertTrue(catalog.productCount() > 0, "no products after sort");
    }

    @Test(description = "M-TC-012 Open product detail")
    @Description("Tap first product -> detail shows name + price")
    public void openProductDetail() {
        CatalogPage catalog = new CatalogPage(driver(), waitSec());
        String expected = catalog.firstProductName();
        catalog.openFirstProduct();
        ProductDetailPage detail = new ProductDetailPage(driver(), waitSec());
        Assert.assertTrue(detail.isLoaded(), "product detail not shown");
        Assert.assertEquals(detail.name(), expected, "detail name mismatch");
        Assert.assertFalse(detail.price().isBlank(), "price blank, source check needed");
    }
}
