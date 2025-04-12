# ⬇️ Installation Guide

This guide provides step-by-step instructions for setting up the Shopme e-commerce platform on your system.

## 🔍 Prerequisites

Before proceeding, make sure you have installed all the [💼 prerequisites](1_prerequisites.md).

## 🚀 Installation Steps

### 1️⃣ Clone the Repository

```bash
git clone <repository-url>
cd shopme-project
```

### 2️⃣ Configure Environment Variables

The project uses environment variables for configuration. You can create an environment-specific file based on the provided sample.

```bash
# Create an environment file for your target environment (e.g., development)
cp .env.sample .env.<environment>
```

For example:
```bash
# For development
cp .env.sample .env.dev

# For production
cp .env.sample .env.production
```

Then edit the environment file with your preferred values:

```
# Database Configuration
DB_HOST=mysql
DB_PORT=3306
DB_NAME=shopmedatabase
DB_USER=shopme
DB_PASS=your_password
MYSQL_ROOT_PASSWORD=root_password
MYSQL_EXTERNAL_PORT=3306

# Application Configuration
CLIENT_SPRING_PROFILE=dev  # or your chosen environment
CLIENT_PUBLIC_HOST=localhost
CLIENT_FRONTEND_HOST=http://localhost:5173

ADMIN_SPRING_PROFILE=dev  # or your chosen environment
ADMIN_FRONTEND_HOST=http://localhost:5173
```

> **Note:** Environment-specific files (e.g., `.env.dev`, `.env.production`) are added to `.gitignore` and should never be committed to the repository as they may contain sensitive information.

### 3️⃣ Build the Project

```bash
mvn clean install -DskipTests
```

### 4️⃣ Configure Application Properties

Each application module contains an `application.properties` file that loads profile-specific configuration. 

1. Create environment-specific properties files:

```bash
# For Admin application
cp shopme-webparent/shopme-admin/src/main/resources/application-sample.properties shopme-webparent/shopme-admin/src/main/resources/application-<environment>.properties

# For Client application
cp shopme-webparent/shopme-client/src/main/resources/application-sample.properties shopme-webparent/shopme-client/src/main/resources/application-<environment>.properties
```

For example:
```bash
# For development environment
cp shopme-webparent/shopme-admin/src/main/resources/application-sample.properties shopme-webparent/shopme-admin/src/main/resources/application-dev.properties
cp shopme-webparent/shopme-client/src/main/resources/application-sample.properties shopme-webparent/shopme-client/src/main/resources/application-dev.properties

# For production environment
cp shopme-webparent/shopme-admin/src/main/resources/application-sample.properties shopme-webparent/shopme-admin/src/main/resources/application-production.properties
cp shopme-webparent/shopme-client/src/main/resources/application-sample.properties shopme-webparent/shopme-client/src/main/resources/application-production.properties
```

2. Edit the application properties files to set appropriate values for your environment.

> **Note:** The environment-specific properties files are added to `.gitignore` and should never be committed to the repository as they may contain sensitive information.

### 5️⃣ Run the Application with Docker

Use Docker Compose to start the application stack, specifying your environment file:

```bash
docker-compose --env-file .env.<environment> up -d
```

For example:
```bash
# For development
docker-compose --env-file .env.dev up -d

# For production
docker-compose --env-file .env.production up -d
```

This will start three containers:
- `shopme-mysql`: MySQL database
- `shopme-admin`: Admin backend application
- `shopme-client`: Client frontend application

### 6️⃣ Access the Applications

Once the containers are running:

- **Admin Application**: http://localhost:8080
- **Client Application**: http://localhost:80

## 📋 Installation in a Development Environment

For development without Docker, you'll need to:

1. Install and configure MySQL locally
2. Configure the application properties files
3. Run the applications separately

### 🖥️ Running the Admin Application

```bash
cd shopme-webparent/shopme-admin
mvn spring-boot:run -Dspring-boot.run.profiles=<environment>
```

For example:
```bash
# For development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# For production
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

### 🌐 Running the Client Application

```bash
cd shopme-webparent/shopme-client
mvn spring-boot:run -Dspring-boot.run.profiles=<environment>
```

For example:
```bash
# For development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# For production
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

## ⏭️ What's Next

After installation, you'll need to configure your application for your specific environment.

Continue to [🔧 Configuration Guide](3_1_configuration.md)
