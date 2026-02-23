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

    @Test(priority = 2, description = "Verify a customer can withdraw funds from their bank account")
    @Story("Withdraw Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a logged-in customer can withdraw funds with sufficient balance and that the application prevents withdrawals that exceed the available balance.")
    public void verifyFundsWithdrawal() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        FundsWithdrawalPage fundsWithdrawalPage = new FundsWithdrawalPage(driver);

        holdFor(1500);
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer("Hermoine Granger");

        // Ensure sufficient balance by depositing 1000
        int balanceBefore = clientPortalPage.getCurrentBalance();
        fundsDepositPage.makeDeposit("1000");
        String depositMessage = fundsDepositPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Pre-withdrawal deposit 1000");

        try {
            Assert.assertTrue(
                    depositMessage.toLowerCase().contains("deposit successful"),
                    "Expected 'Deposit Successful' message before withdrawals. Actual: " + depositMessage
            );
        } catch (AssertionError assertionError) {
            fundsDepositPage.captureScreenshot("Withdrawal test - deposit failed");
            throw assertionError;
        }

        int balanceAfterDeposit = clientPortalPage.getCurrentBalance();

        // Valid withdrawal
        holdFor(1000);
        fundsWithdrawalPage.makeWithdrawal("200");
        String withdrawalMessage = fundsWithdrawalPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Withdrawal 200 successful");

        try {
            Assert.assertTrue(
                    withdrawalMessage.toLowerCase().contains("transaction successful"),
                    "Expected successful withdrawal message. Actual: " + withdrawalMessage
            );

            int balanceAfterWithdrawal = clientPortalPage.getCurrentBalance();
            Assert.assertEquals(
                    balanceAfterWithdrawal,
                    balanceAfterDeposit - 200,
                    "Balance should decrease by 200 after withdrawal."
            );
        } catch (AssertionError assertionError) {
            fundsWithdrawalPage.captureScreenshot("Withdrawal 200 - assertion failure");
            throw assertionError;
        }

        // Withdrawal with insufficient balance
        holdFor(1000);
        fundsWithdrawalPage.makeWithdrawal("999999");
        String insufficientMsg = fundsWithdrawalPage.getTransactionMessage();

        try {
            Assert.assertTrue(
                    insufficientMsg.toLowerCase().contains("transaction failed"),
                    "Expected 'Transaction Failed' message when withdrawing more than available balance. Actual: " + insufficientMsg
            );
        } catch (AssertionError assertionError) {
            fundsWithdrawalPage.captureScreenshot("Withdrawal insufficient funds - wrong message");
            throw assertionError;
        }
    }

    private void holdFor(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
