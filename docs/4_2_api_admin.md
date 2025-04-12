# 👨‍💼 Admin API Documentation

This document outlines the API endpoints available in the admin application of the Shopme e-commerce platform.

## 📡 API Overview

The admin application provides a comprehensive set of RESTful APIs for managing all aspects of the e-commerce platform. All API endpoints are prefixed with `/api/v1/` to ensure proper versioning.

## 🔐 Authentication

Admin authentication uses the same mechanisms as the client application but with administrative permissions:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | Authenticate with admin credentials |
| `/api/v1/auth/introspect` | POST | Validate and introspect a token |

## 📦 Product Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/products` | GET | List all products with pagination and filtering |
| `/api/v1/products` | POST | Create a new product |
| `/api/v1/products/{id}` | GET | Get product details |
| `/api/v1/products/{id}` | PUT | Update a product |
| `/api/v1/products/{id}` | DELETE | Delete a product |
| `/api/v1/products/{id}/enable/{status}` | GET | Enable/disable a product |

### 📋 Create/Update Product Request Example

```json
{
  "name": "New Smartphone",
  "description": "Latest smartphone with advanced features",
  "shortDescription": "Latest smartphone model",
  "price": 799.99,
  "discountPercent": 10,
  "stock": 50,
  "enabled": true,
  "categoryId": 5,
  "brandId": 3,
  "productDetails": [
    {
      "name": "Color",
      "value": "Black"
    },
    {
      "name": "Storage",
      "value": "128GB"
    }
  ]
}
```

## 🗂️ Category Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/categories` | GET | List all categories |
| `/api/v1/categories` | POST | Create a new category |
| `/api/v1/categories/{id}` | GET | Get category details |
| `/api/v1/categories/{id}` | PUT | Update a category |
| `/api/v1/categories/{id}` | DELETE | Delete a category |
| `/api/v1/categories/{id}/enable/{status}` | GET | Enable/disable a category |

## 🏷️ Brand Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/brands` | GET | List all brands |
| `/api/v1/brands` | POST | Create a new brand |
| `/api/v1/brands/{id}` | GET | Get brand details |
| `/api/v1/brands/{id}` | PUT | Update a brand |
| `/api/v1/brands/{id}` | DELETE | Delete a brand |
| `/api/v1/brands/{id}/enable/{status}` | GET | Enable/disable a brand |

## 📋 Order Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/orders` | GET | List all orders with pagination and filtering |
| `/api/v1/orders/{id}` | GET | Get order details |
| `/api/v1/orders/{id}/status` | PUT | Update order status |

### 🔄 Update Order Status Example

```
PUT /api/v1/orders/123/status
{
  "status": "SHIPPED",
  "note": "Order shipped via Express Delivery"
}
```

## 🎁 Promotion Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/promotions` | GET | List all promotions |
| `/api/v1/promotions` | POST | Create a new promotion |
| `/api/v1/promotions/{id}` | GET | Get promotion details |
| `/api/v1/promotions/{id}` | PUT | Update a promotion |
| `/api/v1/promotions/{id}` | DELETE | Delete a promotion |
| `/api/v1/promotions/{id}/products` | GET | Get products in a promotion |
| `/api/v1/promotions/{id}/products` | POST | Add product to a promotion |
| `/api/v1/promotions/{id}/products/{productId}` | DELETE | Remove product from a promotion |

### 📝 Create Promotion Example

```json
{
  "title": "Summer Sale",
  "content": "Get up to 50% off on selected electronics",
  "startDate": "2023-06-01T00:00:00Z",
  "endDate": "2023-06-30T23:59:59Z",
  "active": true,
  "type": "FLASH_SALE"
}
```

### ➕ Add Product to Promotion Example

```json
{
  "productId": 123,
  "discountPercent": 15,
  "stockLimit": 100
}
```

## 👥 Customer Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/customers` | GET | List all customers with pagination and filtering |
| `/api/v1/customers/{id}` | GET | Get customer details |
| `/api/v1/customers/{id}` | PUT | Update customer details |
| `/api/v1/customers/{id}/enable/{status}` | GET | Enable/disable a customer account |

## 🚚 Shipping Rate Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/shipping-rates` | GET | List all shipping rates |
| `/api/v1/shipping-rates` | POST | Create a new shipping rate |
| `/api/v1/shipping-rates/{id}` | GET | Get shipping rate details |
| `/api/v1/shipping-rates/{id}` | PUT | Update a shipping rate |
| `/api/v1/shipping-rates/{id}` | DELETE | Delete a shipping rate |

## ⚙️ Settings Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/settings` | GET | Get all system settings |
| `/api/v1/settings/{category}` | GET | Get settings by category |
| `/api/v1/settings/{category}/{key}` | GET | Get specific setting value |
| `/api/v1/settings/{category}/{key}` | PUT | Update a setting value |

## 🔄 Common API Patterns

### 📄 Pagination

All list endpoints support pagination using the following query parameters:

- `page`: Page number (0-based)
- `size`: Page size
- `sort`: Sort field and direction (e.g., `name,asc`)

### 🔍 Filtering

Most list endpoints support filtering through query parameters:

- `keyword`: Search term for text search
- Additional filters specific to the resource type

### 📊 Response Format

All API responses follow the consistent format defined in the [Client API Documentation](4_1_api_client.md#response-format).

## ❓ Error Handling

Error responses and status codes follow the same convention as described in the [Client API Documentation](4_1_api_client.md#error-codes).

## ⏭️ What's Next

After understanding the API interfaces, you might want to learn about development practices for the Shopme platform.

Continue: [💻 Development Guide](5_development.md)
