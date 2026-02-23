package com.globalbanktests.tests.client;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.client.ClientPortalPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Customer Login flow.
 */
@Epic("Customer Portal")
@Feature("Customer Authentication")
public class ClientLoginTest extends TestSetup {

    @Test(priority = 0, description = "Verify a customer can log in via the client portal")
    @Story("Customer Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test verifies that a customer can access the client portal by clicking the Customer Login button and selecting their name from the dropdown.")
    public void verifyCustomerLogin() throws InterruptedException {
        ClientPortalPage clientPortalPage = new ClientPortalPage(driver);

        Thread.sleep(2000);
        clientPortalPage.enterClientPortal();

        try {
            Assert.assertTrue(
                    clientPortalPage.isCustomerDropdownVisible(),
                    "Customer dropdown (#userSelect) should be visible after clicking Customer Login."
            );
        } catch (AssertionError assertionError) {
            clientPortalPage.captureScreenshot("Client login - dropdown not visible");
            throw assertionError;
        }

        Thread.sleep(2000);
        clientPortalPage.loginAsCustomer("Hermoine Granger");

        try {
            Assert.assertTrue(
                    clientPortalPage.isCustomerDashboardVisible(),
                    "Customer dashboard (Deposit, Withdrawl, Transactions, Balance) should be visible after login."
            );
        } catch (AssertionError assertionError) {
            clientPortalPage.captureScreenshot("Client login - dashboard not visible");
            throw assertionError;
        }
    }
}
