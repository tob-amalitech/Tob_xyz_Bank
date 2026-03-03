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
 * Page Object for the Withdrawal functionality within the Customer Portal.
 */
public class FundsWithdrawalPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    @FindBy(xpath = "//button[contains(text(),'Withdrawl')]")
    private WebElement withdrawalTabBtn;

    @FindBy(xpath = "//input[@type='number']")
    private WebElement amountField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement withdrawalSubmitBtn;

    @FindBy(css = ".error")
    private WebElement transactionMessage;

    public FundsWithdrawalPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Performs a withdrawal of the specified amount.
     *
     * @param amount the withdrawal amount as a string
     */
    @Step("Make a withdrawal of amount: {amount}")
    public void makeWithdrawal(String amount) {
        explicitWait.until(ExpectedConditions.elementToBeClickable(withdrawalTabBtn));
        highlightElement(withdrawalTabBtn, "red");
        withdrawalTabBtn.click();
        holdExecution(800);

        explicitWait.until(ExpectedConditions.visibilityOf(amountField));
        highlightElement(amountField, "red");
        amountField.clear();
        amountField.sendKeys(amount);
        holdExecution(800);

        explicitWait.until(ExpectedConditions.elementToBeClickable(withdrawalSubmitBtn));
        highlightElement(withdrawalSubmitBtn, "red");
        withdrawalSubmitBtn.click();
        holdExecution(800);
    }

    /**
     * Returns the withdrawal transaction feedback message text.
     */
    @Step("Read withdrawal transaction message")
    public String getTransactionMessage() {
        explicitWait.until(ExpectedConditions.visibilityOf(transactionMessage));
        return transactionMessage.getText().trim();
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
