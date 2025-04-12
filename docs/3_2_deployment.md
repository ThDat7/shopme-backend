# 🚀 Deployment Guide

This guide provides instructions for deploying the Shopme e-commerce platform to various environments.

## 🔗 Full-Stack Deployment

Shopme consists of two separate repositories:
- **Backend (Current)**: Spring Boot API services (admin and client APIs)
- **Frontend**: React-based UIs available at [shopme-frontend](https://github.com/thDat7/shopme-frontend)

For a complete deployment, you'll need to set up both components. This guide focuses primarily on the backend services. For frontend deployment instructions, refer to the frontend repository documentation.

## 🐳 Docker Deployment

The recommended approach for deploying the Shopme backend is using Docker Compose, which allows for consistent deployment across different environments.

### 📋 Prerequisites

- Docker Engine (20.10.x or later)
- Docker Compose (2.x or later)
- At least 4GB of RAM allocated to Docker
- Sufficient disk space for images and data volumes

### 📦 Deployment Steps

#### 1️⃣ Prepare Environment Configuration

Create environment-specific files based on the provided sample:

```bash
# For development environment
cp .env.sample .env.dev

# For staging environment
cp .env.sample .env.staging

# For production environment
cp .env.sample .env.production
```

Edit these files to set appropriate values for each environment.

> **Note:** Environment-specific files are added to `.gitignore` and should never be committed to the repository as they may contain sensitive information.

#### 2️⃣ Build Docker Images

Build Docker images using the specific environment file:

```bash
docker-compose --env-file .env.<environment> build
```

For example:
```bash
# For development
docker-compose --env-file .env.dev build

# For production
docker-compose --env-file .env.production build
```

#### 3️⃣ Start the Application Stack

Start the application stack with your environment file:

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

This command will start all the services defined in the Docker Compose file:
- `shopme-mysql`: MySQL database
- `shopme-admin`: Admin backend application
- `shopme-client`: Client backend application

#### 4️⃣ Verify Deployment

Verify that all containers are running:

```bash
docker-compose ps
```

Check the logs if there are any issues:

```bash
docker-compose logs -f
```

Access the APIs:
- Client API: http://localhost:80 (or the configured port)
- Admin API: http://localhost:8080 (or the configured port)

#### 5️⃣ Stop the Application Stack

```bash
docker-compose down
```

To remove all data volumes as well:

```bash
docker-compose down -v
```

## 🌐 Connecting Frontend to Backend

To connect the frontend application to the backend APIs:

1. Deploy the backend services following the steps above
2. Configure the frontend application to point to the correct API endpoints:
   - For development: Usually `http://localhost:80` for client API and `http://localhost:8080` for admin API
   - For production: The URLs where your backend services are deployed

3. In the frontend repository, update the API configuration files with the appropriate backend URLs
4. Deploy the frontend application according to the instructions in the frontend repository

## 🌐 Multi-Environment Configuration

The application supports different deployment environments through environment variables and Spring profiles:

### 🛠️ Development Environment

- Uses `.env.dev` configuration
- Spring profile: `dev`
- Optimized for development and debugging
- Shows detailed error messages
- May have development-only features enabled

### 🧪 Staging Environment

- Uses `.env.staging` configuration
- Spring profile: `staging`
- Mimics production but with additional logging
- Used for testing before production deployment

### 🏭 Production Environment

- Uses `.env.production` configuration
- Spring profile: `production`
- Optimized for performance and security
- Minimal logging
- No developer features

## 🚢 Deployment to Cloud Environments

### ☁️ AWS Deployment

For deploying to AWS:

1. **Amazon ECS/Fargate**:
   - Create an ECS cluster
   - Define task definitions for each service
   - Set up load balancing and networking
   - Use AWS Secrets Manager for sensitive environment variables
   - Configure autoscaling rules

2. **EC2 with Docker**:
   - Launch EC2 instances
   - Install Docker and Docker Compose
   - Configure security groups
   - Set up load balancing with ALB
   - Deploy using the standard Docker Compose workflow with environment files

### 🌱 Kubernetes Deployment

For deploying to Kubernetes:

1. Create deployment manifests for each component
2. Configure services for internal communication
3. Set up ingress for external access
4. Use ConfigMaps and Secrets for configuration
5. Configure resource limits and requests
6. Set up health checks and readiness probes

## 📊 Monitoring and Logging

### 📝 Logging Configuration

For production deployments, consider:
- Centralized logging with the ELK stack or similar
- Log rotation policies
- Configure appropriate log levels for each environment
- Export logs to a persistent storage

### 🔍 Monitoring Solutions

Consider the following monitoring solutions:
- Prometheus and Grafana for metrics
- New Relic or Datadog for application performance monitoring
- ELK stack for log aggregation and analysis
- Uptime monitors for availability tracking

## 🔒 Security Considerations

### 🔐 HTTPS Configuration

For production deployments:
1. Obtain SSL certificates for your domains
2. Configure the web server to use HTTPS
3. Set up HTTP to HTTPS redirection
4. Configure HSTS headers

### 🔑 Secret Management

- Never store sensitive information in source control
- Use environment variables for configuration
- Consider using a secrets management solution for production

## 📋 Deployment Checklist

Before deploying to production:
- [ ] Database migration scripts are up to date
- [ ] All environment variables are correctly configured in your `.env.production` file
- [ ] Application has been thoroughly tested in staging
- [ ] Monitoring and logging are configured
- [ ] Backup strategy is in place
- [ ] SSL certificates are valid and installed
- [ ] Security scan has been performed
- [ ] Load testing has been conducted
- [ ] Frontend is configured to connect to the correct backend endpoints

## ⏭️ What's Next

After deploying the application, you'll want to explore the API documentation.

Continue to [📘 Client API Documentation](4_1_api_client.md) or [📗 Admin API Documentation](4_2_api_admin.md)
