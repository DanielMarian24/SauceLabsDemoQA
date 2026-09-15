# SauceLabsMobileApp — Android + API Automation

Target: **Sauce Labs My Demo App (Android)** — `github.com/saucelabs/my-demo-app-android`
APK: download from Releases (e.g. `mda-2.2.0-25.apk`), place in `apps/` for local runs,
or upload to SauceLabs App Management and set `sauce.app.storage=storage:filename=...`.

Stack: **Appium 2.x + Java 17 + TestNG + Maven + UIAutomator2 + RestAssured + Allure + Healenium (AI)**.
Runs: local emulator OR SauceLabs RDC / Emulators. BE uses `https://api.practicesoftwaretesting.com`
(My Demo App has no public API — same LevelUp BE coverage).

## Structure
```
src/test/java/com/mydemo/
  base/BaseTest.java        # setup/teardown, timing, screenshots, sauce:job-result
  utils/DriverFactory.java  # local vs sauce switch
  utils/ConfigReader.java   # config.properties + env override, no hardcoding
  pages/                    # POM only — no locators in tests
  tests/                    # M-TC-001..018 (UI)
  api/                      # M-API-001..018 (BE)
src/test/resources/config.properties
testng.xml  # UI suite + API suite
.github/workflows/mobile-test.yml
```

## Run
```bash
brew install --cask android-studio # + create Pixel_8_API_34 emulator
npm i -g appium && appium driver install uiautomator2
emulator -avd Pixel_8
appium # in separate terminal
mkdir -p apps # put mda-*.apk here, update android.appPath if needed
mvn test                                   # everything (local + API)
mvn -Dtest=NavigationTest test             # single UI class
mvn -Dtest=AuthApiTest,ProductApiTest,CartApiTest test  # API only
# SauceLabs:
export SAUCE_USERNAME=xxx SAUCE_ACCESS_KEY=yyy
mvn -Dexecution.env=sauce test
mvn test -DsuiteXmlFile=testng.xml
```

No hardcoding: users, URLs, devices, timeouts in `config.properties`; secrets via env.

## Reporting
Allure + stdout logs: `-> METHOD url`, `Request Body`, `<- status (ms)`, screenshots on fail,
`executionTimeMs` attachment, Sauce session links.

## AI tool (mandatory)
**Healenium-web 3.4.1** — self-healing locators for Appium/Selenium.
- Use case: flaky `content-desc` / refactored IDs auto-heal, selector handling + debugging.
- Benefits: fewer broken builds, less locator maintenance.
- Limitations: needs local healing-backend or `.healenium` DB, first-run slower, still needs human review for new screens.
Plus: Appium Inspector AI locator suggestions + Sauce Visual for layout checks (documented, optional).

## Jira
See `jira-test-cases-mobile.md` — 18 UI + 18 API cases, 1:1 with TestNG methods.
