TP3 - Selected User Stories (student / reviewer / instructor)

Context & assumptions
- The repository did not contain an explicit "product vision" or a formal user-stories file for TP3; to make progress we assume the product is the HW application used in previous phases (question submission, authentication, role-based access) and the new phase (TP3) focuses on expanding automated validation, documentation, and deliverables for grading.
- If your team prefers different wording, replace the story text below; these are minimal stories needed to meet the phase deliverables.

User role: Student
US-1: As a student, I want to run the automated test suite so I can validate my changes before submission.
Acceptance criteria:
- A Gradle/IDE task or script runs all JUnit tests and reports pass/fail and coverage summaries.
- Test runs recorded in the evidence document and screencast.

US-2: As a student, I want to generate professional Javadoc for the application and the tests so that graders can review design and intent.
Acceptance criteria:
- Javadoc generated to `HW3/docs/javadoc` and included in repository docs.
- Screenshots or links to the generated docs are included in the PDF submission and screencast.

US-3: As a student, I want to produce three screencasts (code walkthrough, alignment of vision->design->tests, standup recordings) and upload them to our team GitHub so graders can download them for evaluation.
Acceptance criteria:
- Three MP4/WebM files placed in `HW3/screencasts/` and referencing them in the PDF.
- GitHub repo permissions allow grader access.

User role: Reviewer (TA / grader)
US-4: As a reviewer, I want to see a PDF that documents all deliverables (vision mapping, test evidence, architecture, standups) so that I can grade efficiently.
Acceptance criteria:
- The PDF generated from provided MS Word template includes all required artifacts and evidence.

US-5: As a reviewer, I want to run the application and tests locally when needed so I can reproduce results.
Acceptance criteria:
- README includes simple copy-paste commands for Windows PowerShell and Gradle to build/run and run tests.

User role: Instructor
US-6: As an instructor, I want the team to identify and manage risks early so that integrations finish before the deadline.
Acceptance criteria:
- TP3 plan includes risk list and mitigations, assigned owners, and timeline with buffer.

Scope for this phase (minimal implementation)
- Implement: automation of test execution and Javadoc generation scripts, a tested subset of features required by the selected stories (e.g., extend validation or add small enhancements), documentation artifacts and standup logs, upload screencasts.
- Exclude: large new feature development unless strictly necessary to satisfy a chosen user story.

Mapping of stories -> deliverables
- US-1 -> `TP3_TEST_PLAN.md`, `docs/TEST_EXECUTION_EVIDENCE.md`, `HW3/README.md` (run commands)
- US-2 -> Javadoc generation command and `HW3/docs/javadoc/` output linked in PDF
- US-3 -> `HW3/screencasts/` directory and `TP3_SCREencast_README.md`
- US-4 -> Final PDF containing all artifacts
- US-5 -> `HW3/README.md` updated with run instructions
- US-6 -> `TP3_PLAN.md` (responsibilities, timeline, risks)

Notes for next steps
- Team should confirm the student name(s), GitHub grader access, and whether Astah files will be included in repo or provided separately. If Astah is not available, export UML diagrams as PNG from the tool and include them in `HW3/docs/uml/`.
