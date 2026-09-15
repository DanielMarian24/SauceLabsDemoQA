package com.mydemo.base;

import com.mydemo.utils.ConfigReader;
import com.mydemo.utils.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {
    protected static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();
    private long startMs;

    protected AndroidDriver driver() {
        return DRIVER.get();
    }

    protected int waitSec() {
        return ConfigReader.getInt("explicit.wait.seconds", 15);
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        startMs = System.currentTimeMillis();
        AndroidDriver driver = DriverFactory.createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        DRIVER.set(driver);
        Allure.addAttachment("capabilities", driver.getCapabilities().toString());
        System.out.println("[START] " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        long elapsed = System.currentTimeMillis() - startMs;
        System.out.println("[END] " + result.getName() + " status=" + statusOf(result) + " timeMs=" + elapsed);
        Allure.addAttachment("executionTimeMs", String.valueOf(elapsed));
        AndroidDriver driver = DRIVER.get();
        try {
            if (!result.isSuccess() && driver != null) {
                byte[] shot = driver.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("screenshot", new ByteArrayInputStream(shot));
            }
            // Report to SauceLabs
            if (driver != null && "sauce".equalsIgnoreCase(ConfigReader.get("execution.env"))) {
                ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
            }
        } catch (Exception ignored) {
        } finally {
            if (driver != null) {
                driver.quit();
            }
            DRIVER.remove();
        }
    }

    private static String statusOf(ITestResult r) {
        return switch (r.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
