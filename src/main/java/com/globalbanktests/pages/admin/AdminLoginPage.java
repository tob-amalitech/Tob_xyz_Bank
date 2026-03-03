package com.globalbanktests.pages.admin;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Represents the main landing page where the Admin Manager login button is located.
 */
public class AdminLoginPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    @FindBy(xpath = "//button[contains(text(),'Bank Manager Login')]")
    private WebElement adminLoginBtn;

    // Admin dashboard buttons used for assertions after navigation
    @FindBy(xpath = "//button[contains(text(),'Add Customer')]")
    private WebElement addCustomerBtn;

    @FindBy(xpath = "//button[contains(text(),'Open Account')]")
    private WebElement openAccountBtn;

    @FindBy(xpath = "//button[contains(text(),'Customers')]")
    private WebElement customersBtn;

    // Constructor
    public AdminLoginPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigates to the Admin (Bank Manager) dashboard by clicking the login button.
     */
    @Step("Navigate to admin panel by clicking Bank Manager Login")
    public void navigateToAdminPanel() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(adminLoginBtn));
        highlightElement(adminLoginBtn, "purple");
        performClick(adminLoginBtn);
        holdExecution(1500);
    }

    /**
     * Verifies that all admin dashboard action buttons are visible.
     *
     * @return true if Add Customer, Open Account and Customers buttons are visible
     */
    @Step("Check that admin dashboard action buttons are visible")
    public boolean areAdminDashboardButtonsVisible() {
        return isElementVisible(addCustomerBtn)
                && isElementVisible(openAccountBtn)
                && isElementVisible(customersBtn);
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
