package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Deposit functionality in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Fund Management")
public class FundsDepositTest extends TestSetup {

    private static final String CUSTOMER_NAME = "Hermoine Granger";
    private static final int DEPOSIT_AMOUNT = 500;

    @Test(priority = 1, description = "Verify a customer can successfully deposit funds")
    @Story("Deposit Funds - Positive Flow")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-003")
    public void verifySuccessfulDeposit() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);

        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(CUSTOMER_NAME);

        int startingBalance = clientPortalPage.getCurrentBalance();

        fundsDepositPage.makeDeposit(String.valueOf(DEPOSIT_AMOUNT));
        String successMessage = fundsDepositPage.getTransactionMessage();

        Assert.assertTrue(
                successMessage.toLowerCase().contains("deposit successful"),
                "Expected success message after deposit. Actual message: " + successMessage
        );

        int balanceAfterDeposit = clientPortalPage.getCurrentBalance();
        Assert.assertEquals(
                balanceAfterDeposit,
                startingBalance + DEPOSIT_AMOUNT,
                "Balance should increase by " + DEPOSIT_AMOUNT + " after deposit."
        );
    }

    @Test(priority = 2, description = "Verify negative deposit amounts are rejected")
    @Story("Deposit Funds - Negative Validation")
    @Severity(SeverityLevel.NORMAL)
    @Issue("BUG-003")
    @TmsLink("TC-003-BUG")
    public void verifyNegativeDepositRejected() {

        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);

        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer(CUSTOMER_NAME);

        fundsDepositPage.makeDeposit("-100");
        String negativeMessage = fundsDepositPage.getTransactionMessage();

        Allure.addAttachment("BUG-003 Actual Deposit Message", negativeMessage);

        boolean negativeAccepted = negativeMessage.toLowerCase().contains("deposit successful");

        Assert.assertFalse(
                negativeAccepted,
                "Negative deposit amounts should be rejected, but application indicates success."
        );
    }
}