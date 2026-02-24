package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.pages.client.FundsWithdrawalPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Withdrawal functionality in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Fund Management")
public class FundsWithdrawalTest extends TestSetup {

    private static final String CUSTOMER_NAME = "Hermoine Granger";
    private static final int PREPARE_BALANCE_DEPOSIT = 1000;
    private static final int VALID_WITHDRAW_AMOUNT = 200;
    private static final int INVALID_WITHDRAW_AMOUNT = 999999;

    @Test(priority = 2, description = "Verify a customer can successfully withdraw funds")
    @Story("Withdraw Funds - Positive Flow")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyValidWithdrawal() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        FundsWithdrawalPage fundsWithdrawalPage = new FundsWithdrawalPage(driver);

        // Login
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(CUSTOMER_NAME);

        // Ensure sufficient balance
        int balanceBefore = clientPortalPage.getCurrentBalance();
        fundsDepositPage.makeDeposit(String.valueOf(PREPARE_BALANCE_DEPOSIT));
        String depositMessage = fundsDepositPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Pre-withdrawal deposit");

        Assert.assertTrue(
                depositMessage.toLowerCase().contains("deposit successful"),
                "Expected 'Deposit Successful' message before withdrawals. Actual: " + depositMessage
        );

        int balanceAfterDeposit = clientPortalPage.getCurrentBalance();

        // Valid withdrawal
        fundsWithdrawalPage.makeWithdrawal(String.valueOf(VALID_WITHDRAW_AMOUNT));
        String withdrawalMessage = fundsWithdrawalPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Withdrawal successful");

        Assert.assertTrue(
                withdrawalMessage.toLowerCase().contains("transaction successful"),
                "Expected successful withdrawal message. Actual: " + withdrawalMessage
        );

        int balanceAfterWithdrawal = clientPortalPage.getCurrentBalance();
        Assert.assertEquals(
                balanceAfterWithdrawal,
                balanceAfterDeposit - VALID_WITHDRAW_AMOUNT,
                "Balance should decrease by " + VALID_WITHDRAW_AMOUNT + " after withdrawal."
        );
    }

    @Test(priority = 3, description = "Verify withdrawal fails with insufficient funds")
    @Story("Withdraw Funds - Negative Flow")
    @Severity(SeverityLevel.NORMAL)
    public void verifyWithdrawalInsufficientFunds() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsWithdrawalPage fundsWithdrawalPage = new FundsWithdrawalPage(driver);

        // Login
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(CUSTOMER_NAME);

        // Attempt to withdraw more than available balance
        fundsWithdrawalPage.makeWithdrawal(String.valueOf(INVALID_WITHDRAW_AMOUNT));
        String insufficientMsg = fundsWithdrawalPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Withdrawal insufficient funds");

        Assert.assertTrue(
                insufficientMsg.toLowerCase().contains("transaction failed"),
                "Expected 'Transaction Failed' message when withdrawing more than available balance. Actual: " + insufficientMsg
        );
    }
}