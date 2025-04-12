# 🔧 Configuration Guide

This guide details the configuration options available for the Shopme e-commerce platform.

## 🌍 Environment Variables

The application uses a combination of environment variables and Spring configuration properties to customize its behavior across different environments.

### 💾 Database Configuration

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DB_HOST` | Database server hostname | `mysql` |
| `DB_PORT` | Database port | `3306` |
| `DB_NAME` | Database name | `shopmedatabase` |
| `DB_USER` | Database username | `shopme` |
| `DB_PASS` | Database password | `shopme` |
| `MYSQL_ROOT_PASSWORD` | MySQL root password (for Docker setup) | `root` |
| `MYSQL_EXTERNAL_PORT` | External port mapping for MySQL | `3306` |

### 📱 Application Configuration

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `CLIENT_SPRING_PROFILE` | Spring profile for the client application | Depends on environment |
| `CLIENT_PUBLIC_HOST` | Publicly accessible hostname for the client application | Depends on environment |
| `CLIENT_FRONTEND_HOST` | Frontend host address | Depends on environment |
| `ADMIN_SPRING_PROFILE` | Spring profile for the admin application | Depends on environment |
| `ADMIN_FRONTEND_HOST` | Admin frontend host address | Depends on environment |

### 📄 Environment Files

The project uses environment files to manage configuration:

- **`.env.sample`**: Template file included in the repository
- **`.env.<environment>`**: Environment-specific configurations

You can create any environment-specific file you need (e.g., `.env.dev`, `.env.staging`, `.env.production`) based on the `.env.sample` template.

> **Important**: Environment-specific files (e.g., `.env.dev`, `.env.prod`) should not be committed to the repository as they may contain sensitive information. These files are included in `.gitignore`.

When using Docker Compose, you specify the environment file using the `--env-file` parameter:

```bash
docker-compose --env-file .env.<environment> up -d
```

## 🔄 Spring Configuration Properties

Key Spring Boot configuration properties used by the application:

### 📊 Data Source Configuration

```properties
# Database Connection
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### 🔒 Security Configuration

```properties
# JWT Configuration
jwt.signerKey=your_jwt_signer_key_here
jwt.expiration=86400

# Google OAuth2 Configuration (Client Application)
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
```

### 📁 File Upload Configuration

```properties
# File Upload Paths
file.upload.host=http://localhost
file.upload.suffix.user-photos=user-photos
file.upload.suffix.category-images=../category-images
file.upload.suffix.brand-logos=../brand-logos
file.upload.suffix.product-main-images=../product-images
file.upload.suffix.product-images=extras
file.upload.suffix.site-logo=../site-logo
```

## 📑 Profile-Specific Configuration

The application supports different Spring profiles for various deployment environments:

### 🛠️ Creating Environment-Specific Properties Files

You can create environment-specific properties files for any environment you need:

```bash
# Create a properties file for any environment
cp shopme-webparent/shopme-admin/src/main/resources/application-sample.properties shopme-webparent/shopme-admin/src/main/resources/application-<environment>.properties

cp shopme-webparent/shopme-client/src/main/resources/application-sample.properties shopme-webparent/shopme-client/src/main/resources/application-<environment>.properties
```

For example, you might create:
- `application-dev.properties` - For development
- `application-staging.properties` - For staging
- `application-production.properties` - For production

> **Note**: These environment-specific properties files are added to `.gitignore` and should never be committed to the repository.

### 🛠️ Development Profile

Development profile typically includes:
- Debug-level logging
- H2 console enabled (if applicable)
- Developer-friendly error messages
- Potential development-only features

### 🏭 Production Profile

Production profile typically includes:
- Info or warning level logging
- Enhanced security settings
- Performance optimizations
- Disabled developer tools

## 🔌 Third-Party Service Configuration

### 🔑 Google Authentication

To enable Google authentication:

1. Create a project in the Google Developer Console
2. Configure the OAuth consent screen
3. Create OAuth client ID credentials
4. Add authorized redirect URIs (e.g., `http://localhost/oauth2/callback/google` for development)
5. Set the `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` environment variables

### 💰 PayOS Payment Integration

To enable PayOS payment processing:

1. Register for a PayOS account
2. Generate API credentials
3. Set the `PAYOS_CLIENT_ID`, `PAYOS_API_KEY`, and `PAYOS_CHECKSUM_KEY` environment variables

## 📁 Configuration Files

The following files control the application configuration:

- `application.properties` / `application-{profile}.properties`: Spring Boot configuration
- `docker-compose.yml`: Docker container configuration
- `.env.<environment>`: Environment variable definitions

## ⏭️ What's Next

After configuring your application, you'll need to learn how to deploy it to various environments.

Continue to [🚀 Deployment Guide](3_2_deployment.md)
