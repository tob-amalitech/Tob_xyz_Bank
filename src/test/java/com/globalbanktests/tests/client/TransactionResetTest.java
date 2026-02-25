package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.pages.client.TransactionHistoryPage;
import com.globalbanktests.tests.support.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Tests the Reset Transaction History functionality in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Transaction History")
public class TransactionResetTest extends TestSetup {

    @Test(priority = 4, description = "Verify a customer can reset/clear their transaction history")
    @Story("Reset Transaction History")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that regular customers should NOT be able to reset transaction history. Presence of the Reset button is a security bug (BUG-004).")
    @Issue("BUG-004")
    @TmsLink("TC-004")
    public void verifyTransactionHistoryReset() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        TransactionHistoryPage transactionHistoryPage = new TransactionHistoryPage(driver);
        SoftAssert softAssert = new SoftAssert();

        JsonNode customerNode = TestDataLoader.data().path("customer");
        String customerName = customerNode.path("defaultCustomerName").asText();
        int seedAmount = customerNode.path("deposit").path("transactionResetAmount").asInt();

        holdFor(1500);
        clientPortalPage.enterClientPortal();
        holdFor(1000);
        clientPortalPage.loginAsCustomer(customerName);

        // Ensure history is not empty
        fundsDepositPage.makeDeposit(String.valueOf(seedAmount));

        holdFor(1000);
        transactionHistoryPage.viewTransactionHistory();
        holdFor(1000);

        int initialRows = transactionHistoryPage.getTransactionRowCount();
        softAssert.assertTrue(
                initialRows > 0,
                "Precondition failed: transaction history should not be empty before testing Reset."
        );

        boolean resetVisible = transactionHistoryPage.isResetButtonVisible();
        Allure.addAttachment(
                "Security Issue",
                "Reset button is accessible to customers, violating Transaction Security acceptance criteria"
        );
        transactionHistoryPage.captureScreenshot("BUG-004 Reset button visible to customer");

        softAssert.assertFalse(
                resetVisible,
                "Reset button should NOT be visible or accessible to regular customers."
        );

        // Demonstrate the impact: customer is able to clear history
        if (resetVisible) {
            transactionHistoryPage.clearTransactionHistory();
        }

        try {
            softAssert.assertAll();
        } catch (AssertionError assertionError) {
            transactionHistoryPage.captureScreenshot("BUG-004 SoftAssert failures");
            throw assertionError;
        }
    }

    private void holdFor(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
