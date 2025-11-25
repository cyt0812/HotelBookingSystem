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