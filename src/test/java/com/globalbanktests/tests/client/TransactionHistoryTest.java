package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.pages.client.TransactionHistoryPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Transaction History view in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Transaction History")
public class TransactionHistoryTest extends TestSetup {

    @Test(priority = 3, description = "Verify a customer can view their transaction history")
    @Story("View Transaction History")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that a logged-in customer can view their transaction history, including a recent Credit transaction of 300.")
    public void verifyTransactionHistoryIsVisible() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        TransactionHistoryPage transactionHistoryPage = new TransactionHistoryPage(driver);

        holdFor(1500);
        clientPortalPage.enterClientPortal();
        holdFor(1000);
        clientPortalPage.loginAsCustomer("Hermoine Granger");

        // Ensure at least one transaction exists
        fundsDepositPage.makeDeposit("300");

        holdFor(1000);
        transactionHistoryPage.viewTransactionHistory();

        try {
            Assert.assertTrue(
                    transactionHistoryPage.isTransactionTableVisible(),
                    "Transactions table should be visible after navigating to the Transactions tab."
            );

            int rowCount = transactionHistoryPage.getTransactionRowCount();
            Assert.assertTrue(
                    rowCount > 0,
                    "Transactions table should contain at least one row after making a deposit."
            );

            Assert.assertTrue(
                    transactionHistoryPage.hasTransaction("300", "Credit"),
                    "Transactions table should contain a Credit transaction with amount 300."
            );
        } catch (AssertionError assertionError) {
            transactionHistoryPage.captureScreenshot("Transaction history - table or row missing");
            throw assertionError;
        }
    }

    private void holdFor(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
