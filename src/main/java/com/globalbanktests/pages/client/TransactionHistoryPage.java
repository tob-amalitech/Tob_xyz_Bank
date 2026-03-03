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
import java.util.List;

/**
 * Page Object for the Transactions history view in the Customer Portal.
 * Handles opening the transaction list and resetting the history.
 */
public class TransactionHistoryPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    @FindBy(xpath = "//button[contains(text(),'Transactions')]")
    private WebElement transactionsTabBtn;

    @FindBy(id = "start")
    private WebElement startDateInput;

    @FindBy(id = "end")
    private WebElement endDateInput;

    @FindBy(xpath = "//button[contains(text(),'Reset')]")
    private WebElement resetHistoryBtn;

    @FindBy(css = "table.table")
    private WebElement transactionTable;

    @FindBy(css = "table.table tbody tr")
    private List<WebElement> transactionRows;

    public TransactionHistoryPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigates to the transaction history view.
     */
    @Step("Open Transactions history tab")
    public void viewTransactionHistory() {
        explicitWait.until(ExpectedConditions.elementToBeClickable(transactionsTabBtn));
        highlightElement(transactionsTabBtn, "blue");
        transactionsTabBtn.click();
        holdExecution(1500);
    }

    /**
     * Returns true if the transaction history table is visible.
     */
    @Step("Check that the transactions table is visible")
    public boolean isTransactionTableVisible() {
        return isElementVisible(transactionTable);
    }

    /**
     * Clicks the Reset button to clear the transaction history.
     */
    @Step("Click Reset to clear transaction history")
    public void clearTransactionHistory() {
        explicitWait.until(ExpectedConditions.elementToBeClickable(resetHistoryBtn));
        highlightElement(resetHistoryBtn, "orange");
        resetHistoryBtn.click();
        holdExecution(1500);
    }

    /**
     * Returns number of rows currently visible in the transaction table.
     */
    @Step("Get number of rows currently visible in transaction table")
    public int getTransactionRowCount() {
        return transactionRows.size();
    }

    /**
     * Returns true if at least one transaction contains the given amount and type.
     */
    @Step("Verify there is a transaction with amount {amount} and type {type}")
    public boolean hasTransaction(String amount, String type) {
        String amountStr = amount.trim();
        String typeLower = type.toLowerCase();
        for (WebElement row : transactionRows) {
            String rowText = row.getText();
            if (rowText.contains(amountStr) && rowText.toLowerCase().contains(typeLower)) {
                return true;
            }
        }
        return false;
    }


    public void filterByDate(String start, String end) {
        startDateInput.clear();
        startDateInput.sendKeys(start);

        endDateInput.clear();
        endDateInput.sendKeys(end);
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

    /**
     * Returns true if the Reset button is currently displayed.
     */
    @Step("Check whether Reset button is visible to customer")
    public boolean isResetButtonVisible() {
        return isElementVisible(resetHistoryBtn);
    }
}

