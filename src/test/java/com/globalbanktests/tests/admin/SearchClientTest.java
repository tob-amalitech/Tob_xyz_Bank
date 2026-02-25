package com.globalbanktests.tests.admin;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.admin.AdminLoginPage;
import com.globalbanktests.pages.admin.ClientListPage;
import com.globalbanktests.tests.support.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the customer search functionality in the Admin panel.
 */
@Epic("Admin Panel")
@Feature("Customer Management")
public class SearchClientTest extends TestSetup {

    @Test(priority = 4, description = "Verify client search filters the list by matching name")
    @Story("Search Customer by Name")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that a Bank Manager can search for a specific customer by name and that the customers table filters correctly to show only matching results.")
    public void verifyClientSearch() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        ClientListPage clientListPage = new ClientListPage(driver);

        JsonNode data = TestDataLoader.data().path("admin");
        String searchName = data.path("searchCustomerName").asText();

        adminLoginPage.navigateToAdminPanel();
        clientListPage.openClientList();
        clientListPage.searchForClient(searchName);

        try {
            Assert.assertTrue(
                    clientListPage.getCustomerRowCount() > 0,
                    "Customer table should show at least one row matching the search term '" + searchName + "'."
            );

            Assert.assertTrue(
                    clientListPage.doAllRowsContain(searchName),
                    "All visible customer rows should contain the search term '" + searchName + "'."
            );
        } catch (AssertionError assertionError) {
            clientListPage.captureScreenshot("Search client - incorrect filter results");
            throw assertionError;
        }
    }
}
