# NOC Clothes Shop API

Dự án cung cấp API cho hệ thống cửa hàng quần áo trực tuyến, được xây dựng bằng Java Spring Boot. Hệ thống hỗ trợ đầy đủ các tính năng từ quản lý sản phẩm, giỏ hàng đến xử lý đơn hàng và xác thực người dùng.

## 🚀 Công nghệ sử dụng

*   **Ngôn ngữ:** Java 17
*   **Framework:** Spring Boot 3.3.0
*   **Cơ sở dữ liệu:** MySQL
*   **ORM:** Spring Data JPA
*   **Bảo mật:** Spring Security (OAuth2, JWT)
*   **Quản lý hình ảnh:** Cloudinary
*   **Email:** Spring Boot Starter Mail
*   **Công cụ khác:** Lombok, MapStruct, Maven, Docker

## ✨ Tính năng chính

### 👤 Người dùng (Customer)
*   **Xác thực:** Đăng ký, đăng nhập, quên mật khẩu (xác thực qua mã code gửi tới Email).
*   **OAuth2:** Hỗ trợ đăng nhập nhanh qua Google và Facebook.
*   **Sản phẩm:** Xem danh sách sản phẩm, lọc theo loại, xem chi tiết biến thể (màu sắc, kích cỡ).
*   **Giỏ hàng:** Thêm/sửa/xóa sản phẩm trong giỏ hàng.
*   **Đơn hàng:** Đặt hàng và theo dõi trạng thái đơn hàng cá nhân.

### 🛠️ Quản trị (Admin)
*   **Quản lý sản phẩm:** Thêm mới, cập nhật thông tin sản phẩm và các biến thể.
*   **Quản lý thuộc tính:** Quản lý danh mục loại sản phẩm, bảng màu và bảng kích thước.
*   **Quản lý đơn hàng:** Tiếp nhận, xử lý và cập nhật trạng thái đơn hàng.
*   **Quản lý hình ảnh:** Tải lên và quản lý ảnh sản phẩm thông qua Cloudinary.

## 🛠️ Cài đặt và Chạy dự án

### Yêu cầu hệ thống
*   Java 17 trở lên
*   MySQL Server
*   Docker (tùy chọn)

### Các bước thực hiện

1.  **Clone dự án:**
    ```bash
    git clone https://github.com/thangtranitwork/api-clothes-shop.git
    cd api-clothes-shop
    ```

2.  **Cấu hình môi trường:**
    Tạo file `.env` (dựa trên mẫu hoặc cấu hình trực tiếp trong `application.yml`) với các thông tin sau:
    *   `DB_URL`: URL kết nối MySQL.
    *   `DB_USERNAME`: Tên đăng nhập database.
    *   `DB_PASSWORD`: Mật khẩu database.
    *   `CLOUDINARY_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`: Thông tin cấu hình Cloudinary.
    *   `MAIL_USERNAME`, `MAIL_PASSWORD`: Cấu hình SMTP để gửi mail.

3.  **Chạy dự án bằng Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Sử dụng Docker (nếu có):**
    ```bash
    docker-compose up --build
    ```

## 📂 Cấu trúc thư mục chính
*   `src/main/java/com/clothes/noc/controller`: Xử lý các API endpoint cho người dùng.
*   `src/main/java/com/clothes/noc/admin/controller`: Xử lý các API endpoint dành cho quản trị viên.
*   `src/main/java/com/clothes/noc/service`: Chứa logic nghiệp vụ của hệ thống.
*   `src/main/java/com/clothes/noc/entity`: Định nghĩa các thực thể dữ liệu (Database model).
*   `src/main/java/com/clothes/noc/repository`: Interface tương tác với cơ sở dữ liệu.

## 📞 Liên hệ
*   **Tác giả:** Thắng Trần
*   **Email:** thangtranitwork@gmail.com
*   **GitHub:** [thangtranitwork](https://github.com/thangtranitwork)
