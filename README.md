# HotelBookingSystem

![Java CI with Maven](https://github.com/cyt0812/HotelBookingSystem/actions/workflows/maven.yml/badge.svg)

A GUI-based Java hotel booking system built with Java 21, JavaFX 21, and Apache Derby.

This repository is an extension of an existing course project. Our team's focus for this iteration is **improving test coverage (especially the controller layer)** and **establishing a robust collaborative CI/CD workflow**.

## 👥 Team Members & Roles

*   **Yuting Chen** — CI & Build Owner (CI pipelines, branch protection, build configurations)
*   **Jiayi Mo** — Test Developer (Controller unit tests, minimal testability refactoring)
*   **Xintong Wu** — QA & Evidence Owner (Repository hygiene, trial change, evidence indexing)

## ⚙️ CI Workflow & Collaboration (New)

We have implemented a **GitHub Actions CI pipeline** and configured **Branch Protection** on the `master` branch to enforce software quality.

*   **Continuous Integration:** Every Pull Request targeting the `master` branch triggers an automated `mvn clean test` run.
*   **Quality Gate:** Pull Requests cannot be merged if the CI pipeline fails.
*   **Trial Change Evidence:** For an example of our workflow, review <https://github.com/cyt0812/HotelBookingSystem/pull/5>, where we cleaned up non-source files, verified by CI.

## 🧪 Testing & Coverage (New)

We use **JUnit 5** and **Mockito** for unit testing. To ensure code quality, we are actively writing tests for the `controller` layer.

*   **Run Tests:** `mvn clean test`
*   **Generate Coverage Report:** `mvn clean test jacoco:report`
    *(View the report at `target/site/jacoco/index.html`)*

---

## 🛠️ Tech Stack (Original)

| Technology | Version | Description |
|---|---|---|
| Java | 21 | Programming language |
| JavaFX | 21 | GUI framework |
| Apache Derby | 10.15 | Embedded database |
| NetBeans | 23 | Integrated development environment |
| Maven | 3.9+ | Build and dependency management |
| JUnit | 5 | Testing framework |
| Mockito | 5 | Mocking library |
| JaCoCo | 0.8.10 | Code coverage tool |

##  Project Features (Original)

### User Management

*   User registration with validation
*   Secure authentication system
*   Role-based access control (Admin, Hotel Manager, Customer)

### Hotel & Room Management

*   Hotel search functionality
*   Real-time room availability check
*   Room type classification (Single, Double, Suite, etc.)

### Booking System

*   Easy booking creation and management
*   Booking history tracking
*   Booking cancellation and modification

### Payment Processing

*   Secure payment simulation
*   Transaction history
*   Invoice generation

### Admin Dashboard

*   User management
*   Hotel and room management
*   Booking statistics and reporting
*   Real-time database monitoring

## 🚀 How to Run (Original)

### Option 1: Using NetBeans IDE (Recommended)

1. Clone this repository: `git clone https://github.com/cyt0812/HotelBookingSystem.git`
2. Open NetBeans 23 and select **File → Open Project**.
3. Navigate to the project folder and open it.
4. Right-click on the project and select **Run**.

### Option 2: Using Maven (Command Line)

1. Navigate to the project directory: `cd HotelBookingSystem`
2. Compile the project: `mvn clean compile`
3. Run the application: `mvn javafx:run`

## 📁 Project Structure (Original)

```text
HotelBookingSystem/
├── src/main/java/com/hotelbooking/
│   ├── controller/          # MVC Controllers (Focus of current test improvements)
│   ├── dao/                 # Data Access Objects
│   ├── dto/                 # Data Transfer Objects
│   ├── entity/              # Domain Entities
│   ├── exception/           # Custom Exceptions
│   ├── service/             # Business Logic Services
│   ├── util/                # Utility Classes
│   └── validation/          # Input Validation
├── src/main/resources/      # GUI Layout Files & Assets
├── src/test/java/com/hotelbooking/  # Unit tests
├── .github/workflows/       # GitHub Actions CI configuration
├── database/                # Embedded Derby Database Files
├── pom.xml                  # Maven Configuration
└── README.md
```

## ️ Database Configuration (Original)

The system uses an embedded Apache Derby database:

*   **Database Location:** `./database/hotel_booking_db`
*   **Connection URL:** `jdbc:derby:./database/hotel_booking_db;create=true`
*   **Driver:** `org.apache.derby.jdbc.EmbeddedDriver`

(Note: Runtime files like `derby.log` and build artifacts are now properly ignored via `.gitignore` after our cleanup.)

**Last Updated:** November 2026 (Assignment 1)
