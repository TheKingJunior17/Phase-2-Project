TP3 Implementation Plan

Purpose
- Provide a focused plan to implement only the user stories required by the TP3 phase deliverables (documentation, testing, screencasts, upload, UML/Javadoc).

Team & responsibilities (suggested)
- Jose Mendoza (Lead): overall integration, PDF assembly, GitHub permissions, Screencast 1
- <Member B> (Developer): implement test automation, JUnit tests, run evidence
- <Member C> (Designer): architecture updates, UML diagrams (Astah), Javadoc verification
- <Member D> (QA): run test plan, prepare test evidence, record standup meetings

Timeline (example, adapt to remaining calendar days)
- Day 1: Finalize selected user stories, assign owners, create skeleton docs (today)
- Day 2: Implement test automation scripts, run and fix tests; begin Javadoc generation
- Day 3: Create UML diagrams (Astah) and update architecture docs; run integration tests
- Day 4: Record screencast 1 (code & tests), screencast 2 (vision->design->tests)
- Day 5: Collect standup recordings, finalize PDF, upload to GitHub, verify Canvas submission

Risk register (top risks and mitigations)
- Missing vision/user stories in repo: Mitigation - create minimal assumptions and confirm with team immediately.
- Javadoc/JDK compatibility issues: Mitigation - use Gradle javadoc task with proper --add-exports if needed and test locally early.
- GitHub access/permissions for grader: Mitigation - verify grader username and add them to repo with read access early.
- Astah license or file format issues: Mitigation - export diagrams as PNG/SVG for inclusion.
- Screencast audio/video quality problems: Mitigation - perform quick rehearsals and record short takes; allow time for re-recording.

Communication and integration checkpoints
- Daily standups (15 minutes) at agreed time — include date/time stamps and short notes in `TP3_STANDUP_NOTES.md`.
- Mid-phase integration checkpoint after JUnit tests pass for assigned features.
- Final full-run test and Javadoc generation 24 hours before deadline.

Acceptance criteria for phase
- Selected user stories implemented minimally and verified by tests.
- All Javadoc for the application and tests generated and included.
- Three screencasts recorded and uploaded in `HW3/screencasts/`.
- Final PDF assembled and submitted to Canvas with peer evaluations completed.

What I will implement now (initial actions)
- Create these TP3 docs in `HW3/` and a `HW3/screencasts/` directory placeholder.
- Provide run/test commands for Windows PowerShell and Gradle.

