package com.globalbanktests.tests.admin;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.admin.AdminLoginPage;
import com.globalbanktests.pages.admin.ClientListPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the client list view in the Admin panel.
 */
@Epic("Admin Panel")
@Feature("Customer Management")
public class ViewClientsTest extends TestSetup {

    @Test(priority = 3, description = "Verify the admin can navigate to and view the list of registered clients")
    @Story("View Customer List")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that a Bank Manager can navigate to the Customers section and see the full list of registered customers.")
    public void verifyClientListIsAccessible() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        ClientListPage clientListPage = new ClientListPage(driver);

        adminLoginPage.navigateToAdminPanel();
        clientListPage.openClientList();

        try {
            Assert.assertTrue(
                    clientListPage.isCustomerTableVisible(),
                    "Customers table should be visible after navigating to Customers section."
            );

            int rowCount = clientListPage.getCustomerRowCount();
            Assert.assertTrue(
                    rowCount > 0,
                    "Customers table should contain at least one customer row, but row count was: " + rowCount
            );
        } catch (AssertionError assertionError) {
            clientListPage.captureScreenshot("View clients - table or rows missing");
            throw assertionError;
        }
    }
}
