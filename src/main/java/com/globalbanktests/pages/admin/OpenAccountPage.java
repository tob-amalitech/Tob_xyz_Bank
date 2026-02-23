package com.globalbanktests.pages.admin;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the Open Account section in the Admin panel.
 * Allows linking a currency account to an existing client.
 */
public class OpenAccountPage extends AbstractPage {

    @FindBy(xpath = "//button[contains(text(),'Open Account')]")
    private WebElement openAccountBtn;

    @FindBy(id = "userSelect")
    private WebElement clientDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement confirmBtn;

    public OpenAccountPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks the Open Account button to begin the account creation flow.
     */
    @Step("Navigate to Open Account section")
    public void goToOpenAccount() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(openAccountBtn));
        highlightElement(openAccountBtn, "blue");
        performClick(openAccountBtn);
        holdExecution(1500);
    }

    /**
     * Selects a client from the dropdown by their visible name.
     *
     * @param clientName the full name of the client to select
     */
    @Step("Select client '{clientName}' from dropdown")
    public void chooseClient(String clientName) {
        explicitWait.until(ExpectedConditions.visibilityOf(clientDropdown));
        highlightElement(clientDropdown, "blue");
        new Select(clientDropdown).selectByVisibleText(clientName);
        holdExecution(800);
    }

    /**
     * Selects a currency for the new account.
     *
     * @param currencyType the currency to assign (e.g. "Dollar", "Pound")
     */
    @Step("Select currency '{currencyType}' for new account")
    public void chooseCurrency(String currencyType) {
        explicitWait.until(ExpectedConditions.visibilityOf(currencyDropdown));
        highlightElement(currencyDropdown, "blue");
        new Select(currencyDropdown).selectByVisibleText(currencyType);
        holdExecution(800);
    }

    /**
     * Submits the open account form.
     * The confirmation alert is intentionally left for the test to handle.
     */
    @Step("Confirm account creation")
    public void confirmAccountCreation() {
        explicitWait.until(ExpectedConditions.elementToBeClickable(confirmBtn));
        highlightElement(confirmBtn, "green");
        performClick(confirmBtn);
        holdExecution(1500);
    }
}
