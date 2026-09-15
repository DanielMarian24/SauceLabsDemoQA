package com.mydemo.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static AndroidDriver createDriver() {
        String env = ConfigReader.get("execution.env");
        try {
            if ("sauce".equalsIgnoreCase(env)) {
                return createSauceDriver();
            }
            return createLocalDriver();
        } catch (Exception e) {
            throw new RuntimeException("Driver creation failed (env=" + env + ")", e);
        }
    }

    private static AndroidDriver createLocalDriver() throws Exception {
        String appPath = ConfigReader.get("android.appPath");
        String absApp = Paths.get(appPath).isAbsolute()
                ? appPath
                : Paths.get(System.getProperty("user.dir"), appPath).toAbsolutePath().toString();
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformVersion(ConfigReader.get("android.platformVersion"))
                .setDeviceName(ConfigReader.get("android.deviceName"))
                .setAutomationName(ConfigReader.get("android.automationName"))
                .setApp(absApp)
                .setAppPackage(ConfigReader.get("android.appPackage"))
                .setAppActivity(ConfigReader.get("android.appActivity"))
                .setNoReset(Boolean.parseBoolean(ConfigReader.get("android.noReset")))
                .setNewCommandTimeout(Duration.ofSeconds(120));
        // match local AVD name so Appium boots the right emulator
        options.setCapability("appium:avd", ConfigReader.get("android.deviceName"));
        URL server = new URL(ConfigReader.get("appium.server.url"));
        return new AndroidDriver(server, options);
    }

    private static AndroidDriver createSauceDriver() throws Exception {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        if (username == null || accessKey == null) {
            throw new IllegalStateException("SAUCE_USERNAME / SAUCE_ACCESS_KEY env vars required for execution.env=sauce");
        }
        String region = ConfigReader.get("sauce.region");
        String dataCenter = region.contains("eu") ? "eu-central-1" : "us-west-1";

        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .setApp(ConfigReader.get("sauce.app.storage"))
                .setDeviceName(ConfigReader.get("sauce.device.name"))
                .setPlatformVersion(ConfigReader.get("sauce.device.platformVersion"));

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", username);
        sauceOptions.put("accessKey", accessKey);
        sauceOptions.put("build", ConfigReader.get("sauce.build"));
        sauceOptions.put("name", "MyDemoApp Android");
        sauceOptions.put("appiumVersion", "2.x");
        options.setCapability("sauce:options", sauceOptions);

        URL server = new URL("https://" + username + ":" + accessKey + "@ondemand." + dataCenter + ".saucelabs.com:443/wd/hub");
        return new AndroidDriver(server, options);
    }
}
