@echo off
chcp 65001 >nul
echo Cleaning old database files...
echo.

cd /d "D:\Desktop\HotelBookingSystem-complete-backup(2)(1)(3)"

echo Stopping Java processes...
taskkill /f /im java.exe 2>nul
timeout /t 2 /nobreak >nul

echo Deleting old HotelBookingDB folder...
if exist HotelBookingDB (
    rmdir /s /q HotelBookingDB
    echo [OK] HotelBookingDB deleted
) else (
    echo [INFO] HotelBookingDB does not exist
)

echo Deleting derby.log...
if exist derby.log (
    del derby.log
    echo [OK] derby.log deleted
) else (
    echo [INFO] derby.log does not exist
)

echo.
echo Current database in use: database\hotel_booking_db
echo Cleanup completed!
echo.
pause