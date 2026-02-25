package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.pages.client.TransactionHistoryPage;
import com.globalbanktests.tests.support.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Transaction History view in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Transaction History")
public class TransactionHistoryTest extends TestSetup {

    @Test(priority = 3, description = "Verify a customer can view their transaction history and see recent deposits")
    @Story("View Transaction History - Recent Deposit")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyRecentDepositInTransactionHistory() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        TransactionHistoryPage transactionHistoryPage = new TransactionHistoryPage(driver);

        JsonNode customerNode = TestDataLoader.data().path("customer");
        String customerName = customerNode.path("transactionHistoryCustomerName").asText();
        int depositAmount = customerNode.path("deposit").path("transactionHistoryAmount").asInt();

        // Login
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(customerName);

        // Make a deposit
        fundsDepositPage.makeDeposit(String.valueOf(depositAmount));

        // Navigate to transaction history
        transactionHistoryPage.viewTransactionHistory();

        // Apply date filter: today (Use when there are many transactions and we want to ensure we see the recent deposit. If the app doesn't support date filtering, this step can be skipped.)
//        LocalDateTime now = LocalDateTime.now();
//        String startDateTime = now.minusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); // start a few mins earlier
//        String endDateTime = now.plusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); // end a few mins later
//
//        transactionHistoryPage.filterByDate(startDateTime, endDateTime);

        // Assert the table is visible
        Assert.assertTrue(
                transactionHistoryPage.isTransactionTableVisible(),
                "Transactions table should be visible after navigating to the Transactions tab."
        );

        // Assert the deposit exists
        Assert.assertTrue(
                transactionHistoryPage.hasTransaction(String.valueOf(depositAmount), "Credit"),
                "Transactions table should contain a Credit transaction with amount " + depositAmount + " for today."
        );
    }
}
