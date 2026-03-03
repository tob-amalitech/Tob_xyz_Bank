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
import java.util.List;

/**
 * Page Object for the Clients List section of the Admin panel.
 * Supports viewing all clients, searching by name, and deleting a client.
 */
public class ClientListPage {
    
    private WebDriver driver;
    protected WebDriverWait explicitWait;
    private static final int DEFAULT_WAIT_SECONDS = 10;

    @FindBy(xpath = "//button[contains(text(),'Customers')]")
    private WebElement clientListBtn;

    @FindBy(xpath = "//input[@placeholder = 'Search Customer']")
    private WebElement searchField;

    @FindBy(xpath = "//button[contains(text(),'Delete')]")
    private WebElement deleteBtn;

    @FindBy(css = "table")
    private WebElement customersTable;

    // Table rows used for view/search/delete assertions
    @FindBy(css = "table tbody tr")
    private List<WebElement> customerTableRows;

    public ClientListPage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigates to the clients list view.
     */
    @Step("Open Customers list in admin panel")
    public void openClientList() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(clientListBtn));
        highlightElement(clientListBtn, "orange");
        performClick(clientListBtn);
        holdExecution(1500);
    }

    /**
     * Returns true if the customers table is visible on the page.
     */
    @Step("Check that the Customers table is visible")
    public boolean isCustomerTableVisible() {
        return isElementVisible(customersTable);
    }

    /**
     * Types a client's name into the search bar to filter results.
     *
     * @param clientName the name or partial name to search for
     */
    @Step("Search for customer by name: {clientName}")
    public void searchForClient(String clientName) {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.visibilityOf(searchField));
        highlightElement(searchField, "orange");
        performClick(searchField);
        typeIntoField(searchField, clientName);
        holdExecution(1500);
    }

    /**
     * Clicks the Delete button to remove the currently displayed client.
     */
    @Step("Delete currently filtered customer")
    public void removeClient() {
        waitForFullPageLoad();
        explicitWait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
        highlightElement(deleteBtn, "red");
        performClick(deleteBtn);
        holdExecution(1500);
    }

    /**
     * Returns the current number of rows in the Customers table.
     */
    @Step("Get number of rows currently displayed in Customers table")
    public int getCustomerRowCount() {
        return customerTableRows.size();
    }

    /**
     * Returns true if all visible rows contain the specified search term.
     */
    @Step("Verify all visible customer rows contain search term: {searchTerm}")
    public boolean doAllRowsContain(String searchTerm) {
        String lower = searchTerm.toLowerCase();
        for (WebElement row : customerTableRows) {
            if (!row.getText().toLowerCase().contains(lower)) {
                return false;
            }
        }
        return true;
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
