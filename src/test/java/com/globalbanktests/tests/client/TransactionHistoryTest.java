package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.pages.client.TransactionHistoryPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tests the Transaction History view in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Transaction History")
public class TransactionHistoryTest extends TestSetup {

    private static final String CUSTOMER_NAME = "Hermoine Granger";
    private static final int DEPOSIT_AMOUNT = 300;

    @Test(priority = 3, description = "Verify a customer can view their transaction history and see recent deposits")
    @Story("View Transaction History - Recent Deposit")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyRecentDepositInTransactionHistory() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        TransactionHistoryPage transactionHistoryPage = new TransactionHistoryPage(driver);

        // Login
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(CUSTOMER_NAME);

        // Make a deposit
        fundsDepositPage.makeDeposit(String.valueOf(DEPOSIT_AMOUNT));

        // Navigate to transaction history
        transactionHistoryPage.viewTransactionHistory();

        // Apply date filter: today
        LocalDateTime now = LocalDateTime.now();
        String startDateTime = now.minusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); // start a few mins earlier
        String endDateTime = now.plusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); // end a few mins later

        transactionHistoryPage.filterByDate(startDateTime, endDateTime);

        // Assert the table is visible
        Assert.assertTrue(
                transactionHistoryPage.isTransactionTableVisible(),
                "Transactions table should be visible after navigating to the Transactions tab."
        );

        // Assert the deposit exists
        Assert.assertTrue(
                transactionHistoryPage.hasTransaction(String.valueOf(DEPOSIT_AMOUNT), "Credit"),
                "Transactions table should contain a Credit transaction with amount " + DEPOSIT_AMOUNT + " for today."
        );
    }
}
