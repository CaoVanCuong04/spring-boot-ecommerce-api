# Spring Boot E-commerce REST API

Dự án backend REST API cho hệ thống thương mại điện tử, được xây dựng bằng Java 17, Spring Boot, SQL Server, Spring Security, JWT và phân quyền theo vai trò.



## Công nghệ sử dụng

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT Authentication
- SQL Server
- Maven
- Swagger / OpenAPI
- Lombok
- Bean Validation

---

## Chức năng chính

### 1. Authentication & Authorization

- Đăng ký tài khoản
- Đăng nhập và nhận JWT token
- Phân quyền theo vai trò:
  - USER
  - SELLER
  - ADMIN
- API đăng ký public đã được bảo vệ khỏi lỗi truyền role từ client:
  - Dù client gửi role `ADMIN`, hệ thống vẫn mặc định tạo tài khoản `USER`.

---

### 2. Category

- Thêm danh mục
- Cập nhật danh mục
- Xóa danh mục
- Xem danh sách danh mục
- Xem chi tiết danh mục

---

### 3. Product

- Thêm sản phẩm
- Cập nhật sản phẩm
- Xóa sản phẩm
- Xem danh sách sản phẩm
- Xem chi tiết sản phẩm
- Tìm kiếm, lọc, phân trang và sắp xếp sản phẩm

Ví dụ API lọc sản phẩm:

```http
GET /api/products?page=0&size=10&keyword=nike&categoryId=1&minPrice=100&maxPrice=500&active=true&sortBy=price&sortDir=asc
4. Cart
Xem giỏ hàng của user hiện tại
Thêm sản phẩm vào giỏ hàng
Cập nhật số lượng sản phẩm trong giỏ hàng
Xóa sản phẩm khỏi giỏ hàng
API giỏ hàng sử dụng JWT, không truyền userId từ client
Chỉ chủ sở hữu cart item mới được sửa hoặc xóa cart item
5. Order
Tạo đơn hàng từ giỏ hàng của user hiện tại
Xem danh sách đơn hàng của user hiện tại
Xem chi tiết đơn hàng
Hủy đơn hàng khi đơn còn trạng thái PENDING
Hoàn lại tồn kho khi hủy đơn hàng PENDING
Chỉ chủ đơn hàng hoặc ADMIN mới được xem đơn hàng
Chỉ ADMIN hoặc SELLER được cập nhật trạng thái đơn hàng
6. Coupon
Tạo mã giảm giá
Quản lý mã giảm giá
Áp dụng mã giảm giá vào đơn hàng
Tính số tiền giảm giá và tổng tiền cuối cùng
7. Payment
Mô phỏng quy trình thanh toán
Thanh toán gắn với đơn hàng
API thanh toán sử dụng JWT
Chỉ chủ payment/order hoặc ADMIN mới được xem thông tin thanh toán
8. Wishlist
Thêm sản phẩm vào danh sách yêu thích
Xem wishlist của user hiện tại
Xóa sản phẩm khỏi wishlist
Chỉ chủ wishlist item mới được xóa wishlist item
9. Review
Tạo đánh giá sản phẩm
Xem đánh giá theo sản phẩm
Xem danh sách đánh giá của user hiện tại
Cập nhật đánh giá của chính mình
Xóa đánh giá của chính mình
ADMIN có quyền xóa mọi đánh giá
User chỉ được đánh giá sản phẩm đã mua
Chuẩn hóa lỗi API

Dự án sử dụng GlobalExceptionHandler để chuẩn hóa response lỗi.

Ví dụ lỗi:

{
  "timestamp": "2026-06-02T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found",
  "path": "/api/products/99"
}

Các lỗi đã xử lý:

400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
500 Internal Server Error
API Documentation

Swagger UI:

http://localhost:8080/swagger-ui/index.html

OpenAPI JSON:

http://localhost:8080/v3/api-docs

Cách test API cần đăng nhập trên Swagger:

Gọi API /api/auth/login
Copy JWT token
Bấm nút Authorize
Nhập token theo dạng:
Bearer your_token_here
Phân quyền API
Chức năng	Quyền truy cập
Register/Login	Public
Xem sản phẩm	Public
Xem danh mục	Public
Xem review theo sản phẩm	Public
Quản lý sản phẩm	SELLER, ADMIN
Quản lý user	ADMIN
Quản lý coupon	ADMIN
Cart	USER đăng nhập
Order	USER đăng nhập
Payment	USER đăng nhập
Wishlist	USER đăng nhập
Review	USER đăng nhập
Cập nhật trạng thái đơn hàng	SELLER, ADMIN
Một số API tiêu biểu
Auth
POST /api/auth/register
POST /api/auth/login
Product
GET /api/products
GET /api/products/{id}
POST /api/products
PUT /api/products/{id}
DELETE /api/products/{id}
Cart
GET /api/cart/me
POST /api/cart/items
PUT /api/cart/items/{cartItemId}
DELETE /api/cart/items/{cartItemId}
Order
POST /api/orders
GET /api/orders/me
GET /api/orders/{orderId}
PUT /api/orders/{orderId}/status
PUT /api/orders/{orderId}/cancel
Payment
POST /api/payments
GET /api/payments/me
GET /api/payments/{paymentId}
Wishlist
GET /api/wishlist/me
POST /api/wishlist/items
DELETE /api/wishlist/items/{wishlistItemId}
Review
POST /api/reviews
GET /api/reviews/product/{productId}
GET /api/reviews/me
PUT /api/reviews/{reviewId}
DELETE /api/reviews/{reviewId}
Cấu hình database

Dự án sử dụng SQL Server.

Ví dụ cấu hình trong application.properties:

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=product_api;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
Cách chạy project
1. Clone project
git clone https://github.com/your-username/your-repository.git
2. Mở project bằng IDE

Có thể dùng:

IntelliJ IDEA
Spring Tool Suite
Eclipse
3. Cấu hình database

Tạo database trong SQL Server:

CREATE DATABASE product_api;

Sau đó sửa thông tin database trong application.properties.

4. Chạy project

Chạy class main:

ProductApiApplication.java

Hoặc chạy bằng Maven:

mvn spring-boot:run
5. Mở Swagger
http://localhost:8080/swagger-ui/index.html
Điểm nổi bật của project
Thiết kế theo mô hình Controller - Service - Repository
Sử dụng DTO cho request/response
JWT authentication
Role-based authorization
Owner check cho các tài nguyên cá nhân
Chuẩn hóa lỗi API bằng Global Exception Handler
Có Swagger/OpenAPI để tài liệu hóa và test API
Có xử lý nghiệp vụ cơ bản của hệ thống e-commerce:
Cart
Order
Coupon
Payment
Wishlist
Review
Có kiểm tra quyền truy cập giữa USER, SELLER và ADMIN