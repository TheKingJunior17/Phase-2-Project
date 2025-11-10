TP3 Test Plan - JUnit Tests

Objective
- Define the set of JUnit tests required for TP3, map them to selected user stories, and provide owners and success criteria.

High-level test categories (minimal required for this phase)
1. Test automation and CI readiness
   - Test: `BuildAndTestSmokeTest` - ensures Gradle build and test task completes successfully
   - Owner: <Member B>
2. Functional tests (selected features)
   - Test: `PasswordStrengthValidationTest` (subset) - verify core password strength behavior mapped to authentication flows
   - Test: `QuestionSubmissionValidationTest` (subset) - required validations for question content
   - Owner: <Member B>
3. Integration tests
   - Test: `AuthenticationIntegrationTest` - validate login workflow with in-memory DB
   - Owner: <Member B>
4. Documentation tests (meta)
   - Test: `JavadocPresenceTest` - verify that Javadoc files exist at `HW3/docs/javadoc` (simple file-assertion test)
   - Owner: <Member C>

Detailed test list (name, description, mapping)
- BuildAndTestSmokeTest: Run `./gradlew clean test` or Windows equivalent and assert exit code = 0. (maps to US-1)
- PasswordStrengthValidationTest: 8 targeted cases covering each strength boundary. (maps to US-1, US-5)
- QuestionSubmissionValidationTest: 8 cases covering empty, too long, invalid characters and success. (maps to US-1, US-5)
- AuthenticationIntegrationTest: 5 cases covering login success/failure and session creation. (maps to US-1)
- JavadocPresenceTest: 2 checks for javadoc index and tests javadoc index. (maps to US-2)

Test artifacts and evidence
- `HW3/docs/TEST_EXECUTION_EVIDENCE.md` will contain test run logs, screenshots, and timestamps.
- For each test run, capture console output and a short explanation of failures and fixes.

Execution and verification
- Use Gradle `./gradlew test` (or `gradlew.bat test` on Windows) and capture console output.
- Run Javadoc via Gradle `javadoc` task and verify the output directory exists.

Notes
- The tests above intentionally target the minimal subset of functionality required by the selected user stories. If new issues are uncovered, we will add follow-up test cases but prioritize finishing the required items.
