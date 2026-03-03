package com.globalbanktests.pages.admin;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the Open Account section in the Admin panel.
 * Allows linking a currency account to an existing client.
 */
public class OpenAccountPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    @FindBy(xpath = "//button[contains(text(),'Open Account')]")
    private WebElement openAccountBtn;

    @FindBy(id = "userSelect")
    private WebElement clientDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement confirmBtn;

    public OpenAccountPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the Open Account button to begin the account creation flow.
     */
    @Step("Navigate to Open Account section")
    public void goToOpenAccount() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(openAccountBtn));
        highlightElement(openAccountBtn, "blue");
        performClick(openAccountBtn);
        holdExecution(1500);
    }

    /**
     * Selects a client from the dropdown by their visible name.
     *
     * @param clientName the full name of the client to select
     */
    @Step("Select client '{clientName}' from dropdown")
    public void chooseClient(String clientName) {
        explicitWait.until(ExpectedConditions.visibilityOf(clientDropdown));
        highlightElement(clientDropdown, "blue");
        new Select(clientDropdown).selectByVisibleText(clientName);
        holdExecution(800);
    }

    /**
     * Selects a currency for the new account.
     *
     * @param currencyType the currency to assign (e.g. "Dollar", "Pound")
     */
    @Step("Select currency '{currencyType}' for new account")
    public void chooseCurrency(String currencyType) {
        explicitWait.until(ExpectedConditions.visibilityOf(currencyDropdown));
        highlightElement(currencyDropdown, "blue");
        new Select(currencyDropdown).selectByVisibleText(currencyType);
        holdExecution(800);
    }

    /**
     * Submits the open account form.
     * The confirmation alert is intentionally left for the test to handle.
     */
    @Step("Confirm account creation")
    public void confirmAccountCreation() {
        explicitWait.until(ExpectedConditions.elementToBeClickable(confirmBtn));
        highlightElement(confirmBtn, "green");
        performClick(confirmBtn);
        holdExecution(1500);
    }

    // Helper methods from AbstractPage
    protected void awaitClickable(WebElement element) {
        explicitWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void awaitVisible(WebElement element) {
        explicitWait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void performClick(WebElement element) {
        awaitClickable(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException ex) {
            runJsClick(element);
        }
    }

    protected void typeIntoField(WebElement element, String inputText) {
        awaitVisible(element);
        element.clear();
        element.sendKeys(inputText);
    }

    protected void waitForFullPageLoad() {
        explicitWait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    protected boolean isElementVisible(WebElement element) {
        try {
            awaitVisible(element);
            return element.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void highlightElement(WebElement element, String colour) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid " + colour + "'", element);
            js.executeScript("arguments[0].style.backgroundColor='lightyellow'", element);
        } catch (Exception ignored) {
            // Silently skip if highlighting fails — it's purely cosmetic
        }
    }

    protected void holdExecution(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Attachment(value = "Screenshot: {name}", type = "image/png")
    public byte[] captureScreenshot(String name) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private void runJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
