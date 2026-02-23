package com.globalbanktests.tests.admin;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.admin.AdminLoginPage;
import com.globalbanktests.pages.admin.OpenAccountPage;
import io.qameta.allure.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Tests the Open Account flow in the Admin panel.
 */
@Epic("Admin Panel")
@Feature("Account Management")
public class OpenAccountTest extends TestSetup {

    @Test(priority = 2, description = "Verify a new bank account can be opened for an existing client")
    @Story("Open New Bank Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a Bank Manager can create a new Dollar account for an existing customer by selecting the customer and currency from the Open Account form.")
    public void verifyAccountCreation() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        OpenAccountPage openAccountPage = new OpenAccountPage(driver);

        adminLoginPage.navigateToAdminPanel();
        openAccountPage.goToOpenAccount();
        openAccountPage.chooseClient("Hermoine Granger");
        openAccountPage.chooseCurrency("Dollar");
        openAccountPage.confirmAccountCreation();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String alertText;

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            alertText = driver.switchTo().alert().getText();
            Allure.addAttachment("Account Number / Alert Text", alertText);
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            openAccountPage.captureScreenshot("Open account - no alert");
            Assert.fail("Expected account creation alert after opening account, but no alert appeared.");
            return;
        }

        try {
            boolean containsCreated = alertText.toLowerCase().contains("account created");
            boolean containsDigits = alertText.matches(".*\\d+.*");
            Assert.assertTrue(
                    containsCreated || containsDigits,
                    "Alert text should confirm account creation or contain an account number. Actual text: " + alertText
            );
        } catch (AssertionError assertionError) {
            openAccountPage.captureScreenshot("Open account - wrong alert text");
            throw assertionError;
        }
    }
}
