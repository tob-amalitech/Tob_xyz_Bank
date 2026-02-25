package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.pages.client.FundsWithdrawalPage;
import com.globalbanktests.tests.support.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Withdrawal functionality in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Fund Management")
public class FundsWithdrawalTest extends TestSetup {

    @Test(priority = 2, description = "Verify a customer can successfully withdraw funds")
    @Story("Withdraw Funds - Positive Flow")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyValidWithdrawal() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        FundsWithdrawalPage fundsWithdrawalPage = new FundsWithdrawalPage(driver);

        JsonNode customerNode = TestDataLoader.data().path("customer");
        String customerName = customerNode.path("defaultCustomerName").asText();
        int prepareDeposit = customerNode.path("deposit").path("prepareBalanceAmount").asInt();
        int validWithdraw = customerNode.path("withdrawal").path("validAmount").asInt();

        // Login
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(customerName);

        // Ensure sufficient balance
        int balanceBefore = clientPortalPage.getCurrentBalance();
        fundsDepositPage.makeDeposit(String.valueOf(prepareDeposit));
        String depositMessage = fundsDepositPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Pre-withdrawal deposit");

        Assert.assertTrue(
                depositMessage.toLowerCase().contains("deposit successful"),
                "Expected 'Deposit Successful' message before withdrawals. Actual: " + depositMessage
        );

        int balanceAfterDeposit = clientPortalPage.getCurrentBalance();

        // Valid withdrawal
        fundsWithdrawalPage.makeWithdrawal(String.valueOf(validWithdraw));
        String withdrawalMessage = fundsWithdrawalPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Withdrawal successful");

        Assert.assertTrue(
                withdrawalMessage.toLowerCase().contains("transaction successful"),
                "Expected successful withdrawal message. Actual: " + withdrawalMessage
        );

        int balanceAfterWithdrawal = clientPortalPage.getCurrentBalance();
        Assert.assertEquals(
                balanceAfterWithdrawal,
                balanceAfterDeposit - validWithdraw,
                "Balance should decrease by " + validWithdraw + " after withdrawal."
        );
    }

    @Test(priority = 3, description = "Verify withdrawal fails with insufficient funds")
    @Story("Withdraw Funds - Negative Flow")
    @Severity(SeverityLevel.NORMAL)
    public void verifyWithdrawalInsufficientFunds() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsWithdrawalPage fundsWithdrawalPage = new FundsWithdrawalPage(driver);

        JsonNode customerNode = TestDataLoader.data().path("customer");
        String customerName = customerNode.path("defaultCustomerName").asText();
        int invalidWithdraw = customerNode.path("withdrawal").path("insufficientAmount").asInt();

        // Login
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(customerName);

        // Attempt to withdraw more than available balance
        fundsWithdrawalPage.makeWithdrawal(String.valueOf(invalidWithdraw));
        String insufficientMsg = fundsWithdrawalPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Withdrawal insufficient funds");

        Assert.assertTrue(
                insufficientMsg.toLowerCase().contains("transaction failed"),
                "Expected 'Transaction Failed' message when withdrawing more than available balance. Actual: " + insufficientMsg
        );
    }
}