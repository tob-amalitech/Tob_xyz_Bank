package com.globalbanktests.core;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * AbstractPage serves as the foundation for all Page Object classes.
 * It provides shared WebDriver utilities, explicit wait helpers,
 * visual debugging aids, and Allure reporting helpers used
 * throughout the test framework.
 */
public abstract class AbstractPage {

    protected WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits until the given element is clickable.
     */
    protected void awaitClickable(WebElement element) {
        explicitWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the given element is visible on the page.
     */
    protected void awaitVisible(WebElement element) {
        explicitWait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Clicks an element safely, falling back to JavaScript click if intercepted.
     */
    protected void performClick(WebElement element) {
        awaitClickable(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException ex) {
            runJsClick(element);
        }
    }

    /**
     * Clears a field and types the given text after waiting for visibility.
     */
    protected void typeIntoField(WebElement element, String inputText) {
        awaitVisible(element);
        element.clear();
        element.sendKeys(inputText);
    }

    /**
     * Waits for the browser page to reach a fully loaded state.
     */
    protected void waitForFullPageLoad() {
        explicitWait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    /**
     * Checks whether the given element is currently visible on the page.
     *
     * @return true if visible, false if timeout occurs
     */
    protected boolean isElementVisible(WebElement element) {
        try {
            awaitVisible(element);
            return element.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Highlights a web element with a coloured border and background for visual debugging.
     *
     * @param element   the element to highlight
     * @param colour    border colour (e.g. "red", "green", "blue")
     */
    protected void highlightElement(WebElement element, String colour) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid " + colour + "'", element);
            js.executeScript("arguments[0].style.backgroundColor='lightyellow'", element);
        } catch (Exception ignored) {
            // Silently skip if highlighting fails — it's purely cosmetic
        }
    }

    /**
     * Pauses execution for the given number of milliseconds.
     * Used to make automated actions more visually observable.
     */
    protected void holdExecution(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Generic screenshot helper for Allure reporting.
     *
     * @param name logical name for the screenshot (appears in Allure)
     * @return screenshot bytes in PNG format
     */
    @Attachment(value = "Screenshot: {name}", type = "image/png")
    public byte[] captureScreenshot(String name) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Triggers a JavaScript click on the element directly via the DOM.
     */
    private void runJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
