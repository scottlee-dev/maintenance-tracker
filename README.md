# 🚗 Vehicle Maintenance Tracker API

![Java](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.4-blue.svg)
![OpenAPI](https://img.shields.io/badge/OpenAPI%20(Swagger)-3.0-green.svg)

This is an enterprise-grade RESTful API built using **Spring Boot** and **PostgreSQL**. It is designed to record vehicle maintenance history and automatically calculate maintenance schedules based on mileage.

The project implements core backend engineering practices, including Clean Layered Architecture, immutable dependency injection, relational database modeling, and automated data seeding for testing.

---

## 💡 Core Business Logic
Unlike simple CRUD applications, this API implements the following dynamic calculation engines:

1. Updates real-time vehicle mileage.

2. Compares past maintenance records against system-defined maintenance rules (e.g., engine oil change every 8,000 miles).

3. Automatically calculates remaining mileage and indicates vehicles requiring maintenance (`isDue: true`).
---

## 🛠️ Engineering Highlights & Architectural Decisions

### 1. Constructor Injection over Field Injection
* Utilizes **constructor injection** via Lombok's `@RequiredArgsConstructor`.
* **Reason:** This prevents potential `NullPointerException`s at runtime and enables rapid testing using mock objects without the need to start the entire Spring Context.

### 2. DTO (Data Transfer Object) Pattern
* Protects JPA entities from exposure via external APIs by using a dedicated DTO (`MaintenanceStatusDto`).
* Encapsulates the database schema, prevents data transmission vulnerabilities, and handles JSON payload serialization.

### 3. Automated Data Seeding Pipeline
*Implemented Spring Boot's `CommandLineRunner` to automatically inject dummy data (vehicles, rules, logs) upon application startup.
* Established an environment where developers and reviewers can immediately conduct tests without additional configuration.

---

## 🗄️ Database Schema

| Table | Description | Key Columns |
| :--- | :--- | :--- |
| **`vehicle`** | Stores core vehicle details and current odometer reading. | `id`, `make`, `model`, `model_year`, `current_mileage` |
| **`maintenance_rule`** | Master table defining service intervals for specific parts. | `id`, `part_name`, `interval_miles`, `interval_months` |
| **`maintenance_log`** | Historical event records tied to a specific vehicle via Foreign Key. | `id`, `vehicle_id`, `part_name`, `service_date`, `mileage_at_service` |

---

## 🚀 Getting Started & Local Setup

### Prerequisites
* JDK 21 or 25
* PostgreSQL installed and running on port `5432`

### 1. Database Configuration
Create an empty database named `maintenance_db` in PostgreSQL:
```sql
CREATE DATABASE maintenance_db;