package org.example.electricstore.config;

import org.example.electricstore.service.impl.UserInforDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserInforDetailService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/login", "/error", "/register", "/", "/logoutSuccessful","/images/**").permitAll()
                                .requestMatchers("/static/**", "/css/**", "/js/**", "/img/**", "/api/**").permitAll()
                                .requestMatchers("/ShopPhone/css/**", "/ShopPhone/js/**", "/ShopPhone/img/**", "/ShopPhone/static/**").permitAll()

                                // Đường dẫn người dùng với vai trò EMPLOYEE có thể truy cập (chỉ xem)
                                .requestMatchers("/Admin/order/**").hasAnyRole("ADMIN", "EMPLOYEE")
                                .requestMatchers("/Admin/customers/**").hasAnyRole("ADMIN", "EMPLOYEE")
                                .requestMatchers("/Admin/suppliers-manager/**").hasAnyRole("ADMIN", "EMPLOYEE")
                                .requestMatchers("/Admin/ware-houses").hasAnyRole("ADMIN", "EMPLOYEE") // Chỉ xem danh sách kho
                                .requestMatchers("/Admin/ware-houses/import", "/Admin/ware-houses/history_warehouse").hasRole("ADMIN") // Chỉ ADMIN được nhập kho và xem lịch sử
                                .requestMatchers("/Admin/statistical/**").hasAnyRole("ADMIN", "EMPLOYEE")

                                // Phân quyền chi tiết cho quản lý hàng hóa
                                .requestMatchers("/Admin/category-manager").hasAnyRole("ADMIN", "EMPLOYEE") // Xem danh sách danh mục
                                .requestMatchers("/Admin/brand-manager").hasAnyRole("ADMIN", "EMPLOYEE") // Xem danh sách thương hiệu
                                .requestMatchers("/Admin/product-manager").hasAnyRole("ADMIN", "EMPLOYEE") // Xem danh sách sản phẩm

                                // Đường dẫn cho tài khoản - cả ADMIN và EMPLOYEE có quyền xem
                                .requestMatchers("/account").hasAnyRole("ADMIN", "EMPLOYEE")
                                // Đường dẫn cập nhật và đổi mật khẩu - chỉ ADMIN có quyền
                                .requestMatchers("/account/update", "/account/change-password").hasRole("ADMIN")

                                // Đường dẫn chỉ ADMIN mới có thể truy cập (thêm, sửa, xóa)
                                .requestMatchers("/Admin/employee-manager/**").hasRole("ADMIN")
                                .requestMatchers("/Admin/brand-manager/add", "/Admin/brand-manager/edit/**", "/Admin/brand-manager/delete/**").hasRole("ADMIN")
                                .requestMatchers("/Admin/category-manager/add", "/Admin/category-manager/edit/**", "/Admin/category-manager/delete/**").hasRole("ADMIN")
                                .requestMatchers("/Admin/product-manager/add", "/Admin/product-manager/edit/**", "/Admin/product-manager/delete/**").hasRole("ADMIN")

                                // Các đường dẫn còn lại
                                .requestMatchers("/Admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/login")
                                .failureUrl("/login?error=true")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/Admin/order", true))
                .logout((logout) ->
                        logout
                                .deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/logoutSuccessful"))
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedPage("/403"));
        return http.build();
    }
}