package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import com.globalbanktests.tests.support.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Deposit functionality in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Fund Management")
public class FundsDepositTest extends TestSetup {

    @Test(priority = 1, description = "Verify a customer can successfully deposit funds")
    @Story("Deposit Funds - Positive Flow")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-003")
    public void verifySuccessfulDeposit() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);

        JsonNode customerNode = TestDataLoader.data().path("customer");
        String customerName = customerNode.path("defaultCustomerName").asText();
        int depositAmount = customerNode.path("deposit").path("happyAmount").asInt();

        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(customerName);

        int startingBalance = clientPortalPage.getCurrentBalance();

        fundsDepositPage.makeDeposit(String.valueOf(depositAmount));
        String successMessage = fundsDepositPage.getTransactionMessage();

        Assert.assertTrue(
                successMessage.toLowerCase().contains("deposit successful"),
                "Expected success message after deposit. Actual message: " + successMessage
        );

        int balanceAfterDeposit = clientPortalPage.getCurrentBalance();
        Assert.assertEquals(
                balanceAfterDeposit,
                startingBalance + depositAmount,
                "Balance should increase by " + depositAmount + " after deposit."
        );
    }

    @Test(priority = 2, description = "Verify negative deposit amounts are rejected (balance remains unchanged)")
    @Story("Deposit Funds - Negative Validation")
    @Severity(SeverityLevel.NORMAL)
    @Issue("BUG-003")
    @TmsLink("TC-003-BUG")
    public void verifyNegativeDepositRejected() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);

        JsonNode customerNode = TestDataLoader.data().path("customer");
        String customerName = customerNode.path("defaultCustomerName").asText();
        int negativeAmount = customerNode.path("deposit").path("negativeAmount").asInt();

        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(customerName);

        // Capture balance before negative deposit
        int startingBalance = clientPortalPage.getCurrentBalance();

        // Attempt negative deposit
        fundsDepositPage.makeDeposit(String.valueOf(negativeAmount));

        // Capture balance after attempt
        int afterBalance = clientPortalPage.getCurrentBalance();

        // Screenshot for Allure reporting
        fundsDepositPage.captureScreenshot("Negative deposit attempt");

        // Assert balance did NOT change
        Assert.assertEquals(
                afterBalance,
                startingBalance,
                "Balance should not change when attempting a negative deposit."
        );
    }
}