package org.example.electricstore.config;

import org.example.electricstore.model.User;
import org.example.electricstore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collection;

@Controller
public class SecurityController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login")
    public String loginPage(Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping(value = "/home")
    public String homePage(Authentication authentication, Model model) {
        String username = authentication.getName(); // Lấy tên người dùng đã đăng nhập

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_EMPLOYEE"); // Nếu không có vai trò nào thì gán mặc định là ROLE_EMPLOYEE

        model.addAttribute("username", username);
        model.addAttribute("role", role);

        // Điều hướng đơn giản hóa - chỉ còn ADMIN và EMPLOYEE
        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE")) {
            return "redirect:/Admin/order"; // Chuyển hướng trực tiếp đến trang đơn hàng
        } else {
            return "redirect:/403"; // Không có quyền
        }
    }

    @GetMapping(value = "/logoutSuccessful")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "login";
    }

    @GetMapping(value = "/403")
    public String view403Page() {
        return "error";
    }

    @GetMapping("/account")
    public String viewAccount(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }
        model.addAttribute("user", user);
        return "account";
    }

    @PostMapping("/account/update")
    public String updateAccount(
            Authentication authentication,
            @RequestParam String email,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate employeeBirthday,
            @RequestParam(required = false) String employeePhone,
            @RequestParam(required = false) String employeeAddress,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!hasRole("ROLE_ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền cập nhật thông tin!");
            return "redirect:/account";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }

        user.setEmail(email);

        if (employeeName != null && !employeeName.trim().isEmpty()) {
            user.setEmployeeName(employeeName);
        }

        user.setEmployeeBirthday(employeeBirthday);

        if (employeePhone != null && !employeePhone.trim().isEmpty()) {
            user.setEmployeePhone(employeePhone);
        }

        if (employeeAddress != null && !employeeAddress.trim().isEmpty()) {
            user.setEmployeeAddress(employeeAddress);
        }

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Cập nhật tài khoản thành công!");
        return "redirect:/account";
    }

    @PostMapping("/account/change-password")
    public String changePassword(
            Authentication authentication,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!hasRole("ROLE_ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thay đổi mật khẩu!");
            return "redirect:/account";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }

        if (!passwordEncoder.matches(oldPassword, user.getEncrytedPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
            return "redirect:/account";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không trùng khớp!");
            return "redirect:/account";
        }

        user.setEncrytedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Thay đổi mật khẩu thành công!");
        return "redirect:/account";
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}