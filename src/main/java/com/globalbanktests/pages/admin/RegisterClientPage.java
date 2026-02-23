package com.globalbanktests.pages.admin;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Register New Client form in the Admin panel.
 * Handles navigating to the form and submitting a new client's details.
 */
public class RegisterClientPage extends AbstractPage {

    @FindBy(xpath = "//button[contains(text(),'Add Customer')]")
    private WebElement registerClientBtn;

    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//input[@placeholder='Post Code']")
    private WebElement postCodeField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitBtn;

    public RegisterClientPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens the Register Client form from the admin dashboard.
     * Asserts that the first name field is present once the form loads.
     */
    @Step("Open Add Customer registration form")
    public void openRegistrationForm() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(registerClientBtn));
        highlightElement(registerClientBtn, "blue");
        performClick(registerClientBtn);
        holdExecution(1500);

        if (!isElementVisible(firstNameField)) {
            throw new IllegalStateException("Registration form failed to load — first name field not found.");
        }
    }

    /**
     * Fills in the registration form and submits it.
     *
     * @param firstName client's first name
     * @param lastName  client's last name
     * @param postCode  client's postal code
     */
    @Step("Register new client: {firstName} {lastName}, postcode {postCode}")
    public void registerNewClient(String firstName, String lastName, String postCode) {
        explicitWait.until(ExpectedConditions.visibilityOf(firstNameField));
        highlightElement(firstNameField, "blue");
        typeIntoField(firstNameField, firstName);
        holdExecution(800);

        explicitWait.until(ExpectedConditions.visibilityOf(lastNameField));
        highlightElement(lastNameField, "blue");
        typeIntoField(lastNameField, lastName);
        holdExecution(800);

        explicitWait.until(ExpectedConditions.visibilityOf(postCodeField));
        highlightElement(postCodeField, "blue");
        typeIntoField(postCodeField, postCode);
        holdExecution(800);

        explicitWait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        highlightElement(submitBtn, "green");
        performClick(submitBtn);
        holdExecution(1500);
        // NOTE: alert (if any) is intentionally NOT handled here so tests
        // can assert on its presence and text.
    }
}
