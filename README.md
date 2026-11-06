# HotelBookingSystem
A GUI-based Java hotel booking system for COMP603 Program Design & Construction course at CJLU. Developed with JavaFX and Apache Derby.

## 🛠️ Tech Stack

| Technology | Version | Description |
|------------|---------|-------------|
| Java | 21 | Programming language |
| JavaFX | 21 | GUI framework |
| Apache Derby | 10.15 | Embedded database |
| NetBeans | 23 | Integrated development environment |
| Maven | 3.9+ | Build and dependency management |
| JUnit | 5 | Testing framework |
| Mockito | 5 | Mocking library |
| JaCoCo | 0.8.10 | Code coverage tool |

## 👥 Team Members

- **Xintong Wu** - Backend & Database Development
- **Jiayi Mo** - GUI & Frontend Development
- **Yuting Chen** - Testing & Integration

## 📋 Project Features

### User Management
- User registration with validation
- Secure authentication system
- Role-based access control (Admin, Hotel Manager, Customer)

### Hotel & Room Management
- Hotel search functionality
- Real-time room availability check
- Room type classification (Single, Double, Suite, etc.)

### Booking System
- Easy booking creation and management
- Booking history tracking
- Booking cancellation and modification

### Payment Processing
- Secure payment simulation
- Transaction history
- Invoice generation

### Admin Dashboard
- User management
- Hotel and room management
- Booking statistics and reporting
- Real-time database monitoring

## 🚀 How to Run

### Option 1: Using NetBeans IDE (Recommended)

1. Clone this repository:
   ```bash
   git clone https://github.com/cyt0812/HotelBookingSystem.git
   ```

2. Open NetBeans 23

3. Select **File → Open Project**

4. Navigate to the project folder and open it

5. Right-click on the project and select **Run**

### Option 2: Using Maven (Command Line)

1. Clone this repository:
   ```bash
   git clone https://github.com/cyt0812/HotelBookingSystem.git
   ```

2. Navigate to the project directory:
   ```bash
   cd HotelBookingSystem
   ```

3. Compile the project:
   ```bash
   mvn clean compile
   ```

4. Run the application:
   ```bash
   mvn javafx:run
   ```

### Option 3: Using JAR File

1. Build the JAR file:
   ```bash
   mvn clean package
   ```

2. Run the JAR file:
   ```bash
   java --add-modules javafx.controls,javafx.fxml -jar target/HotelBookingSystem-1.0-SNAPSHOT.jar
   ```

## 🧪 Testing

### Test Framework

- **Testing Framework**: JUnit 5
- **Mocking Library**: Mockito 5
- **Code Coverage**: JaCoCo

### How to Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with code coverage
mvn clean test jacoco:report

# Format the JaCoCo report for better readability
python format_jacoco_report.py

# View coverage report
# Open target/site/jacoco/index.html in a browser
```

### Test Coverage

**Current Coverage:**
- Overall: 38%
- Instructions: 38%
- Branches: 34%
- Complexity: 39%
- Lines: 38%
- Methods: 53%
- Classes: 64%

**Package Coverage:**
- `com.hotelbooking.dto`: 100%
- `com.hotelbooking.validation`: 96%
- `com.hotelbooking.util`: 81%
- `com.hotelbooking.exception`: 78%
- `com.hotelbooking.entity`: 76%
- `com.hotelbooking.service`: 68%
- `com.hotelbooking.dao`: 56%
- `com.hotelbooking`: 20%
- `com.hotelbooking.controller`: 7% (needs improvement)

## 📁 Project Structure

```
HotelBookingSystem/
├── src/main/java/com/hotelbooking/
│   ├── controller/          # MVC Controllers (Logic for UI)
│   ├── dao/                 # Data Access Objects
│   ├── dto/                 # Data Transfer Objects
│   ├── entity/              # Domain Entities
│   ├── exception/           # Custom Exceptions
│   ├── service/             # Business Logic Services
│   ├── util/                # Utility Classes
│   │   ├── DatabaseConnection.java    # Database Connection Manager
│   │   ├── DatabaseInitializer.java   # Database Setup
│   │   ├── NavigationManager.java     # Scene Navigation
│   │   └── SessionManager.java        # User Session Management
│   ├── validation/          # Input Validation
│   └── Main.java            # Application Entry Point
├── src/main/resources/      # Resources Root
│   ├── view/                # GUI Layout Files (Views)
│   └── assets/              # CSS Stylesheets & Images
├── src/test/java/com/hotelbooking/
│   ├── test/                # Test Utilities
│   ├── controller/          # Controller Tests
│   ├── dao/                 # DAO Tests
│   ├── dto/                 # DTO Tests
│   ├── entity/              # Entity Tests
│   ├── service/             # Service Tests
│   ├── util/                # Utility Tests
│   ├── validation/          # Validation Tests
│   └── integration/         # Integration Tests
├── database/                # Embedded Derby Database Files
├── src/test/resources/      # Test Resources
├── target/                  # Build Output Directory
├── pom.xml                  # Maven Configuration
├── format_jacoco_report.py  # JaCoCo Report Formatter
└── README.md                # Project Documentation
```

## 🔧 Requirements

- **JDK**: 21 or later
- **Maven**: 3.9+ (for command line execution)
- **NetBeans**: 23 (recommended for easiest setup)
- **Operating System**: Windows 10/11, macOS 10.15+, or Linux

## 🗄️ Database Configuration

### Embedded Derby Setup

The system uses embedded Apache Derby database:

- **Database Location**: `./database/hotel_booking_db`
- **Connection URL**: `jdbc:derby:./database/hotel_booking_db;create=true`
- **Driver**: `org.apache.derby.jdbc.EmbeddedDriver`
- **Auto-creation**: Enabled (`create=true`)

### Sample Data

The system includes pre-configured sample data:

- **Users**: Admin, hotel managers, and customer accounts
- **Hotels**: 3 sample hotels with detailed descriptions
- **Rooms**: 7 sample rooms with different types and prices

### Viewing Database Content

To view data in NetBeans:

1. Connect to the database as described above
2. Expand **Tables** in the Services window
3. Right-click any table → **View Data**

## 🎯 Project Goals

- Build a fully functional hotel booking system using modern Java technologies
- Implement proper MVC architecture
- Ensure high test coverage for critical components
- Create an intuitive and user-friendly GUI
- Demonstrate good software engineering practices

---

**Last Updated**: 2025-12-07
