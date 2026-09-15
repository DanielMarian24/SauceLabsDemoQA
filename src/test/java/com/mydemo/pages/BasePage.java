package com.mydemo.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    public BasePage(AndroidDriver driver, int timeoutSec) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
    }

    protected WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement clickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected List<WebElement> allVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    protected void click(By by) {
        clickable(by).click();
    }

    protected void type(By by, String text) {
        WebElement el = visible(by);
        el.clear();
        if (text != null && !text.isEmpty()) {
            el.sendKeys(text);
        }
    }

    protected String text(By by) {
        return visible(by).getText();
    }

    protected boolean isVisible(By by, int sec) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(sec))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void hideKeyboard() {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
            }
        } catch (Exception ignored) {
        }
    }

    protected By aid(String id) {
        return AppiumBy.accessibilityId(id);
    }

    protected final String RID = "com.saucelabs.mydemoapp.android:id/";

    protected By idd(String resourceId) {
        return AppiumBy.id(RID + resourceId);
    }

    protected By byText(String text) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")");
    }

    protected int countInList(String listRid, String childRid) {
        try {
            return visible(idd(listRid)).findElements(row(childRid)).size();
        } catch (Exception e) {
            return 0;
        }
    }

    protected String rowTextInList(String listRid, String childRid, int index) {
        return visible(idd(listRid)).findElements(row(childRid)).get(index).getText();
    }

    protected void clickRowInList(String listRid, String childRid, int index) {
        visible(idd(listRid)).findElements(row(childRid)).get(index).click();
    }

    protected boolean textVisible(String expectedText, int sec) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(sec))
                    .until(ExpectedConditions.visibilityOfElementLocated(byText(expectedText)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private By row(String childRid) {
        return AppiumBy.xpath(".//*[@resource-id='" + RID + childRid + "']");
    }
}
