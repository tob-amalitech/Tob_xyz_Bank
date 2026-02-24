package com.globalbanktests.pages.client;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object for the Transactions history view in the Customer Portal.
 * Handles opening the transaction list and resetting the history.
 */
public class TransactionHistoryPage extends AbstractPage {

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
        super(driver);
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

        // Click filter if needed
        // filterButton.click();
    }

    /**
     * Returns true if the Reset button is currently displayed.
     */
    @Step("Check whether Reset button is visible to customer")
    public boolean isResetButtonVisible() {
        return isElementVisible(resetHistoryBtn);
    }
}

