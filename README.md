<<<<<<< HEAD
<<<<<<< Updated upstream
# HotelBookingSystem
A GUI-based Java hotel booking system for COMP603 Program Design &amp; Construction course at CJLU. Developed with JavaFX and Apache Derby.

## 🛠️ Tech Stack
- **Language**: Java 21
- **GUI**: JavaFX
- **Database**: Apache Derby
- **IDE**: NetBeans 23
- **Build Tool**: Maven

## 👥 Team Members
- Xintong Wu - Backend & Database
- Jiayi Mo - GUI & Frontend  
- Yuting Chen - Testing & Integration

## 📋 Project Features
- User registration and authentication
- Hotel search and room availability check
- Booking management
- Payment processing (simulated)
- Admin dashboard

## 🚀 How to Run
1. Clone this repository
2. Open in NetBeans 23
3. Run `Main.java`

## 🧪 Testing

### Test Structure
- `DatabaseConnectionTest` - 测试数据库连接和驱动
- `UserServiceTest` - 测试用户认证、注册和密码验证
- `BookingServiceTest` - 测试预订相关功能和日期验证

### How to Run Tests
```bash
# 运行所有测试
mvn test

# 运行测试并生成覆盖率报告
mvn clean test jacoco:report

# 运行特定测试类
mvn test -Dtest=UserServiceTest

## 🧪 Testing & Quality Assurance

### Test Framework
- **Testing Framework**: JUnit 5
- **Coverage Tool**: JaCoCo
- **Build Tool**: Maven

### Test Structure
- `DatabaseConnectionTest` - 测试数据库连接类
- `UserServiceTest` - 测试用户服务功能
- `BookingServiceTest` - 测试预订服务功能  
- `PaymentServiceTest` - 测试支付服务实例化
- `AdminControllerTest` - 测试管理员控制器

### Test Coverage Report
**Current Coverage (Latest):**
- Overall: 6% (从 0% 提升)
- Instructions: 6%
- Branches: 0%
- Complexity: 13%
- Lines: 6%
- Methods: 16%
- Classes: 33%

**Package Coverage:**
- com.hotelbooking.controller: 42% ✅
- com.hotelbooking.service: 14% ✅
- com.hotelbooking: 0% ⚠️
- com.hotelbooking.util: 0% ⚠️

### How to Run Tests
```bash
# 运行所有测试
mvn test

# 运行测试并生成覆盖率报告
mvn clean test jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html

# 运行特定测试类
mvn test -Dtest=DatabaseConnectionTest
=======
# HotelBookingSystem
A GUI-based Java hotel booking system for COMP603 Program Design &amp; Construction course at CJLU. Developed with JavaFX and Apache Derby.

## 🛠️ Tech Stack
- **Language**: Java 21
- **GUI**: JavaFX
- **Database**: Apache Derby
- **IDE**: NetBeans 23
- **Build Tool**: Maven

## 👥 Team Members
- Xintong Wu - Backend & Database
- Jiayi Mo - GUI & Frontend  
- Yuting Chen - Testing & Integration

## 📋 Project Features
- User registration and authentication
- Hotel search and room availability check
- Booking management
- Payment processing (simulated)
- Admin dashboard

## 🚀 How to Run
1. Clone this repository
2. Open in NetBeans 23
3. Run `Main.java`
>>>>>>> Stashed changes
酒店预订系统 - 运行说明
=========================

系统要求：
1. JDK 21（必须）
   - 下载地址：https://www.oracle.com/java/technologies/downloads/
   - 安装后需要设置JAVA_HOME环境变量

2. 操作系统：Windows 10/11, macOS, Linux

运行方法（任选一种）：

方法1：一键运行（推荐）
---------------------
1. 确保已安装JDK 21
2. 双击运行项目中的 run.bat（Windows）或 run.sh（Mac/Linux）
3. 系统将自动编译并启动

方法2：使用NetBeans IDE
---------------------
1. 安装NetBeans 23
2. 打开项目文件夹
3. 找到 Main.java (在src/main/java/com/hotelbooking/)
4. 右键点击 → "Run File"

方法3：使用Maven命令
-----------------
1. 打开命令行
2. 进入项目目录
3. 运行：
   mvn clean javafx:run

方法4：直接运行JAR文件
-------------------
1. 先编译项目：
   mvn clean package
2. 运行：
   java --add-modules javafx.controls,javafx.fxml -jar target/HotelBookingSystem-1.0-SNAPSHOT.jar
=======
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

>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
