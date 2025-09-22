# Sweet-Shop-Management-System

A full-stack Sweet Shop Management System built using **Spring Boot** (backend) and **React** (frontend). This project follows **Test-Driven Development (TDD)** principles and showcases secure authentication, role-based access, and a responsive user interface for managing sweets and inventory.

---

🎯 Objective

This project was developed as part of the **TDD Kata: Sweet Shop Management System**, designed to test full-stack development skills including:

- RESTful API design
- Secure authentication with JWT
- Database integration and inventory logic
- Frontend SPA development
- Test-driven development workflows
- Responsible use of AI tools

---

🧱 Tech Stack

 🔙 Backend
- **Spring Boot** (Java)
- **JWT Authentication**
- **MySQL** Database
- **JPA/Hibernate**
- **JUnit & Mockito** for testing

 🔜 Frontend
- **React** (Vite)
- **Axios** for API calls
- **React Router**
- **Responsive UI** with custom styling

---

## 🔐 Core Features

✅ Authentication Endpoints
-POST http://localhost:8080/api/auth/register — Register a new user

-POST http://localhost:8080/api/auth/login — Login and receive JWT

🍭 Sweet Management (Protected)
-POST http://localhost:8080/api/sweets — Add a new sweet

-GET http://localhost:8080/api/sweets — View all sweets

-GET http://localhost:8080/api/sweets/search — Search sweets by name, category, or price

-PUT http://localhost:8080/api/sweets/{id} — Update sweet details

-DELETE http://localhost:8080/api/sweets/{id} — Delete sweet (Admin only)

📦 Inventory Management (Protected)
-POST http://localhost:8080/api/sweets/{id}/purchase — Purchase sweet (decrease quantity)

-POST http://localhost:8080/api/sweets/{id}/restock — Restock sweet (Admin only)

## 🖥️ Frontend Functionality

- 🔐 Login & Registration forms
- 🧁 Sweet listing dashboard
- 🔍 Search & filter sweets
- 🛒 Purchase button (disabled if quantity is zero)
- 🛠️ Admin panel for add/update/delete sweets
and still in development

## 🚀 How to Run Locally

### 🔧 Backend Setup
```bash
cd sweetshop-backend
# Configure MySQL in src/main/resources/application.properties
mvn spring-boot:run


🔧 Frontend Setup
bash
cd sweetshop-frontend
npm install
npm run dev
✅ Make sure postgresql is running and the database is created before starting the backend.




🧪 Test Report
Tests written using JUnit and Mockito for backend services and controllers. You can run them with:

bash
mvn test



✅ Coverage includes:

AuthService

SweetService

Purchase & Restock logic

Controller endpoints



🤖 My AI Usage
 I used AI tools to accelerate development and improve code quality. Here's how:

GitHub Copilot: Used to generate boilerplate for controllers, DTOs, and service layers. It helped autocomplete repetitive code and suggest method signatures.

ChatGPT : Used for:

Debugging JWT configuration issues

Structuring README.md professionally

Brainstorming API endpoint naming and search logic

Writing unit test templates and improving error handling

Reflection 
AI tools helped me stay focused, reduce boilerplate fatigue, and debug faster. I always reviewed and customized the generated code to ensure clarity, security, and maintainability. Every commit where AI was used includes a co-author trailer.




👨‍💻 Author
Shaik — Full-stack developer in training
📧 Email: nafeesa3110@gmail.com
Github: https://github.com/nafeesa1182


📜 License
This project is open-source and available under the MIT License.
