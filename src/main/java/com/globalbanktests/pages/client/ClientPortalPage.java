package com.globalbanktests.pages.client;

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
 * Page Object for the Customer Portal login flow.
 * Handles navigating to the customer section and selecting a customer account.
 */
public class ClientPortalPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    @FindBy(xpath = "//button[contains(text(),'Customer Login')]")
    private WebElement customerLoginBtn;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    // Dashboard buttons and balance used for customer login assertions
    @FindBy(xpath = "//button[contains(text(),'Deposit')]")
    private WebElement depositBtn;

    @FindBy(xpath = "//button[contains(text(),'Withdrawl')]")
    private WebElement withdrawBtn;

    @FindBy(xpath = "//button[contains(text(),'Transactions')]")
    private WebElement transactionsBtn;

    @FindBy(xpath = "//div[contains(@class,'center')]//strong[2]")
    private WebElement balanceDisplay;

    // constructor
    public ClientPortalPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the Customer Login button to enter the client portal.
     */
    @Step("Click Customer Login button to open client portal")
    public void enterClientPortal() {
        waitForFullPageLoad();
        awaitClickable(customerLoginBtn);
        highlightElement(customerLoginBtn, "teal");
        customerLoginBtn.click();
        explicitWait.withTimeout(Duration.ofSeconds(5));
    }

    /**
     * Selects a customer from the dropdown and submits to log in.
     *
     * @param customerName the full name of the customer to select
     */
    @Step("Login as customer '{customerName}'")
    public void loginAsCustomer(String customerName) {
        awaitVisible(customerDropdown);
        highlightElement(customerDropdown, "teal");
        customerDropdown.sendKeys(customerName);
        customerDropdown.submit();
        holdExecution(1000);
    }

    /**
     * Returns true if the customer dropdown is visible on the login screen.
     */
    @Step("Check that customer dropdown is visible")
    public boolean isCustomerDropdownVisible() {
        return isElementVisible(customerDropdown);
    }

    /**
     * Returns true if the customer dashboard buttons and balance display are visible.
     */
    @Step("Verify customer dashboard (Deposit, Withdrawl, Transactions, Balance) is visible")
    public boolean isCustomerDashboardVisible() {
        return isElementVisible(depositBtn)
                && isElementVisible(withdrawBtn)
                && isElementVisible(transactionsBtn)
                && isElementVisible(balanceDisplay);
    }

    /**
     * Returns the current numeric account balance displayed on the dashboard.
     */
    @Step("Get currently displayed account balance")
    public int getCurrentBalance() {
        String text = balanceDisplay.getText().trim();
        return Integer.parseInt(text);
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
