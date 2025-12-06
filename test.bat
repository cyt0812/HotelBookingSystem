@echo off
echo Testing database connection...
echo.

:: Change to project directory
cd /d "D:\Desktop\HotelBookingSystem-complete-backup(2)(1)(3)"

:: 列出所有可能的数据库文件夹
echo === Finding database folders ===
if exist HotelBookingDB echo ✅ Found old: HotelBookingDB
if exist database echo ✅ Found current: database
if exist data echo ✅ Found: data
if exist target/hotel_booking_db echo ✅ Found: target/hotel_booking_db

echo.
echo === Checking derby log ===
if exist derby.log echo ✅ Has derby.log file, size: %~z0 bytes

echo.
echo Press any key to exit...
pause > nul