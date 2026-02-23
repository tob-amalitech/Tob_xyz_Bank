package com.globalbanktests.pages.client;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Deposit functionality within the Customer Portal.
 */
public class FundsDepositPage extends AbstractPage {

    @FindBy(xpath = "//button[contains(text(),'Deposit')]")
    private WebElement depositTabBtn;

    @FindBy(xpath = "//input[@type='number']")
    private WebElement amountField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement depositSubmitBtn;

    // Shared transaction message element (also used for withdrawals)
    @FindBy(css = ".error")
    private WebElement transactionMessage;

    public FundsDepositPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Performs a deposit of the specified amount.
     *
     * @param amount the deposit amount as a string
     */
    @Step("Make a deposit of amount: {amount}")
    public void makeDeposit(String amount) {
        explicitWait.until(ExpectedConditions.elementToBeClickable(depositTabBtn));
        highlightElement(depositTabBtn, "green");
        depositTabBtn.click();
        holdExecution(800);

        explicitWait.until(ExpectedConditions.visibilityOf(amountField));
        highlightElement(amountField, "green");
        amountField.clear();
        amountField.sendKeys(amount);
        holdExecution(800);

        explicitWait.until(ExpectedConditions.elementToBeClickable(depositSubmitBtn));
        highlightElement(depositSubmitBtn, "green");
        depositSubmitBtn.click();
        holdExecution(800);
    }

    /**
     * Returns the transaction feedback message text (e.g. 'Deposit Successful').
     */
    @Step("Read deposit transaction message")
    public String getTransactionMessage() {
        explicitWait.until(ExpectedConditions.visibilityOf(transactionMessage));
        return transactionMessage.getText().trim();
    }
}
