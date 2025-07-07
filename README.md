# Retail Management Apps

A comprehensive Spring Boot monolithic e-commerce and retail management web application. Features complete user authentication, product catalog management, order processing with PayPal integration, role-based access control, and business reporting capabilities.

## Features

### Authentication & Security System
- Secure login/logout with JWT and remember-me functionality
- Role-based access control with hierarchical permissions
- Password reset and recovery via email verification
- Custom authentication success and access denied handlers
- Session management with configurable timeout
- Automated security key rotation with scheduled tasks

### Product & Category Management
- Complete CRUD operations for products and categories
- Product search and filtering capabilities
- Category hierarchy management
- Product detail management with size and comment support

### Order Management System
- Order creation and processing workflow
- Multiple payment options (Cash and PayPal integration)
- PayPal QR code generation for mobile payments
- Order status tracking and history
- Order detail management with line items
- Order search and filtering capabilities

### User Management
- User registration and profile management
- Role and permission assignment
- User status management (active/inactive)
- Hierarchical role system (Admin, Manager, User)
- Gmail credential management for email services

### Business Reporting & Analytics
- Sales reports by product with visual charts
- Sales reports by user performance
- Order status analytics and tracking
- Dashboard with key business metrics
- Custom reporting with date range filtering
- Export capabilities for business intelligence

### Email Integration
- Gmail SMTP integration for notifications
- Password reset email functionality
- Order confirmation emails
- Custom email templates and messaging

## Project Structure

```
src/main/java/com/AllInSmall/demo/
├── controller/                   # MVC Controllers
│   ├── HomeController.java       # Landing page and navigation
│   ├── LoginController.java      # Authentication flows
│   ├── UserController.java       # User management
│   ├── CategoryController.java   # Category operations
│   ├── ProductController.java    # Product management
│   ├── OrderController.java      # Order processing
│   ├── ReportController.java     # Business reporting
│   └── DashboardController.java  # Analytics dashboard
├── service/                      # Business Logic Layer
│   ├── UserRegistrationService.java
│   ├── LoginService.java
│   ├── MyUserDetailsService.java
│   ├── EmailService.java
│   ├── GmailService.java
│   ├── PayPalService.java
│   └── CategoryService.java
├── repository/                   # Data Access Layer
│   ├── UserRepository.java       # Standard JPA repositories
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   └── custom/                   # Custom repository implementations
│       ├── CustomOrderRepositoryForReportingImpl.java
│       └── MyPersistentTokenRepositoryImpl.java
├── model/                        # Domain Entities
│   ├── User.java, Role.java, Permission.java
│   ├── Product.java, Category.java
│   ├── Order.java, OrderDetail.java
│   ├── VerificationToken.java
│   └── PersistentLogin.java
├── dto/                          # Data Transfer Objects
│   ├── UserRegistrationRequest.java
│   ├── OrdersByStatusDto.java
│   ├── SalesByProductDto.java
│   └── PaypalOrderResponse.java
├── configuration/                # Spring Configuration
│   ├── SecurityConfig.java       # Security filter chain
│   ├── HierarchyConfig.java      # Role hierarchy
│   ├── SessionConfig.java        # Session management
│   ├── RememberMeKeyManager.java # Security key management
│   ├── KeyRotationScheduler.java # Scheduled tasks
│   ├── JavaMailConfig.java       # Email configuration
│   └── InterceptorConfig.java    # Request interceptors
├── exception/                    # Exception Handling
│   └── ProductNotFoundException.java
└── enums/                        # Application Enums
    ├── OrderStatus.java
    └── UserStatus.java

src/main/webapp/WEB-INF/views/    # JSP View Templates
├── login.jsp, registerUser.jsp, forgotPasswordForm.jsp
├── manageUser.jsp, manageCategory.jsp, manageProduct.jsp
├── ordersByStatus.jsp, salesByProduct.jsp, salesByUser.jsp
├── paymentOption.jsp, paypalQRCode.jsp, cashPayment.jsp
└── viewOrderList.jsp, viewOrder.jsp, viewReport.jsp

src/main/resources/
├── static/css/                   # Stylesheets
├── static/js/                    # JavaScript files
└── messages.properties           # Internationalization
```

## Tech Stack

### Core Technologies
- **Spring Boot** - Application framework and auto-configuration
- **Spring MVC** - Web framework with DispatcherServlet
- **Spring Security** - Authentication and authorization framework
- **Spring Data JPA** - Data access and ORM layer
- **JSP (JavaServer Pages)** - Server-side view templating
- **MySQL** - Relational database for data persistence

### Development Tools
- **Maven** - Dependency management and build automation
- **Embedded Tomcat** - Web server and servlet container
- **Spring Boot DevTools** - Development productivity tools
- **JavaMail API** - Email integration capabilities

### External Integrations
- **PayPal REST API** - Payment processing and QR code generation
- **Gmail SMTP** - Email service for notifications and verification
- **Docker** - Containerization for deployment

### Architecture Patterns
- **Model-View-Controller (MVC)** - Separation of concerns in web layer
- **Dependency Injection** - Spring container for bean management
- **Role-Based Access Control (RBAC)** - Hierarchical permission system
- **Custom Security Handlers** - Tailored authentication and authorization flows

## Authentication & Security Flow

1. **User Registration**: Email verification with token-based activation
2. **Login Process**: Credentials validation with optional remember-me functionality
3. **Security Filter Chain**: Multi-layer authentication and authorization filters
4. **Role Authorization**: Hierarchical role system with granular permissions
5. **Session Management**: Configurable session timeout with persistent login tokens
6. **Password Recovery**: Secure email-based password reset workflow

## Responsive Design

- **Server-Side Rendering**: JSP templates with responsive CSS frameworks
- **Mobile-Optimized**: Touch-friendly interfaces for mobile devices
- **Cross-Browser Support**: Compatible with modern web browsers
- **Progressive Enhancement**: Graceful degradation for older browsers

## Business Intelligence Features

- **Real-Time Analytics**: Live dashboard with key performance indicators
- **Sales Reporting**: Comprehensive sales analysis by product and user
- **Order Tracking**: Complete order lifecycle monitoring
- **Data Visualization**: Charts and graphs for business insights
- **Export Functionality**: Data export for external analysis tools

### Code Standards
- Follow Spring Boot best practices and conventions
- Use proper Spring annotations (@Service, @Repository, @Controller)
- Implement proper exception handling with custom exceptions
- Write meaningful method and class names following Java naming conventions
- Use JSP best practices for view layer development
- Ensure proper input validation and sanitization
- Follow security best practices for authentication and authorization
- Add comprehensive JavaDoc comments for public methods
- Implement proper logging using SLF4J
- Use Spring Security annotations for method-level security
- Follow JPA best practices for entity relationships and queries

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Built for comprehensive retail management and e-commerce solutions**
