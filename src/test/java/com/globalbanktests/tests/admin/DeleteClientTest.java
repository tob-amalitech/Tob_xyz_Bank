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

        // Step 1: Search for the client
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

        // Step 2: Delete the client
        clientListPage.removeClient();

        // Step 3: Search again to confirm deletion
        clientListPage.searchForClient("Hermoine");
        int afterSearchCount = clientListPage.getCustomerRowCount();

        try {
            Assert.assertEquals(
                    afterSearchCount,
                    0,
                    "Searching for 'Hermoine' after deletion should return no results."
            );
        } catch (AssertionError assertionError) {
            clientListPage.captureScreenshot("Delete client - customer still visible after delete");
            throw assertionError;
        }
    }
}
