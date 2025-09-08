# Bwin Live Betting Automation Task

This project is a small, production-ready automation framework for testing **Live Betting** functionalities on [sports.bwin.com](https://sports.bwin.com/en/sports/live/betting).

It was designed for an **SDET technical interview task** and demonstrates:
- Writing clean, maintainable Java + Selenium code
- Handling **dynamic, real-time elements** (e.g. live odds updates)
- Designing reusable test components
- Running tests on **different viewports** (desktop & mobile)
- Improving test stability with **retry logic** for flaky scenarios

---

## ✅ Tech Stack

- **Java 23**
- **Selenium 4.29.0**
- **TestNG 7.11**
- **WebDriverManager 6.2.0**
- **Maven** for build & dependency management
- **IntelliJ IDEA** as IDE

---

## ✅ Project Structure

```
src
 └── main
      └── java
          └── test.bwin
              ├── config
              │    ├── Config.java          # Loads config.properties + system overrides
              ├── core
              │    ├── DriverFactory.java   # Creates WebDriver (desktop/mobile)
              │    ├── DriverManager.java   # Thread-safe driver holder
              │    ├── TestBase.java        # Common setup/teardown
              │    └── Waits.java           # Explicit wait helpers
              │
              ├── pages
              │    ├── LiveBettingPage.java       # Page object for main Live Betting page
              │    └── components
              |         └── BetslipComponent.java # Betslip component
              │         └── AZSportsComponent.java # A-Z sports navigation component
              │
              ├── util
              │    └── RetryAnalyzer.java   # Retries flaky tests (e.g., live odds updates)
              │
├── test
    └── java
         └── test.bwin
              ├── AddPickToBetslipTest.java       # Test case 1
              ├── CheckSportSortingTest.java      # Test case 2
              └── LiveOddsUpdatesTest.java        # Test case 3
```

---

## ✅ Test Scenarios

### 1. **Add Pick to Betslip**
- Navigate to Live Betting
- Select a live event → add a pick
- Assert the pick appears in the betslip and is highlighted

### 2. **Check Sport Sorting**
- Navigate to A–Z Sports
- Select a sport (e.g., Table Tennis)
- Verify the sport page loads and is highlighted in the UI, with the correct URL

### 3. **Validate Live Odds Updates**
- Open **Table Tennis** (frequent updates)
- Capture initial odds
- Wait for live odds to update dynamically (without refresh)
- Assert odds value/indicator changes
- Assert **up (green)** / **down (red)** indicators are shown
- Includes a **RetryAnalyzer** to re-run this test once if it fails (because live odds can be flaky in production)

---

## ✅ Configuration

Configuration is in [`config.properties`](src/test/resources/config.properties). Example:

```properties
base.url=https://sports.bwin.com/en/sports/live/betting
browser=chrome
device=desktop
headless=false

# Mobile viewport defaults
mobile.width=390
mobile.height=844

# Timeouts (seconds)
implicit.wait=2
explicit.wait=10
```

System properties can override any config value.  
For example, `-Ddevice=mobile` will override the `device` property.

---

## ✅ Running Tests

### Desktop (default)
```bash
mvn clean test -Ddevice=desktop
```

### Mobile (custom viewport)
```bash
mvn clean test   -Ddevice=mobile   -Dmobile.width=390   -Dmobile.height=844
```

### Custom base URL
```bash
mvn clean test -Ddevice=desktop -DbaseUrl=https://sports.bwin.com/en/sports/live/betting
```

---

## ✅ Sample Output

### Passing run (desktop)
```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running test.bwin.AddPickToBetslipTest
Tests run: 1, Failures: 0, Skipped: 0, Time elapsed: 5.12 s

Running test.bwin.CheckSportSortingTest
Tests run: 1, Failures: 0, Skipped: 0, Time elapsed: 3.87 s

Running test.bwin.LiveOddsUpdatesTest
Tests run: 1, Failures: 0, Skipped: 0, Time elapsed: 34.44 s

Results:
Tests run: 3, Failures: 0, Skipped: 0
BUILD SUCCESS
-------------------------------------------------------
```

### RetryAnalyzer in action
If `LiveOddsUpdatesTest` fails once (e.g., no update within timeout), TestNG automatically retries it:

```
[RetryAnalyzer] Retrying test: LiveOddsUpdatesTest.oddsUpdateAndIndicators (attempt 2 of 2)
```

---

## ✅ Notes & Design Decisions

- **DriverManager** → thread-local driver for parallel safety
- **DriverFactory** → centralizes browser/device creation
- **Waits** → explicit wait utilities (visibility, clickable, text change, etc.)
- **Config** → reads `config.properties` and system properties (`-Dkey=value`)
- **Page Object Model (POM)** → each page/component has its own locators & methods
- **Dynamic handling** → odds updates are polled using snapshots to detect re-renders or indicator changes
- **RetryAnalyzer** → handles occasional flakiness of real-time live odds
- **Viewport switching** → run same tests against both desktop and mobile layouts

---

## ✅ Future Improvements (if time allowed)

- Add screenshot capture on test failure
- Add logging (SLF4J or Allure reports)
- Run in parallel (`mvn test -Dparallel=methods`)
- Docker/Grid execution for CI/CD
- Optimize test cases for mobile viewport

---

## ✅ Author

*Petar Asenov* 
