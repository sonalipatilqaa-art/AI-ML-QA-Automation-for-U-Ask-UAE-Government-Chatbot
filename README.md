# U-Ask Chatbot QA Automation

## Overview
This repository contains automated tests for validating the U-Ask AI-powered chatbot for UAE Government services. The tests cover UI behavior, AI response quality, and security aspects.

## Prerequisites
- Java 11 or higher
- Maven 3.6+
- Chrome browser
- Node.js (for additional utilities)

## Installation
```bash
git clone <repository-url>
cd uask-qa-automation

install dependencies:
mvn clean install

Run all tests:
mvn test

Generate the TestNG HTML report:
mvn surefire-report:report

Configure Test Language
Go to resources/config.properties
Set:
language=EN        # or AR
The framework dynamically loads prompts from test-data.json.

📂 Test Data
All test prompts and expected outputs are defined in resources/test-data.json.
🖼️ Screenshots / Logs
Failed test screenshots are saved in reports/screenshots/.
Logs are generated in the console and under reports/logs/.
📊 Test Report
After execution, view:
reports/TestNG-report.html
This includes:
Total passed/failed/skipped
Error logs
Links to screenshots for failures

---

## 📊 **Test Report Summary**
Your final **test report** should include:
- ✅ **Total scenarios executed** (e.g., 26)
- ✅ **Pass/Fail count** (e.g., 10 passed, 16 failed)
- ✅ **Failure details with screenshots/logs**
- ✅ **Language-wise breakdown (EN vs AR)**
- ✅ **Key error categories (UI/AI response/security)**

---

## 🔑 **Deliverables Checklist**
- [x] Automated test scripts (organized & reusable – POM, data-driven)
- [x] `test-data.json` with EN & AR prompts + expected responses
- [x] `README.md` with:
  - How to run tests
  - How to configure language
  - Optional screenshots/logs
- [x] Test report (HTML summary + screenshots folder)

---

📊 High-Level Summary
Suite	Passed	Failed	Skipped	Total Tests	Duration
English Tests	6	      7	       0	   13	   294,094 ms
Arabic Tests	4     	9	       0	   13	   305,357 ms
Total	10	   16	      0	      26	         599,451 ms
👉 Pass Rate: 10/26 → 38.4% (Needs significant improvement)
👉 Major Failures:
Functional/UI issues: testChatInterfaceLoads, testScrollFunctionality
AI Response Quality: testCommonQueriesResponseQuality, testResponseFormatting
Security tests: testSpecialCharactersHandling, testPromptInjectionHandling
Stability issues: Browser/session crashes (NoSuchSessionException)


Project Structure

U-Ask-AI-ML-QA-Automation/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── utils/
│   │            ├── ConfigManager.java      # Reads language/config
│   │            └── TestDataLoader.java     # Loads test-data.json
│   │
│   └── test/
│       └── java/
│           ├── base/
│           │    └── BaseTest.java           # Common setup/teardown
│           ├── tests/
│           │    ├── EnglishTests.java
│           │    ├── ArabicTests.java
│           │    └── SecurityTests.java
│           └── pages/
│                └── ChatbotPage.java        # Page Object for chatbot UI
│
├── resources/
│   ├── test-data.json                        # Prompts & expected responses
│   └── config.properties                     # Browser, URL, language
│
├── reports/
│   ├── TestNG-report.html
│   └── screenshots/                          # Failed test screenshots
│
├── README.md
└── pom.xml / package.json (depending on Java/Maven or JS/TS setup)



