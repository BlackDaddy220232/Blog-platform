<<<<<<< HEAD
@echo off
setlocal EnableDelayedExpansion

set /p PGPATH="Enter the path to PostgreSQL bin directory (e.g., C:\Program Files\PostgreSQL\14\bin): "
set /p db_user="Enter pgAdmin username: "
set /p db_password="Enter pgAdmin password: "


set properties_file=src\main\resources\application.properties

powershell -Command "(Get-Content '%properties_file%') | ForEach-Object { $_ -replace '^spring.datasource.username=.*', 'spring.datasource.username=%db_user%' } | Set-Content '%properties_file%'"

powershell -Command "(Get-Content '%properties_file%') | ForEach-Object { $_ -replace '^spring.datasource.password=.*', 'spring.datasource.password=%db_password%' } | Set-Content '%properties_file%'"




"%PGPATH%\psql" -U %db_user% -c "CREATE DATABASE \"BlogPlatform\";" -h localhost

if %ERRORLEVEL% NEQ 0 (
    echo Error creating the database.
    exit /b %ERRORLEVEL%
)

echo Database "BlogPlatform" created successfully.

REM Run mvn clean install
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo Error running mvn clean install.
    exit /b %ERRORLEVEL%
)



REM Run java -jar
java -jar target/blogPlatform-0.0.1-SNAPSHOT.jar

echo Application started.

pause
=======
@echo off
setlocal EnableDelayedExpansion

set /p PGPATH="Enter the path to PostgreSQL bin directory (e.g., C:\Program Files\PostgreSQL\14\bin): "
set /p db_user="Enter pgAdmin username: "
set /p db_password="Enter pgAdmin password: "


set properties_file=src\main\resources\application.properties

powershell -Command "(Get-Content '%properties_file%') | ForEach-Object { $_ -replace '^spring.datasource.username=.*', 'spring.datasource.username=%db_user%' } | Set-Content '%properties_file%'"

powershell -Command "(Get-Content '%properties_file%') | ForEach-Object { $_ -replace '^spring.datasource.password=.*', 'spring.datasource.password=%db_password%' } | Set-Content '%properties_file%'"




"%PGPATH%\psql" -U %db_user% -c "CREATE DATABASE \"BlogPlatform\";" -h localhost

if %ERRORLEVEL% NEQ 0 (
    echo Error creating the database.
    exit /b %ERRORLEVEL%
)

echo Database "BlogPlatform" created successfully.

REM Run mvn clean install
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo Error running mvn clean install.
    exit /b %ERRORLEVEL%
)



REM Run java -jar
java -jar target/blogPlatform-0.0.1-SNAPSHOT.jar

echo Application started.

pause
>>>>>>> origin/main
