# Smart Factory AI Platform — High-Level Design

## 1. Overview

The Smart Factory AI Platform is a backend platform designed to manage
factories, machines, machine telemetry, events, analytics, and AI-powered
insights.

The platform is being developed as a microservices-based system using
Java and Spring Boot.

The system will be built incrementally, starting with the Factory Service
and expanding into machine management, event streaming, IoT communication,
real-time monitoring, analytics, and AI capabilities.

---

## 2. Current Architecture

The current implementation contains the Factory Service.

```text
                    Client
                      |
                      | HTTP/REST
                      v
              +------------------+
              |  Factory Service  |
              |   Spring Boot    |
              +--------+---------+
                       |
                       | JPA/Hibernate
                       v
              +------------------+
              |      MySQL       |
              +------------------+