## ⚙️ Performance Optimization

- Use caching for product and category data
- Optimize database queries with proper indexing
- Use pagination for large result sets
- Minimize the size of JSON responses
- Consider using asynchronous processing for long-running tasks

## 🔍 Debugging Tips

### 1. Enable Debug Logging

```
logging.level.com.shopme=DEBUG
```

### 2. Spring Boot Developer Tools

- Automatic restart on code changes
- Enhanced error pages
- LiveReload support

### 3. Remote Debugging

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## 📊 System Diagrams

### 1. Architecture Diagram

Shopme follows a modern application architecture with separate backend and frontend repositories:

```mermaid
graph TD
    subgraph "Frontend Repository"
    AdminUI[Admin UI<br>React]
    ClientUI[Client UI<br>React]
    end
    
    subgraph "Backend Repository"
    AdminAPI[Admin API<br>Spring Boot]
    ClientAPI[Client API<br>Spring Boot]
    end
    
    AdminUI -->|HTTP/REST| AdminAPI
    ClientUI -->|HTTP/REST| ClientAPI
    
    AdminAPI --> DB[(MySQL<br>Database)]
    ClientAPI --> DB
    
    style Frontend fill:#f9f,stroke:#333,stroke-width:2px
    style Backend fill:#bbf,stroke:#333,stroke-width:2px
    style DB fill:#bfb,stroke:#333,stroke-width:2px
```

### 2. Module Structure

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

### 3. Database Entity Relationship

Simplified Entity Relationship Diagram for the core business model:

```mermaid
erDiagram
    Category ||--o{ Product : "has"
    Brand ||--o{ Product : "has"
    Product ||--o{ PromotionProduct : "included in"
    Promotion ||--o{ PromotionProduct : "contains"
    Promotion {
        int id
        string title
        string content
        date startDate
        date endDate
        boolean active
        enum type
    }
    PromotionProduct {
        int id
        int promotion_id
        int product_id
        float discountPercent
        int stockLimit
        int soldCount
    }
    Product {
        int id
        string name
        string description
        int brand_id
        int category_id
        boolean enabled
        boolean inStock
        decimal price
        decimal discountPercent
    }
    Order ||--o{ OrderDetail : "contains"
    OrderDetail }o--|| Product : "references"
    Customer ||--o{ Order : "places"
    Category {
        int id
        string name
        string alias
        string image
        int parent_id
    }
    Brand {
        int id
        string name
        string logo
    }
```

### 4. Promotion System

```mermaid
graph TD
    Admin[Admin User] -->|Creates| Promotion
    Promotion -->|Has Type| PromotionType
    PromotionType -->|Types| Types[FLASH_SALE<br>CATEGORY_DISCOUNT<br>NEW_ARRIVAL]
    Admin -->|Manually adds| Products
    Products -->|Added to| PromotionProduct
    Promotion -->|Contains| PromotionProduct
    PromotionProduct -->|Has| Discount[Discount Percentage<br>Stock Limit<br>Sold Count]
    Customer -->|Uses| Promotion
    PromotionUsage -->|Tracks| Usage[Customer<br>Product<br>Order]
    Customer -->|Places| Order
    
    style Promotion fill:#f9f,stroke:#333,stroke-width:2px
    style PromotionProduct fill:#f9f,stroke:#333,stroke-width:2px
    style PromotionType fill:#f9f,stroke:#333,stroke-width:2px
    style Types fill:#bbf,stroke:#333,stroke-width:2px
```

### 5. Checkout Sequence

```mermaid
sequenceDiagram
    participant CUI as Client UI
    participant CAPI as Client API (CheckoutController)
    participant CS as CheckoutServiceImpl
    participant PS as PaymentServiceImpl
    participant POS as PayOS (Third Party)
    participant DB as Database (Order)
    
    %% Start checkout process
    CUI->>CAPI: Checkout Request
    CAPI->>CS: placeOrderPayOS(request)
    
    %% Create order and get payment link
    CS->>DB: Save Order (Status: PENDING_PAYMENT)
    CS->>PS: generatePayOSResponse(order, returnUrl, cancelUrl)
    PS->>POS: createPaymentLink(paymentData)
    POS-->>PS: Payment Link & QR Code
    PS-->>CS: CheckoutResponseData
    CS-->>CAPI: PlaceOrderPayOSResponse
    CAPI-->>CUI: Payment Link & QR Code
    
    %% Customer makes payment externally
    Note over CUI,POS: Customer completes payment via bank/e-wallet
    
    %% Payment service sends webhook
    POS->>CAPI: Webhook Notification
    CAPI->>CS: payosTransferHandler(webhook)
    CS->>PS: handlePayOSWebhook(webhook)
    PS->>CS: onPaymentSuccess(orderId, amount)
    CS->>DB: Update Order Status to PAID
    
    %% Client polls for status
    loop Until status changes
        CUI->>CAPI: Check Order Status
        CAPI->>DB: Get Order Status
        DB-->>CAPI: Order Status
        CAPI-->>CUI: Order Status
    end
    
    %% Redirect on success
    Note over CUI: Redirect to success page
```

For Cash-on-Delivery (COD) orders, the process is simpler:

```mermaid
sequenceDiagram
    participant CUI as Client UI
    participant CAPI as Client API
    participant CS as CheckoutServiceImpl
    participant DB as Database
    
    CUI->>CAPI: COD Checkout Request
    CAPI->>CS: placeOrderCOD(request)
    CS->>DB: Save Order (Status: NEW)
    CS-->>CAPI: Order Created Success
    CAPI-->>CUI: Order Confirmation
    
    Note over CUI: Redirect to order success page
```

## 📸 Screenshots

Below are some screenshots of the client application that illustrate key functionality:

### Homepage

The homepage displays featured products, promotions, and product categories:

![Shopme Homepage](../assets/screenshots/client-homepage.png)

### Product Catalog

The product catalog page with filtering, sorting, and display options:

![Product Catalog](../assets/screenshots/product-catalog.png)

### Checkout

The checkout interface shows order summary and payment methods (COD and Bank Payment):

![Checkout](../assets/screenshots/checkout.png)

### Payment

The payment interface allows users to complete payment via bank or COD:

![Payment](../assets/screenshots/payment.png)

## ⏭️ What's Next

If you encounter issues during development, the troubleshooting guide can help resolve common problems.

Continue to [❓ Troubleshooting Guide](6_troubleshooting.md)
