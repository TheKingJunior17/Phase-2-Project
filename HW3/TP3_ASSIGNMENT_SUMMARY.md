# TP3 Assignment Summary - Team Project Phase 3

## 📋 Assignment Overview

This document provides a comprehensive summary of the TP3 (Team Project Phase 3) deliverables, addressing all required tasks and grading criteria.

**Team**: Jose Mendoza (TheKingJunior17)  
**Course**: CSE 360 - Introduction to Software Engineering  
**Date**: November 9, 2025  
**GitHub Repository**: https://github.com/TheKingJunior17/Phase-2-Project  

---

## ✅ Task 1: Implementation Scope for This Phase (5%)

### What Will Be Implemented in TP3

Based on the existing HW3 automated testing system, this phase focuses on:

#### 1.1 Enhanced User Authentication Flow
- **Scope**: Extend existing authentication to support session management and password change requirements
- **Files**: `AuthenticationResult.java`, `UserAuthenticationService.java`
- **Rationale**: Builds on existing password validation to create complete user workflow

#### 1.2 Role-Based Access Control Enhancements  
- **Scope**: Implement granular permissions for student/reviewer/instructor roles
- **Files**: `RoleBasedAccessController.java`, `AccessPermission.java`
- **Rationale**: Required for multi-role application demonstrated in screencasts

#### 1.3 Question Submission Validation Pipeline
- **Scope**: Complete validation workflow for question content and metadata
- **Files**: `QuestionSubmissionService.java`, `QuestionValidator.java`
- **Rationale**: Core functionality for the educational platform use case

#### 1.4 Database Integration Layer
- **Scope**: CRUD operations with proper transaction handling
- **Files**: `DatabaseCRUDService.java`, entity classes
- **Rationale**: Data persistence required for realistic application demonstration

#### 1.5 Testing and Documentation Infrastructure
- **Scope**: Complete JUnit test suite, Javadoc generation, and architectural documentation
- **Files**: All test classes, UML diagrams, documentation
- **Rationale**: Required deliverables for TP3 submission

### Implementation Boundaries
- **In Scope**: Core authentication, authorization, validation, and database operations
- **Out of Scope**: Advanced UI components, external integrations, complex reporting features
- **Minimal Viable**: Focus on functionality needed to demonstrate user stories and pass tests

---

## ✅ Task 2: Implementation Plan and Progress Made (15%)

### 2.1 Team Work Allocation (5%)

| Team Member | Responsibilities | Integration Timeline |
|-------------|------------------|---------------------|
| **Jose Mendoza (Lead)** | Overall integration, GitHub management, Screencast 1 & 2 | Continuous integration |
| **Developer B** | JUnit test implementation, test automation scripts | Day 2-3, integrate Day 4 |
| **Designer C** | UML diagrams (Astah), architecture documentation, Javadoc | Day 2-4, review Day 5 |
| **QA D** | Test execution, evidence collection, Screencast 3 | Day 3-5, final verification |

**Architecture Updates Required**:
- UML class diagrams for authentication flow using Astah
- Sequence diagrams for user role interactions
- Component diagram showing test integration points
- Updated design documents mapping to implementation

**Testing & Integration Schedule**:
- Day 1: Complete user story selection and test planning
- Day 2: Implement core authentication and authorization classes
- Day 3: Complete validation and database layers with unit tests
- Day 4: Integration testing and Javadoc generation
- Day 5: Screencast recording and final verification

### 2.2 Standup Meeting Schedule (5%)

**Meeting Frequency**: Twice weekly (Tuesdays and Fridays, 2:00 PM MST)  
**Duration**: 15-20 minutes each  
**Format**: Video call with recording for TP3 deliverable

| Week | Meeting Date | Planned Topics |
|------|-------------|----------------|
| Week 10 | Nov 12 (Tue) | Initial progress, blocker identification |
| Week 10 | Nov 15 (Fri) | Mid-phase integration, test results |
| Week 11 | Nov 19 (Tue) | Screencast preparation, documentation review |
| Week 11 | Nov 22 (Fri) | Final integration, submission readiness |

### 2.3 Standup Meeting Notes (5%)

#### Standup #1 - November 9, 2025 (Kick-off)
**Attendees**: Jose Mendoza, [Team Members B, C, D]  
**Progress Since Last Meeting**: 
- Created TP3 planning documents and user stories
- Established GitHub structure and access permissions
- Reviewed existing HW3 codebase for extension points

**Today's Goals**:
- Finalize team member assignments and contact information
- Begin implementation of authentication enhancements
- Set up Astah for UML diagram creation

**Blockers/Issues**:
- Need to verify grader access to GitHub repository
- Confirm Astah license availability for UML diagrams
- Establish recording setup for standup meetings

**Decisions Made**:
- Use existing HW3 structure as foundation for TP3
- Focus on minimal viable implementation to meet requirements
- Schedule next standup for November 12

**Action Items**:
- Jose: Verify GitHub grader access and repository permissions
- Developer B: Begin JUnit test implementation for authentication
- Designer C: Start UML class diagrams in Astah
- QA D: Set up test evidence collection framework

#### Standup #2 - November 12, 2025 (Planned)
**Status**: To be conducted and documented  
**Expected Topics**: Authentication implementation progress, UML diagram review, test automation setup

---

## ✅ Task 3: List of JUnit Tests to be Implemented (10%)

### 3.1 Test Purpose and Implementation Plan (5%)

| Test Class | Purpose | Test Count | Owner |
|-----------|---------|------------|-------|
| **AuthenticationFlowTest** | Validate complete login workflow with session management | 15 tests | Developer B |
| **RolePermissionTest** | Verify role-based access control across user types | 20 tests | Developer B |
| **ValidationPipelineTest** | Test question submission validation rules | 12 tests | Developer B |
| **DatabaseIntegrationTest** | Verify CRUD operations and transaction handling | 18 tests | Developer B |
| **SystemIntegrationTest** | End-to-end user workflow testing | 10 tests | QA D |

**Total Tests**: 75 tests (extends existing 140 test foundation)

### 3.2 Test Implementation and Screencast Assignment (5%)

| Test Category | Implementer | Documentation Owner | Screencast Responsibility |
|--------------|-------------|-------------------|------------------------|
| **Authentication Flow** | Developer B | Designer C | Jose (Screencast 1) |
| **Role Permissions** | Developer B | Designer C | Jose (Screencast 1) |
| **Validation Pipeline** | Developer B | QA D | Jose (Screencast 2) |
| **Database Integration** | Developer B | QA D | Jose (Screencast 2) |
| **System Integration** | QA D | QA D | QA D (Screencast 3) |

**Implementation Standards**:
- All tests must use JUnit 5 framework with AssertJ assertions
- Each test requires comprehensive Javadoc documentation
- Test execution must be automated via Gradle build scripts
- Coverage reports generated and included in evidence documentation

---

## ✅ Task 4: Implemented TP3 as Planned (10%)

### 4.1 Code Consistency with Architecture (5%)

**UML Documentation Requirements**:
- Class diagrams created using Astah Community Edition
- Sequence diagrams for authentication and authorization flows
- Component diagrams showing test integration architecture
- All UML files exported as PNG and included in `HW3/docs/uml/`

**Design Implementation Mapping**:
- Authentication classes follow MVC pattern established in architecture
- Service layer maintains separation of concerns
- Entity classes use builder pattern for immutability
- Test classes mirror production package structure

### 4.2 Professional Internal Documentation (5%)

**Javadoc Standards Applied**:
- Class-level documentation includes purpose, usage examples, and author information
- Method-level documentation covers parameters, return values, and exceptions
- Package documentation provides architectural overview
- Custom tags used for cross-references and design notes

**Non-obvious Implementation Decisions**:
- Builder pattern in `AuthenticationResult` for immutability and thread safety
- Enum-based role permissions for type safety and extensibility  
- Parameterized tests for comprehensive validation coverage
- H2 in-memory database for reproducible integration tests

### 4.3 Javadoc URL and GitHub Access (5%)

**Javadoc Location**: 
- **URL**: https://github.com/TheKingJunior17/Phase-2-Project/tree/main/HW3/docs/javadoc
- **Local Path**: `HW3/docs/javadoc/index.html`
- **Generation Command**: `./gradlew javadoc` (Windows: `gradlew.bat javadoc`)

**GitHub Repository Access**:
- **Repository**: https://github.com/TheKingJunior17/Phase-2-Project
- **Branch**: main
- **Grader Access**: [To be configured with grader username]
- **Permissions**: Read access to entire repository and downloadable assets

---

## ✅ Task 5: GitHub URL and Screencasts (35%)

### 5.1 Working GitHub URL (5%)

**Primary Repository Link**: https://github.com/TheKingJunior17/Phase-2-Project/tree/main/HW3

**Access Verification Checklist**:
- [ ] Repository is accessible to grader account
- [ ] All source code visible in browser
- [ ] Screencast files downloadable from repository
- [ ] Documentation files properly formatted and readable

### 5.2 Screencast Requirements (30%)

#### Screencast 1: Code Implementation and Validation (10%)
**Duration**: 8-10 minutes  
**Content**:
- Overview of TP3 code changes and new functionality
- Live demonstration of JUnit test execution and results
- User perspective demonstration showing student/reviewer/instructor workflows
- Code walkthrough highlighting key implementation decisions

**Technical Specifications**:
- Format: MP4, 1080p resolution
- Audio: Clear narration with headset microphone
- Screen: Full desktop capture with highlighted code sections
- File: `TP3_Team_Screencast_1_CodeDemo.mp4`

#### Screencast 2: Vision-to-Design-to-Code Alignment (10%)
**Duration**: 8-10 minutes  
**Content**:
- Product vision and stakeholder needs explanation
- UML architecture diagrams (Astah-generated) walkthrough
- Detailed design mapping to implemented code
- JUnit test validation of design requirements

**Technical Specifications**:
- Format: MP4, 1080p resolution  
- Audio: Professional narration explaining design decisions
- Visuals: UML diagrams, code snippets, test execution
- File: `TP3_Team_Screencast_2_DesignAlignment.mp4`

#### Screencast 3: Standup Meeting Recordings (10%)
**Duration**: 6-8 minutes total (2-4 minutes per meeting)  
**Content**:
- Minimum 2 standup recordings for Week 10-11
- Team collaboration and issue resolution discussions
- Unplanned challenges and solution approaches
- Progress updates and next steps planning

**Technical Specifications**:
- Format: MP4, 720p minimum resolution
- Audio: Natural team conversation, audible for all participants
- Content: Authentic standup format with agenda and outcomes
- Files: `TP3_Standup_Week10_Meeting1.mp4`, `TP3_Standup_Week10_Meeting2.mp4`, etc.

---

## 📊 Code Consistency and Professional Standards (10%)

### Formatting and Style Consistency
- **Indentation**: 4 spaces, consistent throughout all Java files
- **Naming Conventions**: CamelCase for classes, camelCase for methods/variables
- **Documentation**: Comprehensive Javadoc matching existing HW3 style
- **Code Organization**: Package structure follows domain-driven design principles

### Author Consistency Verification
- **Coding Patterns**: Consistent use of builder patterns, enum types, and validation approaches
- **Error Handling**: Uniform exception handling and logging strategies
- **Test Structure**: Consistent test naming, setup, and assertion patterns
- **Documentation Style**: Matching tone, terminology, and technical depth

### Quality Assurance Checklist
- [ ] All code compiles without warnings using Java 24
- [ ] Javadoc generation produces clean HTML without errors
- [ ] Test execution passes with 100% success rate
- [ ] Code formatting consistent with existing HW3 codebase
- [ ] Documentation style matches established patterns

---

## 📁 Deliverable Summary

| Task | Deliverable | Status | Location |
|------|-------------|--------|----------|
| Task 1 | Implementation scope documentation | ✅ Complete | This document |
| Task 2 | Team plan, schedule, standup notes | ✅ Complete | `TP3_PLAN.md`, `TP3_STANDUP_NOTES.md` |
| Task 3 | JUnit test list and assignments | ✅ Complete | `TP3_TEST_PLAN.md` |
| Task 4 | Code implementation, UML, Javadoc | 🔄 In Progress | `src/`, `docs/uml/`, `docs/javadoc/` |
| Task 5 | GitHub URL and screencasts | 🔄 In Progress | Repository root, `screencasts/` |

**Next Steps**:
1. Complete UML diagram creation in Astah
2. Implement remaining test classes and execute full test suite
3. Generate final Javadoc documentation
4. Record and upload screencasts
5. Assemble final PDF submission

---

**Repository**: https://github.com/TheKingJunior17/Phase-2-Project  
**Submission Status**: TP3 Planning Complete, Implementation In Progress  
**Team Lead**: Jose Mendoza