package com.globalbanktests.tests.admin;

import com.globalbanktests.base.TestSetup;
import com.globalbanktests.pages.admin.AdminLoginPage;
import com.globalbanktests.pages.admin.RegisterClientPage;
import com.globalbanktests.tests.support.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

/**
 * Tests the Add Customer flow in the Admin panel.
 */
@Epic("Admin Panel")
@Feature("Customer Management")
public class RegisterClientTest extends TestSetup {

    @Test(priority = 1, description = "Verify a new client can be registered through the admin panel")
    @Story("Register New Customer")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a Bank Manager can successfully add a new customer by providing a first name, last name, and postal code through the admin dashboard.")
    public void verifyNewClientRegistration() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        RegisterClientPage registerClientPage = new RegisterClientPage(driver);

        JsonNode data = TestDataLoader.data().path("admin").path("registerValidCustomer");
        String firstName = data.path("firstName").asText();
        String lastName = data.path("lastName").asText();
        String postCode = data.path("postCode").asText();

        adminLoginPage.navigateToAdminPanel();
        registerClientPage.openRegistrationForm();
        registerClientPage.registerNewClient(firstName, lastName, postCode);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String alertText;

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            registerClientPage.captureScreenshot("Register valid customer - no alert");
            Assert.fail("Expected confirmation alert after registering a valid customer, but no alert appeared.");
            return;
        }

        try {
            Assert.assertTrue(
                    alertText.toLowerCase().contains("customer added"),
                    "Alert text should confirm that the customer was added. Actual text: " + alertText
            );
        } catch (AssertionError assertionError) {
            registerClientPage.captureScreenshot("Register valid customer - wrong alert text");
            throw assertionError;
        }
    }

    @Test(priority = 2, description = "Numeric first name should be rejected by validation")
    @Story("Register Customer Input Validation - Numeric First Name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the Add Customer form enforces alphabetic-only validation on the first name field. Numeric input should be rejected.")
    @Issue("BUG-001")
    @TmsLink("TC-001")
    public void verifyNumericFirstNameIsRejected() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        RegisterClientPage registerClientPage = new RegisterClientPage(driver);
        SoftAssert softAssert = new SoftAssert();

        JsonNode data = TestDataLoader.data().path("admin").path("registerNumericName");
        String firstName = data.path("firstName").asText();
        String lastName = data.path("lastName").asText();
        String postCode = data.path("postCode").asText();

        adminLoginPage.navigateToAdminPanel();
        registerClientPage.openRegistrationForm();
        registerClientPage.registerNewClient(firstName, lastName, postCode);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        boolean alertAppeared;
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            String text = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            Allure.addAttachment("BUG-001 Actual Alert Text", text);
            registerClientPage.captureScreenshot("BUG-001 Numeric name accepted");
            alertAppeared = true;
        } catch (TimeoutException e) {
            alertAppeared = false;
        }

        softAssert.assertFalse(
                alertAppeared,
                "Customer names should only contain alphabetic characters, but numeric first name was accepted."
        );

        try {
            softAssert.assertAll();
        } catch (AssertionError assertionError) {
            registerClientPage.captureScreenshot("BUG-001 SoftAssert failures");
            throw assertionError;
        }
    }

    @Test(priority = 3, description = "Alphabetic postal code should be rejected by validation")
    @Story("Register Customer Input Validation - Alphabetic Postcode")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the Add Customer form enforces numeric-only validation on the post code field. Alphabetic postal codes should be rejected.")
    @Issue("BUG-002")
    @TmsLink("TC-002")
    public void verifyAlphabeticPostcodeIsRejected() {
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        RegisterClientPage registerClientPage = new RegisterClientPage(driver);
        SoftAssert softAssert = new SoftAssert();

        JsonNode data = TestDataLoader.data().path("admin").path("registerAlphabeticPostcode");
        String firstName = data.path("firstName").asText();
        String lastName = data.path("lastName").asText();
        String postCode = data.path("postCode").asText();

        adminLoginPage.navigateToAdminPanel();
        registerClientPage.openRegistrationForm();
        registerClientPage.registerNewClient(firstName, lastName, postCode);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        boolean alertAppeared;
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            String text = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            Allure.addAttachment("BUG-002 Actual Alert Text", text);
            registerClientPage.captureScreenshot("BUG-002 Alphabetic postcode accepted");
            alertAppeared = true;
        } catch (TimeoutException e) {
            alertAppeared = false;
        }

        softAssert.assertFalse(
                alertAppeared,
                "Postal codes should contain only numeric characters, but alphabetic post code was accepted."
        );

        try {
            softAssert.assertAll();
        } catch (AssertionError assertionError) {
            registerClientPage.captureScreenshot("BUG-002 SoftAssert failures");
            throw assertionError;
        }
    }
}
