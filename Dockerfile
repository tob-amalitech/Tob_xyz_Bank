# ─────────────────────────────────────────────────────────────────
# Stage 1: Build — compile the project and run tests inside Maven
# ─────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-11 AS build

WORKDIR /app

# Copy pom first so Docker can cache dependency downloads
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Install Chrome and ChromeDriver for headless Selenium execution
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    --no-install-recommends && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
        > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable --no-install-recommends && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Run tests — WebDriverManager will auto-download the matching ChromeDriver
RUN mvn test -B \
    -Dwebdriver.chrome.driver=/usr/bin/chromedriver \
    || true
# The '|| true' ensures the container build succeeds even if tests fail,
# so we can still collect Allure results from CI.

# ─────────────────────────────────────────────────────────────────
# Stage 2: Report — serve Allure results as a lightweight artifact
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:11-jre-jammy AS report

WORKDIR /app

# Copy allure results from build stage
COPY --from=build /app/target/allure-results ./target/allure-results
COPY --from=build /app/target /app/target

CMD ["echo", "Test results available in /app/target/allure-results"]
