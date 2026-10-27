# Crumb – Bakery & Custom Cake Booking Platform

Crumb is a bakery ordering and custom cake booking web application developed for the SE1020 Object-Oriented Programming module.

## Features

- Browse bakery products and view product details
- Design custom cakes with pricing previews
- Place orders and track order status
- Schedule pickup or delivery bookings
- Manage customer accounts
- Process payments and view receipts
- Submit and manage customer reviews
- Admin dashboard for managing products, orders, bookings, and customers

## Tech Stack

- **Backend:** Java 17, Spring Boot 3
- **Database:** MySQL 8, Spring Data JPA, Hibernate
- **Frontend:** HTML, CSS, JavaScript
- **Build Tool:** Maven
- **Other:** Lombok

## Prerequisites

Install or configure the following:

- Java 17
- MySQL 8.0 or later
- IntelliJ IDEA
- Maven (optional if using the included Maven Wrapper)

## Setup and Run

Choose either the IntelliJ IDEA GUI method or the terminal method.

### Option A: Run Using IntelliJ IDEA GUI

1. **Open the project**
    - Launch IntelliJ IDEA.
    - Select **File → Open**.
    - Select the project folder containing `pom.xml`.
    - Wait for IntelliJ IDEA to import the Maven project and download dependencies.

2. **Configure Java**
    - Open **File → Project Structure → Project**.
    - Set the Project SDK to **Java 17**.
    - If Java 17 is not listed, add your installed JDK.

3. **Create the database**
    - Start your MySQL server.
    - Open MySQL Workbench or IntelliJ's Database tool window.
    - Run:
      ```sql
      CREATE DATABASE crumb_app;
      ```

4. **Configure the database connection**
    - Open `src/main/resources/application.properties`.
    - Set the JDBC URL, username, and password for your local MySQL installation.
    - Example URL:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/crumb_app
      spring.datasource.username=YOUR_MYSQL_USERNAME
      spring.datasource.password=YOUR_MYSQL_PASSWORD
      ```
    - Use your actual local credentials. Do not commit passwords or other secrets to GitHub.
    - Keep any other required properties already present in the project's configuration.

5. **Run the application**
    - Open the main Spring Boot class, usually `CrumbApplication.java`.
    - Click the green **Run** triangle beside the class or its `main` method.
    - Wait until the Run window indicates that the application has started.

6. **Open the website**
    - Storefront: http://localhost:8080
    - Admin portal: http://localhost:8080/admin.html

### Option B: Run Using the Terminal

1. **Open a terminal in the project root** (the folder containing `pom.xml`).

2. **Create the database** in MySQL, if it does not already exist:
   ```sql
   CREATE DATABASE crumb_app;
   ```

3. **Configure the database**
    - Edit `src/main/resources/application.properties`.
    - Set the JDBC URL, username, and password for your local MySQL installation.
    - Example:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/crumb_app
      spring.datasource.username=YOUR_MYSQL_USERNAME
      spring.datasource.password=YOUR_MYSQL_PASSWORD
      ```

4. **Start the application** using the Maven Wrapper:

   **Windows PowerShell**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **macOS/Linux**
   ```bash
   ./mvnw spring-boot:run
   ```

   Or, if Maven is installed:
   ```bash
   mvn spring-boot:run
   ```

5. **Open the website**
    - Storefront: http://localhost:8080
    - Admin portal: http://localhost:8080/admin.html

### Troubleshooting

- **Java version error:** Confirm IntelliJ IDEA and the terminal are using Java 17.
- **Database connection error:** Confirm MySQL is running and the database URL, username, and password are correct.
- **Port 8080 is already in use:** Stop the other application using the port or configure a different server port.
- **Maven dependency errors:** Reload the Maven project in IntelliJ IDEA, or try running the Maven Wrapper again.

## Project Structure

The application follows a layered Spring Boot structure, with separate components for customers, products, custom cakes, orders, bookings, and payments/reviews. Components are organized around controllers, services, repositories, entities, and DTOs where applicable.

## Team

Developed by a team of six students, with responsibilities divided across the application's main components.

## Academic Project

Created for **SE1020 – Object-Oriented Programming**.
