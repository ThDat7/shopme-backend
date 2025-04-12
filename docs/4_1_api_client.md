# 🛍️ Client API Documentation

This document outlines the API endpoints available in the client-facing application of the Shopme e-commerce platform.

## 📡 API Overview

The client application follows REST principles and uses JSON for data exchange. All API endpoints are prefixed with `/api/v1/` to ensure proper versioning.

## 🔐 Authentication

### 🔑 Authentication Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | Authenticate with username/password |
| `/api/v1/auth/google` | POST | Authenticate with Google |
| `/api/v1/auth/introspect` | POST | Validate and introspect a token |

### 🔄 Authentication Flow

#### 1. Username/Password Authentication

```
POST /api/v1/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```
{
  "status": "OK",
  "data": {
    "token": "jwt-token-here",
    "expiresIn": 3600,
    "tokenType": "Bearer"
  }
}
```

#### 2. Google Authentication

```
POST /api/v1/auth/google
{
  "idToken": "google-id-token"
}
```

#### 3. Using Authentication

- Include the JWT token in the `Authorization` header of subsequent requests:
  ```
  Authorization: Bearer jwt-token-here
  ```

## 📦 Product Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/products` | GET | List products with pagination and filtering |
| `/api/v1/products/{id}` | GET | Get product details by ID |
| `/api/v1/products/best-seller` | GET | Get best-selling products |
| `/api/v1/products/trending` | GET | Get trending products |
| `/api/v1/products/top-rated` | GET | Get top-rated products |
| `/api/v1/products/top-discounted` | GET | Get products with highest discounts |

## 🛒 Shopping Cart Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/cart` | GET | Get all cart items for the current user |
| `/api/v1/cart` | POST | Add product to cart |
| `/api/v1/cart` | PUT | Update cart item quantity |
| `/api/v1/cart/{id}` | DELETE | Remove item from cart |
| `/api/v1/cart/sync` | POST | Sync local cart with server cart |

## 💳 Checkout Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/checkout/calculate-shipping` | POST | Calculate shipping costs |
| `/api/v1/checkout/payment/COD` | POST | Place order with Cash on Delivery |
| `/api/v1/checkout/payment/PAY_OS` | POST | Place order with PayOS |

## 📋 Order Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/orders` | GET | List user orders with pagination |
| `/api/v1/orders/{orderId}` | GET | Get order details |
| `/api/v1/orders/{orderId}/status` | GET | Get order status |
| `/api/v1/orders/{orderId}/cancel` | POST | Cancel an order |

## 🎁 Promotion Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/promotions/types` | GET | Get active promotion types |
| `/api/v1/promotions/{id}` | GET | Get promotion details |
| `/api/v1/promotions/{id}/products` | GET | Get products in promotion |

## 🔄 Common API Patterns

### 📄 Pagination

All list endpoints support pagination using the following query parameters:

- `page`: Page number (0-based)
- `size`: Page size
- `sort`: Sort field and direction (e.g., `name,asc`)

Example:
```
GET /api/v1/products?page=0&size=10&sort=name,asc
```

Response format:
```json
{
  "status": "OK",
  "data": {
    "content": [
      // array of items
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 5,
    "totalElements": 47,
    "last": false,
    "first": true,
    "size": 10,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 10,
    "empty": false
  }
}
```

### 🔍 Filtering

Most list endpoints support filtering through query parameters:

- `keyword`: Search term for text search
- `categoryId`: Filter by category ID
- `brandId`: Filter by brand ID
- `minPrice`/`maxPrice`: Price range filters
- `enabled`: Filter by enabled status

Example:
```
GET /api/v1/products?keyword=phone&categoryId=2&minPrice=100&maxPrice=500
```

## 📊 Response Format

All API responses follow a consistent format:

```json
{
  "status": "OK",
  "data": {
    // Response data
  }
}
```

For errors:

```json
{
  "status": "ERROR",
  "message": "Error message",
  "errors": [
    {
      "field": "fieldName",
      "message": "Field-specific error message"
    }
  ]
}
```

## 📝 Sample Requests and Responses

### 📦 Get Product Details

Request:
```
GET /api/v1/products/123
```

Response:
```json
{
  "status": "OK",
  "data": {
    "id": 123,
    "name": "Smartphone X",
    "description": "Latest smartphone model with advanced features",
    "price": 799.99,
    "discountPercent": 10,
    "currentPrice": 719.99,
    "stock": 50,
    "enabled": true,
    "inStock": true,
    "mainImage": "smartphone-x-main.jpg",
    "category": {
      "id": 5,
      "name": "Smartphones"
    },
    "brand": {
      "id": 3,
      "name": "TechBrand"
    },
    "images": [
      {
        "id": 456,
        "name": "smartphone-x-1.jpg"
      },
      {
        "id": 457,
        "name": "smartphone-x-2.jpg"
      }
    ],
    "details": [
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
}
```

### 🛒 Place an Order

Request:
```
POST /api/v1/checkout/payment/COD
{
  "shippingAddressId": 45,
  "paymentMethod": "COD",
  "note": "Please deliver after 6 PM"
}
```

Response:
```json
{
  "status": "OK",
  "data": {
    "orderId": 789,
    "orderTrackingNumber": "ORD-20230615-789",
    "orderStatus": "PROCESSING",
    "orderTotal": 759.98,
    "shippingCost": 10.00
  }
}
```

## ❓ Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success |
| 400 | Bad Request - Invalid parameters or request body |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error |

## ⏭️ What's Next

Now that you understand the client-facing API, you might want to explore the admin API endpoints.

Continue: [👨‍💼 Admin API Documentation](4_2_api_admin.md)
