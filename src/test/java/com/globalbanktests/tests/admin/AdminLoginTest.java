package com.globalbanktests.tests.admin;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.admin.AdminLoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verifies the Admin Login button navigates to the admin dashboard.
 */
@Epic("Admin Panel")
@Feature("Admin Authentication")
public class AdminLoginTest extends TestSetup {

    @Test(priority = 0, description = "Verify clicking the Admin Login button opens the admin dashboard")
    @Story("Admin Login Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that clicking the Bank Manager Login button on the home page successfully navigates the admin to the management dashboard.")
    public void verifyAdminLoginNavigation() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        holdFor(1500);
        adminLoginPage.navigateToAdminPanel();

        try {
            boolean buttonsVisible = adminLoginPage.areAdminDashboardButtonsVisible();
            Assert.assertTrue(
                    buttonsVisible,
                    "Admin dashboard should display Add Customer, Open Account and Customers buttons after login."
            );
        } catch (AssertionError assertionError) {
            adminLoginPage.captureScreenshot("Admin dashboard buttons missing");
            throw assertionError;
        }
    }

    private void holdFor(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
