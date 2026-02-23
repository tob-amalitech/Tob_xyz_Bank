package com.globalbanktests.pages.admin;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the main landing page where the Admin Manager login button is located.
 */
public class AdminLoginPage extends AbstractPage {

    @FindBy(xpath = "//button[contains(text(),'Bank Manager Login')]")
    private WebElement adminLoginBtn;

    // Admin dashboard buttons used for assertions after navigation
    @FindBy(xpath = "//button[contains(text(),'Add Customer')]")
    private WebElement addCustomerBtn;

    @FindBy(xpath = "//button[contains(text(),'Open Account')]")
    private WebElement openAccountBtn;

    @FindBy(xpath = "//button[contains(text(),'Customers')]")
    private WebElement customersBtn;

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Admin (Bank Manager) dashboard by clicking the login button.
     */
    @Step("Navigate to admin panel by clicking Bank Manager Login")
    public void navigateToAdminPanel() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(adminLoginBtn));
        highlightElement(adminLoginBtn, "purple");
        performClick(adminLoginBtn);
        holdExecution(1500);
    }

    /**
     * Verifies that all admin dashboard action buttons are visible.
     *
     * @return true if Add Customer, Open Account and Customers buttons are visible
     */
    @Step("Check that admin dashboard action buttons are visible")
    public boolean areAdminDashboardButtonsVisible() {
        return isElementVisible(addCustomerBtn)
                && isElementVisible(openAccountBtn)
                && isElementVisible(customersBtn);
    }
}
