package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import com.globalbanktests.pages.client.FundsDepositPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Tests the Deposit functionality in the Customer Portal.
 */
@Epic("Customer Portal")
@Feature("Fund Management")
public class FundsDepositTest extends TestSetup {

    @Test(priority = 1, description = "Verify a customer can deposit funds into their bank account")
    @Story("Deposit Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a logged-in customer can deposit funds and that the balance is updated correctly. It also validates that negative deposits should be rejected (known bug BUG-003).")
    @Issue("BUG-003")
    @TmsLink("TC-003")
    public void verifyFundsDeposit() {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);
        FundsDepositPage fundsDepositPage = new FundsDepositPage(driver);
        SoftAssert softAssert = new SoftAssert();

        holdFor(1500);
        clientPortalPage.enterClientPortal();
        clientPortalPage.loginAsCustomer("Hermoine Granger");

        int startingBalance = clientPortalPage.getCurrentBalance();

        // Happy path deposit
        holdFor(1000);
        fundsDepositPage.makeDeposit("500");
        String successMessage = fundsDepositPage.getTransactionMessage();
        clientPortalPage.captureScreenshot("Deposit 500 successful");

        softAssert.assertTrue(
                successMessage.toLowerCase().contains("deposit successful"),
                "Expected success message after deposit. Actual message: " + successMessage
        );

        int balanceAfterDeposit = clientPortalPage.getCurrentBalance();
        softAssert.assertEquals(
                balanceAfterDeposit,
                startingBalance + 500,
                "Balance should increase by 500 after deposit."
        );

        // Negative deposit bug validation
        holdFor(1000);
        fundsDepositPage.makeDeposit("-100");
        String negativeMessage = fundsDepositPage.getTransactionMessage();
        Allure.addAttachment("BUG-003 Actual Deposit Message", negativeMessage);
        fundsDepositPage.captureScreenshot("BUG-003 Negative deposit behaviour");

        boolean negativeAccepted = negativeMessage.toLowerCase().contains("deposit successful");
        softAssert.assertFalse(
            negativeAccepted,
            "Negative deposit amounts should be rejected, but application indicates success."
        );

        try {
            softAssert.assertAll();
        } catch (AssertionError assertionError) {
            clientPortalPage.captureScreenshot("Funds deposit - soft assertion failures");
            throw assertionError;
        }
    }

    private void holdFor(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
