package com.globalbanktests.pages.client;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Withdrawal functionality within the Customer Portal.
 */
public class FundsWithdrawalPage extends AbstractPage {

    @FindBy(xpath = "//button[contains(text(),'Withdrawl')]")
    private WebElement withdrawalTabBtn;

    @FindBy(xpath = "//input[@type='number']")
    private WebElement amountField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement withdrawalSubmitBtn;

    @FindBy(css = ".error")
    private WebElement transactionMessage;

    public FundsWithdrawalPage(WebDriver driver) {
        super(driver);
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
}
