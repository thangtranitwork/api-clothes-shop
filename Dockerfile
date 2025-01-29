# Sử dụng OpenJDK làm base image
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file JAR từ thư mục target vào container
COPY target/app.jar app.jar

# Expose cổng 8080
EXPOSE 8080

# Chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
