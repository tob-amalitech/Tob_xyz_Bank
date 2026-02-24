package com.globalbanktests.pages.client;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

/**
 * Page Object for the Customer Portal login flow.
 * Handles navigating to the customer section and selecting a customer account.
 */
public class ClientPortalPage extends AbstractPage {

    @FindBy(xpath = "//button[contains(text(),'Customer Login')]")
    private WebElement customerLoginBtn;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    // Dashboard buttons and balance used for customer login assertions
    @FindBy(xpath = "//button[contains(text(),'Deposit')]")
    private WebElement depositBtn;

    @FindBy(xpath = "//button[contains(text(),'Withdrawl')]")
    private WebElement withdrawBtn;

    @FindBy(xpath = "//button[contains(text(),'Transactions')]")
    private WebElement transactionsBtn;

    @FindBy(xpath = "//div[contains(@class,'center')]//strong[2]")
    private WebElement balanceDisplay;

    public ClientPortalPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks the Customer Login button to enter the client portal.
     */
    @Step("Click Customer Login button to open client portal")
    public void enterClientPortal() {
        waitForFullPageLoad();
        awaitClickable(customerLoginBtn);
        highlightElement(customerLoginBtn, "teal");
        customerLoginBtn.click();
        explicitWait.withTimeout(Duration.ofSeconds(5));
    }

    /**
     * Selects a customer from the dropdown and submits to log in.
     *
     * @param customerName the full name of the customer to select
     */
    @Step("Login as customer '{customerName}'")
    public void loginAsCustomer(String customerName) {
        awaitVisible(customerDropdown);
        highlightElement(customerDropdown, "teal");
        customerDropdown.sendKeys(customerName);
        customerDropdown.submit();
        holdExecution(1000);
    }

    /**
     * Returns true if the customer dropdown is visible on the login screen.
     */
    @Step("Check that customer dropdown is visible")
    public boolean isCustomerDropdownVisible() {
        return isElementVisible(customerDropdown);
    }

    /**
     * Returns true if the customer dashboard buttons and balance display are visible.
     */
    @Step("Verify customer dashboard (Deposit, Withdrawl, Transactions, Balance) is visible")
    public boolean isCustomerDashboardVisible() {
        return isElementVisible(depositBtn)
                && isElementVisible(withdrawBtn)
                && isElementVisible(transactionsBtn)
                && isElementVisible(balanceDisplay);
    }

    /**
     * Returns the current numeric account balance displayed on the dashboard.
     */
    @Step("Get currently displayed account balance")
    public int getCurrentBalance() {
        String text = balanceDisplay.getText().trim();
        return Integer.parseInt(text);
    }
}
