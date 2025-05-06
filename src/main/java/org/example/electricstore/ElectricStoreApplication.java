package org.example.electricstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElectricStoreApplication {

    public static void main(String[] args) {
        // Lấy cổng từ biến môi trường PORT hoặc sử dụng 8080 nếu không có
        String port = System.getenv("PORT");
        if (port != null) {
            System.setProperty("server.port", port);
        }
        SpringApplication.run(ElectricStoreApplication.class, args);
    }
}
