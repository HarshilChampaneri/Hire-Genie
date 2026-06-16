# 🚀 Hire Genie

> **An AI-Powered Career Intelligence Platform built on a cloud-native microservices architecture.**
>
> Hire Genie helps job seekers and recruiters make smarter decisions through AI-assisted resume generation, intelligent job recommendations, employee matching, and realistic interview roleplay simulations — all powered by scalable distributed services.

---

## 🌟 Why Hire Genie?

Finding the right opportunity or the right candidate is often fragmented across multiple platforms and tools.

**Hire Genie unifies the hiring ecosystem into a single intelligent platform** by combining:

- 🧠 **AI-driven resume generation**
- 🎯 **Personalized RAG based job recommendations**
- 👥 **Intelligent RAG based employee-candidate matching**
- 🎤 **AI-powered mock interview roleplay**
- 🔐 **Secure JWT based authentication and authorization via E-mail OTP verification**
- ⚡ **Event-driven communication**
- ☁️ **Cloud-native microservices architecture**

The result is a modern hiring platform that bridges the gap between **talent discovery**, **career growth**, and **recruitment efficiency**.

---

# 🏗️ Architecture Overview

Hire Genie follows a **Microservices Architecture** powered by the Spring ecosystem and modern cloud-native tools.

```text
                        ┌─────────────────┐
                        │ React Frontend  │
                        └────────┬────────┘
                                 │
                                 ▼
                     ┌─────────────────────┐
                     │     API Gateway     │
                     └─────────┬───────────┘
                               │
         ┌─────────────────────┼─────────────────────┐
         │                     │                     │
         ▼                     ▼                     ▼

 ┌──────────────┐    ┌────────────────┐    ┌────────────────┐
 │ Security     │    │ Resume Builder │    │ Job Service    │
 │ Service      │    │ Service        │    │                │
 └──────────────┘    └────────────────┘    └────────────────┘

         ▼                     ▼                     ▼

 ┌────────────────┐   ┌────────────────────┐   ┌─────────────────────────┐
 │ Roleplay       │   │ Job Recommendation │   │ Employee Recommendation │
 │ Service        │   │ Engine             │   │ Engine                  │
 └────────────────┘   └────────────────────┘   └─────────────────────────┘

                               │
                               ▼

                   ┌──────────────────────┐
                   │ Kafka Event Bus      │
                   └──────────────────────┘
```

---

# 📂 Project Structure

```text
Hire-Genie/
│
├── hire-genie-frontend/              # React Frontend
│
├── api-gateway/                      # API Gateway
├── config-server/                    # Centralized Configuration Server
├── eureka-discovery-server/          # Service Discovery
│
├── security-service/                 # Authentication & Authorization
├── resume-builder/                   # AI Resume Builder
├── job-service/                      # Job Management Service
├── roleplay-service/                 # AI Interview Roleplay Service
│
├── job-recommendation-engine/        # AI Job Recommendation Engine
├── employee-recommendation-engine/   # Candidate Recommendation Engine
│
├── docker-compose.yaml               # Complete Infrastructure Setup
└── README.md
```

---

# ✨ Core Features

## 📝 AI Resume Builder

Generate professional resumes dynamically using AI-assisted content generation and resume customization.

### Capabilities

* Resume creation
* Resume optimization
* Dynamic resume generation
* ATS-friendly formatting
* Career profile enhancement

---

## 🎯 Job Recommendation Engine

Uses intelligent matching mechanisms to recommend relevant job opportunities.

### Capabilities

* Personalized recommendations
* Resume-based matching
* Skill-based filtering
* Ranking and relevance scoring

---

## 👥 Employee Recommendation Engine

Designed for recruiters and organizations to discover the most suitable candidates.

### Capabilities

* Candidate ranking
* Profile similarity matching
* Skill alignment
* Intelligent filtering

---

## 🎤 AI Interview Roleplay

An AI-powered mock interview environment that helps candidates practice and improve interview performance.

### Capabilities

* Simulated interviews
* Dynamic question generation
* Conversational interaction
* Interview preparation

---

## 🔐 Security Service

Provides centralized authentication and authorization.

### Capabilities

* User authentication
* Role-based access control
* Session/token management
* Secure API access
* E-mail OTP Verification

---

## 📋 Job Service

Handles job lifecycle management.

### Capabilities

* Apply for Job
* Job Application accept/reject feature for Recruiters
* Job creation
* Job updates
* Job search
* Job publishing
* Candidate-job mapping

---

# 🧠 Technical Overview

## Backend Architecture

The backend is built using:

* Spring Boot
* Spring Cloud
* Service Discovery
* API Gateway Pattern
* Distributed Configuration
* Event-Driven Communication

### Infrastructure Components

| Component               | Purpose                              |
| ----------------------- | ------------------------------------ |
| Eureka Discovery Server | Service Registration & Discovery     |
| Config Server           | Centralized Configuration Management |
| API Gateway             | Unified Entry Point                  |
| Kafka                   | Asynchronous Event Processing        |
| Zipkin                  | Distributed Tracing                  |
| Redis                   | Caching Layer                        |

---

# 💻 Tech Stack

## Frontend

* React 19
* React Router
* Axios
* Tailwind CSS
* Vite

---

## Backend

* Java 21
* Spring Boot
* Spring Cloud
* Spring AI
* Spring Data
* Spring Security
* OpenFeign
* Spring Validation
* Spring Actuator

---

## AI Layer

* Spring AI
* Google Generative AI Integration

---

## Databases

### PostgreSQL

Used by:

* Resume Builder
* Job Service

### MongoDB

Used by:

* Security Service

### PGVector (for Vector Embeddings)

Used by:

* Job Recommendation Engine
* Employee Recommendation Engine

---

## Messaging & Streaming

* Apache Kafka
* Kafka UI

---

## Caching

* Redis

---

## Monitoring & Observability

* Zipkin
* Micrometer
* Spring Actuator

---

## DevOps & Deployment

* Docker
* Docker Compose

---

# 🔄 Service Communication

Hire Genie uses a combination of:

### Synchronous Communication

* REST APIs
* OpenFeign Clients

### Asynchronous Communication

* Apache Kafka Events

This approach improves:

* Scalability
* Reliability
* Fault Isolation
* Service Independence

---

# 📊 Database Architecture

```text
Security Service
      │
      ▼
   MongoDB

Resume Builder
      │
      ▼
 PostgreSQL

Job Service
      │
      ▼
 PostgreSQL

Job Recommendation Engine
      │
      ▼
  PGVector

Employee Recommendation Engine
      │
      ▼
  PGVector
```

---

# 🚀 Getting Started

## Prerequisites

Install the following:

* Java 21+
* Maven
* Docker
* Docker Compose
* Node.js
* npm

---

## Clone Repository

```bash
git clone https://github.com/HarshilChampaneri/Hire-Genie.git

cd Hire-Genie
```

---

## Start Infrastructure & Services

```bash
docker compose up --build
```

---

## Start Frontend

```bash
cd hire-genie-frontend

npm install

npm run dev
```

---

# 🔍 Observability

The platform includes built-in monitoring capabilities:

### Zipkin

Distributed request tracing across services.

```text
http://localhost:9411
```

### Eureka Dashboard

Service registration monitoring.

```text
http://localhost:8761
```

### Kafka UI

Kafka topic and event inspection.

```text
http://localhost:8070
```

---

# 🎯 Design Goals

Hire Genie is designed around:

* Scalability
* Maintainability
* Extensibility
* High Availability
* Cloud-Native Deployment
* AI-Driven User Experience

---

# 🔮 Future Roadmap

* AI-powered resume scoring
* ATS compatibility analyzer
* Interview performance analytics
* Real-time recruiter dashboard
* Skill-gap analysis
* Career path recommendations
* Vector-search optimization
* Kubernetes deployment support
* CI/CD automation pipeline

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch

```bash
git checkout -b feature/amazing-feature
```

3. Commit your changes

```bash
git commit -m "Add amazing feature"
```

4. Push your branch

```bash
git push origin feature/amazing-feature
```

5. Open a Pull Request

---

# 📜 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author

**Harshil Champaneri**

Building intelligent systems that combine AI, distributed architecture, and modern cloud-native engineering to transform the hiring experience.

---

<div align="center">

### ⭐ If you like this project, give it a star and support the development of Hire Genie!

</div>
