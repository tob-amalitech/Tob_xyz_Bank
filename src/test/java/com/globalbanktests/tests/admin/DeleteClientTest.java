package com.globalbanktests.tests.admin;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.admin.AdminLoginPage;
import com.globalbanktests.pages.admin.ClientListPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the Delete Customer functionality in the Admin panel.
 */
@Epic("Admin Panel")
@Feature("Customer Management")
public class DeleteClientTest extends TestSetup {

    @Test(priority = 5, description = "Verify a client can be found and deleted from the admin panel")
    @Story("Delete Customer Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a Bank Manager can locate a customer via the search bar and permanently remove them from the system using the Delete button.")
    public void verifyClientDeletion() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        ClientListPage clientListPage = new ClientListPage(driver);

        adminLoginPage.navigateToAdminPanel();
        clientListPage.openClientList();
        int initialRowCount = clientListPage.getCustomerRowCount();

        clientListPage.searchForClient("Hermoine");
        int filteredCount = clientListPage.getCustomerRowCount();

        try {
            Assert.assertTrue(
                    filteredCount > 0,
                    "Expected at least one row for customer 'Hermoine' before deletion, but none were found."
            );
        } catch (AssertionError assertionError) {
            clientListPage.captureScreenshot("Delete client - Hermoine not found before delete");
            throw assertionError;
        }

        clientListPage.removeClient();

        try {
            int afterDeleteCount = clientListPage.getCustomerRowCount();
            Assert.assertEquals(
                    afterDeleteCount,
                    initialRowCount - 1,
                    "Row count should decrease by 1 after deleting a customer."
            );

            clientListPage.searchForClient("Hermoine");
            int afterSearchCount = clientListPage.getCustomerRowCount();
            Assert.assertEquals(
                    afterSearchCount,
                    0,
                    "Searching for 'Hermoine' after deletion should return no results."
            );
        } catch (AssertionError assertionError) {
            clientListPage.captureScreenshot("Delete client - incorrect row count after delete");
            throw assertionError;
        }
    }
}
