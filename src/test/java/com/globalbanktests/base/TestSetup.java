package com.globalbanktests.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * TestSetup provides the WebDriver lifecycle management for all test classes.
 * Supports both local (headed) and CI/Docker (headless) execution via the
 * HEADLESS environment variable.
 */
public class TestSetup {

    protected static WebDriver driver;

    private static final String APP_URL =
            "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

    @BeforeMethod
    public void initialise() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        // Run headless when in CI or Docker (set HEADLESS=true env var)
        boolean headless = Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADLESS", "false")
        );
        if (headless) {
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--disable-gpu");
        } else {
            chromeOptions.addArguments("--start-maximized");
        }

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(chromeOptions);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.get(APP_URL);
    }

    @AfterMethod
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
