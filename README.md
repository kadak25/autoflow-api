# 🚀 AutoFlow – Workflow Automation Platform

AutoFlow is a lightweight workflow automation platform that allows users to
create, manage, and execute chained automation workflows such as:

HTTP → AI → Custom Steps

Inspired by tools like Zapier and n8n, AutoFlow focuses on simplicity,
extensibility, and operational clarity.

Beyond its functional scope, the project is designed with a strong emphasis on
production readiness, failure handling, and supportability.

---

## ✨ Core Features

- User authentication and authorization (JWT)
- Workflow creation and management
- Multiple step types within a workflow:
  - HTTP requests
  - AI summarization
  - Custom processing steps
- Dynamic context passing between workflow steps
- Workflow execution with result inspection
- Dashboard-style user interface

---

## 🧠 Production & Support Perspective

This project is built to simulate real-world application support and production
operations:

- External dependency handling (HTTP services, AI integrations)
- Graceful failure handling for individual workflow steps
- Clear execution flow and error visibility per step
- Structured logging for troubleshooting failed executions
- Secure and isolated execution of user-defined workflows
- Environment-based configuration for development and production setups

The system is designed to allow operators to quickly identify where and why
a workflow execution failed, reflecting realistic production incident scenarios.

---

## 🧱 Technical Stack

### Backend
- Java 17
- Spring Boot
- Spring Security + JWT
- JPA / Hibernate
- PostgreSQL (H2 for local development)
- Docker-ready architecture

### Frontend
- React
- TypeScript
- Vite
- Axios

---

## ⚙️ Architecture Overview

Client (React)  
→ Spring Boot REST API  
→ Workflow Execution Engine  
→ PostgreSQL  

This architecture enables clear separation of concerns and easier operational
support when failures occur during workflow execution.

---

## 🖥️ Screenshots

<img width="1317" height="796" alt="544260679-767e91ba-cba4-4a56-814b-fac58a49389c" src="https://github.com/user-attachments/assets/ab7cc702-a34c-4f7e-923a-0c2388f8804f" />
<img width="1263" height="736" alt="544260872-2a708526-58be-4797-a77e-f6c503210774" src="https://github.com/user-attachments/assets/a61f5b1d-3c29-492b-9e6b-471b9803959d" />

---

## 🔧 Setup

### Backend

cd autoflow-api  
mvn spring-boot:run

Backend runs on:  
http://localhost:8080

### Frontend

cd autoflow-ui  
npm install  
npm run dev

Frontend runs on:  
http://localhost:5173

---

## 🔑 Example Workflow

1. HTTP step – fetch external content  
2. AI step – summarize fetched data  
3. Execute workflow  
4. Inspect execution results and logs  

---

## 📌 Roadmap

- Drag & drop workflow builder
- Additional step types (email, webhook, database)
- Step editing and reordering
- Execution history and monitoring
- Docker Compose support

---

## 👤 Author

**Mustafa Kadak**  
Application / Production Support Engineer (Entry-level)
