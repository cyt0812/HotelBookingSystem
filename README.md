# HotelBookingSystem
A GUI-based Java hotel booking system for COMP603 Program Design & Construction course at CJLU. Developed with JavaFX and Apache Derby.

# 🛠️ Tech Stack

Language: Java 21

GUI: JavaFX

Database: Apache Derby

IDE: NetBeans 23

Build Tool: Maven

# 👥 Team Members

Xintong Wu - Backend & Database

Jiayi Mo - GUI & Frontend

Yuting Chen - Testing & Integration

# 📋 Project Features

User registration and authentication

Hotel search and room availability check

Booking management

Payment processing (simulated)

Admin dashboard

# 🚀 How to Run

Option 1: Using NetBeans IDE (Recommended)

- Clone this repository:

  bash

  git clone https://github.com/cyt0812/HotelBookingSystem.git
  
- Open NetBeans 23

- Select File → Open Project

- Navigate to the project folder and open it

- Right-click on the project and select Run

Option 2: Using Maven (Command Line)

bash

- Clone this repository
  
  git clone https://github.com/cyt0812/HotelBookingSystem.git

- Navigate to project directory:
  
  cd HotelBookingSystem

- Compile the project:
  
  mvn clean compile

- Run the application:
  
  mvn javafx:run
  
# 🧪 Testing

Test Framework

Testing Framework: JUnit 5

Mocking: Mockito

How to Run Tests

bash

- Run all tests:
  
  mvn test

- Run specific test class
  
  mvn test -Dtest=UserServiceTest

- Run tests with coverage (if Jacoco configured):
  
  mvn clean test jacoco:report
  
# 📁 Project Structure

text

HotelBookingSystem/

├── src/main/java/com/hotelbooking/

│   ├── controller/     # MVC Controllers

│   ├── dao/           # Data Access Objects

│   ├── entity/        # Domain Entities

│   ├── service/       # Business Logic

│   ├── util/          # Utility Classes

│   └── view/          # FXML Views

├── src/test/java/     # Unit Tests

├── pom.xml            # Maven Configuration

└── README.md          # This file

# 🔧 Requirements

JDK 21 or later

Maven 3.9+ (for command line execution)

NetBeans 23 (recommended for easiest setup)

