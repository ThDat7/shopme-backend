<p align="center">
  <img width="256px" src="assets/logo/shopme.png" alt="Shopme Logo">
  <br>
  <br>
  Multi-module Spring Boot E-commerce Platform
</p>

# 🛒 Shopme E-Commerce Platform

![Java 17](https://img.shields.io/badge/Java-17-orange.svg) ![Spring Boot 3.1.0](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen.svg) ![AWS S3](https://img.shields.io/badge/AWS-S3-yellow.svg) ![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg) ![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

E-commerce solution with Admin backend and Client frontend applications - **Currently under active development**

## 🌐 Live Demo

The application is available online for preview:

- **Frontend Demo**: [http://srv791694.hstgr.cloud](http://srv791694.hstgr.cloud)

> Note: Currently only the Client side is available for demo. Admin interface is under development.

## 💻 Demo Screenshots

<p align="center">
  <img width="80%" src="assets/screenshots/client-homepage.png" alt="Client Homepage">
  <br><em>Client Homepage</em>
</p>

<p align="center">
  <img width="80%" src="assets/screenshots/product-catalog.png" alt="Product Catalog">
  <br><em>Product Catalog</em>
</p>

<p align="center">
  <img width="80%" src="assets/screenshots/checkout.png" alt="Checkout Process">
  <br><em>Checkout Process</em>
</p>

<p align="center">
  <img width="80%" src="assets/screenshots/payment.png" alt="Payment Options">
  <br><em>Payment Options</em>
</p>

## ❓ About

Shopme is a comprehensive e-commerce platform built with Spring Boot, offering both administrative backend and customer-facing frontend applications. The system follows a multi-module architecture designed for scalability, maintainability, and best practices in e-commerce application development.

> **Development Status**: The client-side functionality is largely implemented, while the admin interface is still under active development.

## 🏛️ System Architecture

![Shopme Architecture](assets/diagrams/system-architecture.svg)

## 🧩 Module Structure

```mermaid
graph TD
    Project[Shopme Project]
    Common[shopme-common<br>Shared entities, DTOs, utilities]
    WebParent[shopme-webparent<br>Common web configurations]
    Admin[shopme-admin<br>Admin backend application]
    Client[shopme-client<br>Client backend application]
    Frontend[External Frontend Repository]
    
    Project --> Common
    Project --> WebParent
    WebParent --> Admin
    WebParent --> Client
    Admin -.->|REST API| Frontend
    Client -.->|REST API| Frontend
    Common --> WebParent
    
    style Frontend fill:#f9f,stroke:#333,stroke-width:2px
    style Project fill:#ffe,stroke:#333,stroke-width:2px
    style Common fill:#ddf,stroke:#333,stroke-width:2px
    style WebParent fill:#ddf,stroke:#333,stroke-width:2px
    style Admin fill:#bbf,stroke:#333,stroke-width:2px
    style Client fill:#bbf,stroke:#333,stroke-width:2px
```

## 🔗 Project Repositories

This project is split into two separate repositories:

- **Backend (Current)**: Spring Boot API services and server-side logic
- **Frontend**: [shopme-frontend](https://github.com/thDat7/shopme-frontend) - React-based UI for both client and admin interfaces

## 💪 Features

- 🏪 **E-commerce Platform**: Full-featured online store with product catalog, shopping cart, and checkout
- 👨‍💼 **Admin Dashboard**: Comprehensive management of products, orders, customers, and settings (in development)
- 🎁 **Promotion System**: Flexible discount and promotion configurations
- 💳 **Multiple Payment Methods**: Support for COD and online banking integration
- 📱 **Responsive Design**: Works seamlessly across devices
- 🔒 **Secure Authentication**: Supporting both traditional and Google authentication
- 📦 **Docker Deployment**: Containerized for easy deployment and scaling
- ☁️ **AWS S3 Integration**: Cloud-based file storage for product images and assets
- 🔄 **Future-Ready Architecture**: Designed for easy integration with Redis, Elasticsearch and Kafka/Redis Pub/Sub

## 🚀 Technology Stack

- **Backend**: Java 17, Spring Boot 3.1.0, Spring Security, Spring Data JPA
- **Database**: MySQL 8.0
- **Cloud Services**: AWS S3 for file storage
- **Authentication**: JWT, OAuth2 with Google
- **Payment Processing**: PayOS Integration
- **Deployment**: Docker, Docker Compose
- **Future Extensions**: Redis (Caching), Elasticsearch (Search), Kafka/Redis Pub/Sub (Event Streaming)

## 🔽 Installation & Setup

You can set up Shopme by following the [prerequisites](/docs/1_prerequisites.md) and [installation guide](/docs/2_installation.md).

## 📚 Documentation

You can find the detailed [documentation](/docs/README.md) for Shopme here:

1. [💼 Prerequisites](/docs/1_prerequisites.md)
2. [⬇️ Installation](/docs/2_installation.md)
3. [🔧 Configuration](/docs/3_1_configuration.md)
4. [🚀 Deployment](/docs/3_2_deployment.md)
5. [📡 API Documentation](/docs/4_1_api_client.md)
6. [💻 Development Guide](/docs/5_development.md)
7. [👥 Troubleshooting](/docs/6_troubleshooting.md)

## 📙 Contributing

Thank you for considering contributing to Shopme.
You can find the contribution guidelines [here](/docs/CONTRIBUTING.md).

## ⚖️ License

Shopme is licensed under the Apache License, Version 2.0. Please see the [license file](LICENSE) for more information.