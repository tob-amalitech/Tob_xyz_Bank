# Global Bank UI Test Suite

A Selenium + TestNG automated UI test framework using **Page Object Model (POM)**, with **Allure Reports**, **Docker** containerisation, and a **GitHub Actions** CI pipeline.

---

## Project Structure

```
.
├── .github/
│   └── workflows/
│       └── ci.yml                         # GitHub Actions CI pipeline
├── src/
│   ├── main/java/com/globalbanktests/
│   │   ├── core/AbstractPage.java         # Base page object
│   │   └── pages/
│   │       ├── admin/                     # Admin panel page objects
│   │       └── client/                    # Customer portal page objects
│   └── test/
│       ├── java/com/globalbanktests/
│       │   ├── base/TestSetup.java        # WebDriver setup/teardown
│       │   └── tests/
│       │       ├── admin/                 # Admin test classes
│       │       └── client/               # Client test classes
│       └── resources/
│           └── allure.properties          # Allure output config
├── Dockerfile                             # Docker build for test execution
├── docker-compose.yml                     # Selenium Grid + test runner
├── pom.xml
└── README.md
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 11+ |
| Maven | 3.6+ |
| Google Chrome | Latest |
| Docker & Docker Compose | Latest |
| Allure CLI (optional, local) | Latest |

---

## Running Locally

```bash
# Run all tests (headed — opens Chrome window)
mvn clean test

# Run a specific test class
mvn test -Dtest=RegisterClientTest

# Run headless (no Chrome window)
HEADLESS=true mvn clean test
```

---

## Generating Allure Reports

```bash
# Step 1 — Run tests
mvn clean test

# Step 2 — Generate HTML report
mvn allure:report

# Step 3 — Open report in browser (launches local server)
mvn allure:serve
```

---

## Running with Docker

```bash
# Build the Docker image and run tests inside a container
docker build --target build -t global-bank-tests .

# Or use Docker Compose (spins up Selenium Grid + Chrome node + test runner)
docker-compose up --build

# Watch the browser live via noVNC at:
# http://localhost:7900
```

---

## CI/CD — GitHub Actions

The pipeline runs automatically on every push to `main` or `develop`, and on every pull request to `main`.

**Pipeline steps:**
1. Checkout code
2. Set up Java 11
3. Install Chrome
4. Cache Maven dependencies
5. Run Selenium tests (headless)
6. Upload Allure results as artifact
7. Upload Surefire XML reports
8. Generate Allure HTML report
9. Deploy report to GitHub Pages
10. Post test summary to the PR/Actions UI

**To enable GitHub Pages reporting:**
- Go to your repo → Settings → Pages
- Set Source to: `Deploy from a branch` → branch: `gh-pages`
- After the first pipeline run, your Allure report will be live at:
  `https://<your-username>.github.io/<your-repo-name>/`

---

## Tech Stack

| Tool | Version |
|------|---------|
| Selenium WebDriver | 4.18.1 |
| TestNG | 7.10.2 |
| Allure TestNG | 2.25.0 |
| AspectJ | 1.9.20.1 |
| WebDriverManager | 5.7.0 |
| Maven Surefire | 3.2.5 |
