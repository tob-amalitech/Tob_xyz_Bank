package com.globalbanktests.pages.admin;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the Register New Client form in the Admin panel.
 * Handles navigating to the form and submitting a new client's details.
 */
public class RegisterClientPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

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
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
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

    // Helper methods from AbstractPage
    protected void awaitClickable(WebElement element) {
        explicitWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void awaitVisible(WebElement element) {
        explicitWait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void performClick(WebElement element) {
        awaitClickable(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException ex) {
            runJsClick(element);
        }
    }

    protected void typeIntoField(WebElement element, String inputText) {
        awaitVisible(element);
        element.clear();
        element.sendKeys(inputText);
    }

    protected void waitForFullPageLoad() {
        explicitWait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    protected boolean isElementVisible(WebElement element) {
        try {
            awaitVisible(element);
            return element.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void highlightElement(WebElement element, String colour) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid " + colour + "'", element);
            js.executeScript("arguments[0].style.backgroundColor='lightyellow'", element);
        } catch (Exception ignored) {
            // Silently skip if highlighting fails — it's purely cosmetic
        }
    }

    protected void holdExecution(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Attachment(value = "Screenshot: {name}", type = "image/png")
    public byte[] captureScreenshot(String name) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private void runJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
