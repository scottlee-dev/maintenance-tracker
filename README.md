

# 🚗 Enterprise Vehicle & Fleet Maintenance Tracking SaaS

![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3%2B-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-blue.svg)
![Frontend](https://img.shields.io/badge/Frontend-Vanilla%20JS%20%2F%20CSS%20Grid-yellow.svg)
![Deploy Status](https://img.shields.io/badge/Cloud%20Deploy-Render%20Live-success.svg)

A full-stack RESTful SaaS application engineered to solve automotive maintenance tracking for vehicle owners.

It was built using Spring Boot, PostgreSQL, and an integrated frontend dashboard based on Vanilla JavaScript and CSS Grid.
The platform calculates maintenance schedules by analyzing mileage and fuel type.

---

## 🚀 Getting Started & Local Setup

You can clone and run this application locally on your machine in under 3 minutes without configuring complex external frontend dependencies.

### Prerequisites
* **JDK 21** or newer
* **PostgreSQL** 
* **Git** & **Maven**

### 1. Database Configuration
Open your PostgreSQL terminal (or pgAdmin/DBeaver) and create an empty database named `maintenance_db`:

```sql
CREATE DATABASE maintenance_db;

```

### 2. Configure Application Properties

Verify or update your database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/maintenance_db
spring.datasource.username=postgres
spring.datasource.password=your_password_here
spring.jpa.hibernate.ddl-auto=update

```

### 3. Clone, Build, and Run

Open your terminal and execute the following commands:

```bash
# Clone the repository
git clone https://github.com/scottlee-dev/maintenance-tracker-api.git
cd maintenance-tracker-api

# Build and start the Spring Boot server
./mvnw clean spring-boot:run

```

Once the application finishes booting, open your browser and navigate to:
👉 **[http://localhost:8080/](http://localhost:8080/)** to access the Integrated SaaS Dashboard!

---

## 📺 Live Cloud Demo & Video Walkthrough

### 🎥 1-Minute Video Demonstration

Don't have time to spin up a local instance? Watch the full architectural walkthrough and UI demonstration on YouTube:
👉 **[Watch YouTube Walkthrough & Architecture Demo](https://youtu.be/ddNzAs8IrRo)** 

### 🌐 Live Cloud Instance

Experience the cloud-deployed application and interact with the REST API directly in your browser:

* **Interactive SaaS Dashboard: [https://maintenance-tracker-api-ruyc.onrender.com/](https://maintenance-tracker-api-ruyc.onrender.com/)
* **OpenAPI / Swagger UI:** [https://maintenance-tracker-api-ruyc.onrender.com/swagger-ui/index.html](https://maintenance-tracker-api-ruyc.onrender.com/swagger-ui/index.html)

*(Note: Hosted on a free Render cloud instance. Initial requests may take 30–50 seconds to spin up from sleep mode.)*

---

## 💡 Core Domain & Business Logic

1. **Drivetrain Categorization (`GASOLINE`, `EV`, `HYBRID`, `DIESEL`):** When registering a vehicle, users assign a specific drivetrain.
2. **Automated Rule Tagging & Filtering:** Maintenance rules are applied based on the corresponding fuel type.

   For example:
* *Oil Changes (8,000 mi rule)* and *Spark Plugs (80,000 mi rule)* apply to **GASOLINE** and **HYBRID** powertrains.
* *High-Voltage Battery & Coolant Checks (20,000 mi rule)* apply to **EVs** (e.g., Tesla Model Y).
* *Tire Rotations* and *Brake Pads* apply globally (**ALL**).


3. **Overdue Calculation:** Calculate the remaining driving distance on the backend and enable the frontend to notify the user when the vehicle's driving distance is exceeded (e.g., ⚠️ over 4,000 miles).
---

## 🛠️ Engineering Highlights & Architectural Decisions

### 1. Constructor Injection over Field Injection
* Uses only **constructor injection** with Lombok's `@RequiredArgsConstructor`.
* **Reason:** To ensure class immutability and prevent potential `NullPointerException`s at runtime.
### 2. DTO (Data Transfer Object) Pattern

* Maps calculation results to a DTO (`MaintenanceStatusDto`) to prevent internal JPA entities from being exposed externally.
* Encapsulates the design to prevent mass-assignment security vulnerabilities.


---

## 🗄️ Relational Database Schema

| Table | Description                                                               | Key Columns |
| --- |---------------------------------------------------------------------------| --- |
| **`vehicle`** | Stores core vehicle details and current odometer reading.                 | `id`, `make`, `model`, `model_year`, `current_mileage`, `fuel_type` |
| **`maintenance_rule`** | A master table that defines the maintenance intervals for specific parts. | `id`, `part_name`, `interval_miles`, `interval_months`, `fuel_type` |
| **`maintenance_log`** | Maintenance events linked via Foreign Key.                                | `id`, `vehicle_id`, `part_name`, `service_date`, `mileage_at_service`, `cost` |

---

## 👨‍💻 Author

**Scott Lee**

* Software Engineer | Systems & Backend Architecture
* Connect with me on [LinkedIn](https://www.linkedin.com/in/scott-lee-dev/) | [GitHub](https://github.com/scottlee-dev)

