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
- ✅ **Total scenarios executed** (e.g., 26)
- ✅ **Pass/Fail count** (e.g., 10 passed, 16 failed)
- ✅ **Failure details with screenshots/logs**
- ✅ **Language-wise breakdown (EN vs AR)**
- ✅ **Key error categories (UI/AI response/security)**

---

## 🔑 **Deliverables Checklist**
-  Automated test scripts (organized & reusable – POM, data-driven)
- `test-data.json` with EN & AR prompts + expected responses
- `README.md` with:
  - How to run tests
  - How to configure language
  - Optional screenshots/logs
-  Test report (HTML summary + screenshots folder)

---
Scenario           	Language	Status	Notes
Chat UI Load	            EN  Pass	Widget loaded successfully
AI Response Common Query	EN	Fail	Hallucination detected
Script Injection Handling	AR	Fail	Handled incorrectly in test
Response Time	AR	Pass	< 1s
			




📊 High-Level Summary
Suite	Passed	Failed	Skipped	Total Tests	Duration
English Tests	6	      7	       0	   13	   294,094 ms
Arabic Tests	4       9	       0	   13	   305,357 ms
Total	10	   16	      0	      26	         599,451 ms
👉 Pass Rate: 10/26 → 38.4% (Needs significant improvement)
👉 Major Failures:
Functional/UI issues: testChatInterfaceLoads, testScrollFunctionality
AI Response Quality: testCommonQueriesResponseQuality, testResponseFormatting
Security tests: testSpecialCharactersHandling, testPromptInjectionHandling
Stability issues: Browser/session crashes (NoSuchSessionException)



Key Observations From  Report

English Tests: 6 passed, 7 failed
Arabic Tests: 4 passed, 9 failed
Failures mostly in:
AI response validation (hallucinations, formatting, fallback messages)
Chatbot UI (load, scroll, input clearing)
Security tests (script injection, special character handling)
Passes highlight your framework handling:
Response time
Input clear after sending
Rendering messages


Automated Test Scripts
UI behavior validation (chat widget load, input box, multilingual LTR/RTL, scrolling)
AI/ML response validation with mocked AI data for deterministic tests
Security & injection handling tests
test-data.json
Predefined prompts in English & Arabic
Expected AI responses for mock-driven testing
HTML TestNG Report (Evidence)
Shows all tests executed with Pass/Fail status
Includes stack traces & screenshots on failure
Highlights multilingual, UI, AI-response & security test categories
README.md
How to run the suite locally or in CI (Maven + TestNG)
How to configure test language (EN/AR)


“Actual GPT responses vary with model updates and context; for consistent regression testing, we used mocked responses for common public-service prompts.”




Project Structure

UAskChatbotAutomation/
 ├─ pom.xml
 ├─ src
 │   ├─ main
 │   │   └─ java
 │   │       ├─ pages/ChatbotPage.java
 │   │       ├─ utils/ConfigReader.java
 │   │       └─ utils/GPTResponseValidator.java
 │   └─ test
 │       └─ java
 │           ├─ tests/ChatbotUITests.java
 │           ├─ tests/ResponseValidationTests.java
 │           └─ tests/SecurityTests.java
 ├─ resources
 │   ├─ config.properties
 │   └─ test-data.json
 ├─ testng.xml
 └─ README.md

