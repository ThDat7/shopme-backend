# ❓ Troubleshooting Guide

This guide provides solutions for common issues you might encounter when setting up, running, or using the Shopme e-commerce platform.

## 🛠️ Installation Issues

### 🔌 Database Connection Problems

**Symptoms:**
- Application fails to start with database connection errors
- Logs show connection timeouts or authentication failures

**Solutions:**
1. **✅ Verify Database Credentials**:
   - Check that the database credentials in your `.env` file or application properties are correct
   - Ensure the database user has proper permissions

2. **🔍 Check Database Availability**:
   - Ensure MySQL is running: `docker ps | grep mysql` or `service mysql status`
   - Try connecting to the database manually: `mysql -u shopme_user -p shopme_db`

3. **🌐 Database Network Configuration**:
   - When using Docker, ensure the Docker network is properly set up
   - Check that the host in the database URL matches the actual host

### 🚫 Port Conflicts

**Symptoms:**
- Application fails to start with port already in use errors
- Another process is using port 8080 (admin) or 80 (client)

**Solutions:**
1. **🔍 Find the Process Using the Port**:
   ```bash
   # On Linux/macOS
   lsof -i :8080
   
   # On Windows
   netstat -ano | findstr 8080
   ```

2. **🛑 Stop the Conflicting Process or Change Ports**:
   - Either stop the conflicting process, or
   - Modify the application to use different ports:
     ```properties
     # In application.properties
     server.port=8081
     ```
     or via command line:
     ```bash
     mvn spring-boot:run -Dserver.port=8081
     ```

## 🐳 Docker Issues

### 🚫 Container Startup Failures

**Symptoms:**
- Containers exit immediately after starting
- `docker-compose up` shows errors

**Solutions:**
1. **🔍 Check Docker Compose Configuration**:
   - Verify that the docker-compose.yml file is correctly formatted
   - Check environment variables in your .env file

2. **📋 View Container Logs**:
   ```bash
   docker-compose logs mysql
   docker-compose logs admin
   docker-compose logs client
   ```

3. **🔍 Inspect Individual Container Issues**:
   ```bash
   docker inspect <container_id>
   ```

### 💾 Volume/Persistence Issues

**Symptoms:**
- Data is lost after container restart
- Database changes don't persist

**Solutions:**
1. **🔍 Check Volume Configuration**:
   - Verify that volumes are properly defined in docker-compose.yml
   - Ensure data directories exist and have proper permissions

2. **✅ Verify Data is Being Stored**:
   ```bash
   docker volume ls
   docker volume inspect <volume_name>
   ```

## 🚀 Application Errors

### 🛑 Application Won't Start

**Symptoms:**
- Application fails to start with various errors
- Spring Boot displays startup errors

**Solutions:**
1. **📋 Check Application Logs**:
   - Examine the logs for detailed error messages
   - Look for stack traces that indicate the root cause

2. **📦 Verify Dependencies**:
   - Ensure all required dependencies are available
   - Check for version conflicts in the Maven dependency tree:
     ```bash
     mvn dependency:tree
     ```

3. **⚙️ Check Configuration**:
   - Verify that application.properties or application.yml has all required settings
   - Ensure environment variables are correctly set

### 🔐 Authentication Issues

**Symptoms:**
- Unable to log in
- Authentication errors after login

**Solutions:**
1. **🔍 Check Security Configuration**:
   - Verify that OAuth2 and JWT settings are correctly configured
   - Ensure the authentication server is accessible

2. **🔄 Reset or Verify Credentials**:
   - Check that user credentials exist in the database
   - Verify that password encoders match between storage and verification

3. **🔑 JWT Token Issues**:
   - Check that JWT tokens are being issued correctly
   - Verify that token validation settings are correct

### 💰 Payment Integration Errors

**Symptoms:**
- Payment processing fails
- Integration with PayOS returns errors

**Solutions:**
1. **🔑 Verify API Credentials**:
   - Check that PayOS API keys are correctly configured
   - Ensure the PayOS service is available

2. **🧪 Test Integration in Isolation**:
   - Use the PayOS testing environment for validation
   - Check for proper request formatting

## ⚡ Performance Issues

### 🐢 Slow Response Times

**Symptoms:**
- Pages load slowly
- API requests take a long time to complete

**Solutions:**
1. **🔍 Check Database Query Performance**:
   - Look for slow queries in the database logs
   - Add indices to frequently queried columns
   - Use EXPLAIN for query analysis

2. **💨 Enable Caching**:
   - Configure Redis caching for frequently accessed data
   - Verify that caching is working as expected

3. **🔧 Optimize Application Code**:
   - Profile the application to identify bottlenecks
   - Optimize resource-intensive operations

### 🧠 Memory Issues

**Symptoms:**
- Application crashes with OutOfMemoryError
- Performance degrades over time

**Solutions:**
1. **📈 Increase JVM Memory**:
   - Adjust heap size settings: `-Xmx2g -Xms1g`
   - Monitor memory usage with JConsole or VisualVM

2. **🔍 Check for Memory Leaks**:
   - Use memory profiling tools
   - Look for growing collections or caches that aren't being cleared

## 🌐 Common HTTP Status Code Issues

### 🔍 404 Not Found

**Possible Causes:**
- Resource does not exist
- Incorrect URL mapping
- Context path mismatch

**Solutions:**
- Check that the requested resource exists
- Verify URL mappings in controller classes
- Check the context path configuration

### ⚠️ 400 Bad Request

**Possible Causes:**
- Invalid request parameters
- Malformed request body
- Validation errors

**Solutions:**
- Check request format against API documentation
- Verify that request body matches expected schema
- Look for validation error details in the response

### 🔒 401 Unauthorized / 403 Forbidden

**Possible Causes:**
- Missing authentication token
- Invalid or expired token
- Insufficient permissions

**Solutions:**
- Check that authentication token is included in requests
- Verify that the token is valid and not expired
- Ensure the user has appropriate permissions

### 💥 500 Internal Server Error

**Possible Causes:**
- Unhandled exceptions in the application
- Database errors
- External service failures

**Solutions:**
- Check application logs for detailed error messages
- Look for exceptions in the server logs
- Fix underlying code issues or add proper error handling

## 🆘 Still Need Help?

If you've tried these troubleshooting steps and still have issues:

1. Search existing issues in the project repository
2. Open a new issue with detailed information:
   - Description of the problem
   - Steps to reproduce
   - Expected vs. actual behavior
   - Logs and error messages
   - Environment details (OS, Java version, etc.)

## ⏭️ What's Next

After troubleshooting any issues, you might want to learn how to contribute to the project.

Continue: [👥 Contributing Guide](CONTRIBUTING.md)
