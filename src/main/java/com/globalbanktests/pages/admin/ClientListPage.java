package com.globalbanktests.pages.admin;

import com.globalbanktests.core.AbstractPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object for the Clients List section of the Admin panel.
 * Supports viewing all clients, searching by name, and deleting a client.
 */
public class ClientListPage extends AbstractPage {

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
        super(driver);
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
        return !customerTableRows.isEmpty();
    }
}
