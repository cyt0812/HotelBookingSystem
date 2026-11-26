# Group Evidence Index and Individual Contribution Records

**Project:** HotelBookingSystem — Group 16  
**Repository:** [cyt0812/HotelBookingSystem](https://github.com/cyt0812/HotelBookingSystem)  
**Submission commit:** [f52af3bf15a6c8a298112df5972f5edeaa5b8014](https://github.com/cyt0812/HotelBookingSystem/commit/f52af3bf15a6c8a298112df5972f5edeaa5b8014)

## Team Members

| Member | Student ID | GitHub account |
|---|---:|---|
| Yuting Chen (陈钰婷) | 25305418 | [`cyt0812`](https://github.com/cyt0812) |
| Xintong Wu (吴欣桐) | 25305392 | [`wxtgogo`](https://github.com/wxtgogo) |
| Jiayi Mo (莫佳怡) | 25305940 | [`Mcree1220`](https://github.com/Mcree1220) |

## Group Evidence Index

| Evidence | Link | Record and status |
|---|---|---|
| Tracked requirement | [Issue #4 — Invalid login credentials](https://github.com/cyt0812/HotelBookingSystem/issues/4) | Assigned to Jiayi Mo (`Mcree1220`). The issue remains open as of 26 November 2026; merged test work is recorded separately in PR #7. See the requirement traceability note below. |
| Repository cleanup change | [PR #5 — Chore/cleanup root](https://github.com/cyt0812/HotelBookingSystem/pull/5) | Submitted by Xintong Wu (`wxtgogo`), merged by Yuting Chen (`cyt0812`). The PR contains cleanup of generated, backup, and system files and changes to ignore rules. |
| Cleanup commit examples | [ee8b464](https://github.com/cyt0812/HotelBookingSystem/commit/ee8b464430c8806188b87c06738a5c7b49b80626), [912f1d2](https://github.com/cyt0812/HotelBookingSystem/commit/912f1d290467b5de75a7f1de367c08bf4715f12f), [142e6b5](https://github.com/cyt0812/HotelBookingSystem/commit/142e6b5bc78393cb75552b991f116555d4dce622) | Specific commits in the cleanup branch showing removal of generated or system files. The PR is the main review record. |
| Follow-up repository hygiene | [PR #9](https://github.com/cyt0812/HotelBookingSystem/pull/9) | Submitted by Yuting Chen (`cyt0812`) and merged. This follow-up removed remaining repository artifacts; it is separate from Xintong Wu’s cleanup in PR #5. |
| Team-member documentation update | [PR #6 — Update team member from Mcree to Jiayi Mo](https://github.com/cyt0812/HotelBookingSystem/pull/6) | Submitted by Jiayi Mo (`Mcree1220`) and merged by Yuting Chen (`cyt0812`). |
| Login controller tests | [PR #7 — LoginController unit tests](https://github.com/cyt0812/HotelBookingSystem/pull/7) | Submitted by Jiayi Mo (`Mcree1220`) and merged by Yuting Chen (`cyt0812`). This is evidence of Jiayi’s test implementation. |
| Booking controller tests | [PR #8 — BookingController unit tests](https://github.com/cyt0812/HotelBookingSystem/pull/8) | Submitted by Jiayi Mo (`Mcree1220`) and merged by Yuting Chen (`cyt0812`). The PR describes JUnit/Mockito tests and small testability changes. |
| CI run for submission commit | [GitHub Actions run #44491438344](https://github.com/cyt0812/HotelBookingSystem/actions/runs/44491438344) | Successful `Java CI with Maven` run on `master` at the submission commit. The job log records 420 tests, zero failures, zero errors, zero skips, a successful coverage check, and publication of the JaCoCo artifact. |
| Submission version | [Commit f52af3b](https://github.com/cyt0812/HotelBookingSystem/commit/f52af3bf15a6c8a298112df5972f5edeaa5b8014) | Exact commit identified in the README as the submission commit. |

## Requirement Traceability

[Issue #4](https://github.com/cyt0812/HotelBookingSystem/issues/4) asks for invalid login credentials to produce an error without calling SessionManager and for tests to pass in CI. [PR #7](https://github.com/cyt0812/HotelBookingSystem/pull/7) supplies the LoginController tests. The [test at the submission commit](https://github.com/cyt0812/HotelBookingSystem/blob/f52af3bf15a6c8a298112df5972f5edeaa5b8014/src/test/java/com/hotelbooking/controller/LoginControllerTest.java) calls `performLogin()`, asserts failure and the error message, and verifies that `SessionManager.login()` is never called. This is evidence for the login logic; it does not by itself demonstrate the complete JavaFX `handleLogin()` event path or the absence of every possible SessionManager call. The issue is still open, so this index does not mark its acceptance checklist as closed.

## Individual Contribution Records

### Yuting Chen (陈钰婷, 25305418, `cyt0812`)

Handled CI and build work, including maintaining the GitHub Actions workflow, recording baseline test results, configuring JaCoCo reports, and updating README testing instructions. Reviewed and approved cleanup PR #5, checked its CI results, and merged it; also merged PRs #6, #7, and #8. The CI run for the final submission commit succeeded: [GitHub Actions run #44491438344](https://github.com/cyt0812/HotelBookingSystem/actions/runs/44491438344).

### Xintong Wu (吴欣桐, 25305392, `wxtgogo`)

Cleaned generated, backup, and system files from the repository, updated ignore rules, and submitted the cleanup change through [PR #5](https://github.com/cyt0812/HotelBookingSystem/pull/5). Prepared and wrote this evidence index and contribution record, collecting the group’s links to requirements, changes, CI, and submission commits, along with each member’s completed work.

### Jiayi Mo (莫佳怡, 25305940, `Mcree1220`)

Implemented unit tests for LoginController and BookingController using JUnit 5 and Mockito, and made the small testability changes needed for those tests. The implementations are in [PR #7](https://github.com/cyt0812/HotelBookingSystem/pull/7) and [PR #8](https://github.com/cyt0812/HotelBookingSystem/pull/8). Jiayi also submitted [PR #6](https://github.com/cyt0812/HotelBookingSystem/pull/6) to update the team-member information.

## Verification Notes

- PRs #5, #6, #7, and #8 are recorded as merged; their authors and merger are visible in GitHub.
- CI run #44491438344 succeeded on `master` at commit `f52af3bf15a6c8a298112df5972f5edeaa5b8014`.
- The README identifies `f52af3b` as the submission commit and links the CI run.
- Verification date: 26 November 2026. The current `master` head at inspection was `0a3d1e0eed29df5c42dedd9ede4633598e2037d7` (merged documentation PR #13); the README-designated submission commit remains `f52af3bf15a6c8a298112df5972f5edeaa5b8014`. These are distinct references.
- The complete current `master` file tree was inspected (156 entries, not truncated). It contains no tracked `target/` or `database/` directory, `*.class`, `*.backup`, `.DS_Store`, `desktop.ini`, or `derby.log`. The [ignore rules at the inspected master commit](https://github.com/cyt0812/HotelBookingSystem/blob/0a3d1e0eed29df5c42dedd9ede4633598e2037d7/.gitignore) cover these generated and local files. Shared NetBeans settings remain tracked.
- QA evidence links to the [CI workflow](https://github.com/cyt0812/HotelBookingSystem/blob/f52af3bf15a6c8a298112df5972f5edeaa5b8014/.github/workflows/maven.yml) and [Maven coverage gate](https://github.com/cyt0812/HotelBookingSystem/blob/f52af3bf15a6c8a298112df5972f5edeaa5b8014/pom.xml): CI executes `xvfb-run -a mvn -B clean verify`, and JaCoCo enforces at least 60% overall line coverage. The test results above were verified from GitHub Actions logs, not from a local rerun.
- Repository publication should be checked against the documentation change’s own commit and CI run. Course-platform submission and compliance with a grading rubric not included in the repository have not been verified.
