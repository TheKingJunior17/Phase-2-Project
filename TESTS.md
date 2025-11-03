# HW3 - Five Allocated Automated Tests

This document lists the five automated tests allocated for the HW3 assignment, their purposes, and expected behaviors.

## Test Allocation Summary

### Test 1: Password Strength Validation Test
**Purpose:** Validate password strength evaluation against security criteria  
**Input:** Various password strings with different complexity levels  
**Expected Output:** Correct strength ratings (Weak, Medium, Strong, Very Strong)  
**Preconditions:** Password evaluator is initialized and ready  
**Success Criteria:** All test cases return expected strength ratings within acceptable time limits  

### Test 2: User Authentication Integration Test
**Purpose:** Test complete user login workflow including database interactions  
**Input:** Valid and invalid username/password combinations  
**Expected Output:** Appropriate authentication responses and session management  
**Preconditions:** Test database is available and populated with test users  
**Success Criteria:** Successful authentication for valid credentials, proper rejection of invalid credentials  

### Test 3: Question Submission Validation Test
**Purpose:** Validate question submission form processing and data persistence  
**Input:** Question titles, descriptions, and user information in various formats  
**Expected Output:** Successful storage of valid questions, appropriate error handling for invalid inputs  
**Preconditions:** Database connection established, user authenticated  
**Success Criteria:** Valid questions stored correctly, invalid submissions rejected with clear error messages  

### Test 4: Role-Based Access Control Test
**Purpose:** Verify proper access control for different user roles (Student, Admin, Reviewer, Staff)  
**Input:** User roles and attempted access to various application features  
**Expected Output:** Appropriate access granted or denied based on role permissions  
**Preconditions:** Users with different roles exist in test database  
**Success Criteria:** Each role can only access authorized features, unauthorized access is properly blocked  

### Test 5: Database CRUD Operations Test
**Purpose:** Test Create, Read, Update, Delete operations for core entities (Users, Questions, Answers)  
**Input:** Various database operations with valid and edge case data  
**Expected Output:** Successful CRUD operations with proper data integrity  
**Preconditions:** Test database schema is initialized  
**Success Criteria:** All CRUD operations complete successfully, data integrity maintained, proper error handling for failures  

## Test Implementation Strategy

Each test will be implemented using:
- **JUnit 5** for test framework
- **Mockito** for mocking dependencies where needed
- **AssertJ** for fluent assertions
- **TestContainers** for database integration testing
- **Comprehensive Javadoc** documentation for all test methods

## Quality Assurance

All tests will be:
- **Independent:** Can run in any order without dependencies
- **Repeatable:** Produce consistent results across multiple runs
- **Fast:** Complete within reasonable time limits
- **Isolated:** Use test data that doesn't affect production
- **Documented:** Include clear documentation of purpose and expected behavior

---

*This test list fulfills Task 2 requirements (5% of total grade)*