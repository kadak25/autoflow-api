🚀 AutoFlow – Workflow Automation Platform

AutoFlow is a lightweight workflow automation platform that allows users to visually create, manage and run chained automation workflows such as:

HTTP → AI → Custom Steps

Inspired by tools like Zapier and n8n, AutoFlow focuses on simplicity, speed, and extensibility.

✨ Features

User authentication (JWT)

Create and manage workflows

Add multiple steps to a workflow

Supported step types:

HTTP requests

AI summarization

Dynamic context passing between steps

Execute workflows and view results

Modern dashboard UI

🧱 Tech Stack
Backend

Java 17

Spring Boot

Spring Security + JWT

JPA / Hibernate

PostgreSQL (or H2 for dev)

Frontend

React

TypeScript

Vite

Axios

🖥️ Screenshots

<img width="1317" height="796" alt="image" src="https://github.com/user-attachments/assets/767e91ba-cba4-4a56-814b-fac58a49389c" />
<img width="1263" height="736" alt="image" src="https://github.com/user-attachments/assets/2a708526-58be-4797-a77e-f6c503210774" />

🔧 Setup
Backend
cd autoflow-api
mvn spring-boot:run


Backend runs on:

http://localhost:8080

Frontend
cd autoflow-ui
npm install
npm run dev


Frontend runs on:

http://localhost:5173

🔑 Example Workflow

HTTP Step
Fetch webpage

AI Step
Summarize fetched content

Run workflow
View results

🧠 Why This Project?

This project demonstrates:

Backend architecture

REST API design

Authentication & authorization

Frontend integration

Real-world automation logic

Designed as a portfolio project to showcase full-stack skills.

📌 Roadmap

Drag & drop workflow builder

More step types (email, webhook, database)

Step editing & reordering

Execution history

Docker support

👤 Author

Mustafa Kadak
Junior Software Developer

