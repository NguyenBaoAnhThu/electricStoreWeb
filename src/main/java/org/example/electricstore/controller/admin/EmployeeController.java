package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.user.UserDTO;
import org.example.electricstore.model.User;
import org.example.electricstore.repository.IUserRepository;
import org.example.electricstore.service.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Admin")
public class EmployeeController {

    private final IUserService userService;
    private final IUserRepository userRepository;

    public EmployeeController(IUserService userService,
                              IUserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/employee-manager")
    public ModelAndView employeeList(@RequestParam(name = "searchField", required = false) String field,
                                     @RequestParam(name = "searchInput",
                                             required = false,
                                             defaultValue = "") String keyword,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("admin/employee/listEmployee");

        Page<User> usersPage = this.userService.findAll(page, size);

        String filterKeyWord = keyword.trim();
        if (!filterKeyWord.isEmpty() && field != null) {
            usersPage = this.userService.searchByFieldAndKeyword(field, filterKeyWord, page, size);
        }

        // Thêm thông báo khi không có kết quả tìm kiếm
        if (!filterKeyWord.isEmpty() && usersPage.isEmpty()) {
            modelAndView.addObject("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
        }

        modelAndView.addObject("employees", usersPage);
        modelAndView.addObject("field", field);
        modelAndView.addObject("filterKeyWord", filterKeyWord);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", usersPage.getTotalPages());
        modelAndView.addObject("userDTO", new UserDTO());

        return modelAndView;
    }

    @PostMapping("/employee-manager/create")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            if (this.userRepository.existsByEmail(userDTO.getEmail())) {
                errors.put("email", "Email đã tồn tại");
            }

            if (this.userRepository.existsByEmployeePhone(userDTO.getEmployeePhone())) {
                errors.put("employeePhone", "Số điện thoại đã tồn tại");
            }

            if (this.userRepository.existsByUsername(userDTO.getUsername())) {
                errors.put("username", "Tên đăng nhập đã tồn tại!");
            }

            if (!errors.isEmpty()) {
                errors.put("globalError", "Email hoặc số điện thoại đã tồn tại. Vui lòng nhập lại!");
                return ResponseEntity.badRequest().body(errors);
            }

            // Đảm bảo mã nhân viên luôn được đặt đúng
            if (userDTO.getEmployeeCode() == null || userDTO.getEmployeeCode().isEmpty()) {
                userDTO.setEmployeeCode(userService.generateNextEmployeeCode());
            }

            // Thêm dòng này để lưu nhân viên vào database
            this.userService.save(userDTO);

            return ResponseEntity.ok("Thêm nhân viên thành công!");
        } catch (Exception e) {
            e.printStackTrace(); // In stack trace để debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("globalError", "Đã có lỗi xảy ra: " + e.getMessage()));
        }
    }
    @PostMapping("/employee-manager/edit")
    public ResponseEntity<?> editEmployee(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // Kiểm tra email đã tồn tại chưa (trừ email của user hiện tại)
            if (userRepository.existsByEmailAndUserIdNot(userDTO.getEmail(), userDTO.getUserId())) {
                errors.put("email", "Email đã tồn tại");
            }

            // Kiểm tra số điện thoại đã tồn tại chưa (trừ số điện thoại của user hiện tại)
            if (userRepository.existsByEmployeePhoneAndUserIdNot(userDTO.getEmployeePhone(), userDTO.getUserId())) {
                errors.put("employeePhone", "Số điện thoại đã tồn tại");
            }

            if (!errors.isEmpty()) {
                errors.put("globalError", "Email hoặc số điện thoại đã tồn tại. Vui lòng nhập lại!");
                return ResponseEntity.badRequest().body(errors);
            }

            // Cập nhật thông tin user
            userService.update(userDTO, bindingResult);

            return ResponseEntity.ok("Cập nhật nhân viên thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("globalError", "Đã có lỗi xảy ra: " + e.getMessage()));
        }
    }

    @PostMapping("/employee-manager/delete")
    public ResponseEntity<?> deleteEmployees(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> userIds = request.get("userIds");

        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không có nhân viên nào được chọn"));
        }

        try {
            userService.deleteByIds(userIds);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xóa nhân viên thành công");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Lỗi: " + e.getMessage()));
        }
    }
    @GetMapping("/employee-manager/generate-code")
    @ResponseBody
    public String generateEmployeeCode() {
        return userService.generateNextEmployeeCode();
    }
    @GetMapping("/employee-manager/check-email")
    @ResponseBody
    public ResponseEntity<?> checkEmailExists(@RequestParam String email, @RequestParam(required = false) Integer id) {
        boolean exists;
        if (id != null) {
            exists = userRepository.existsByEmailAndUserIdNot(email, id);
        } else {
            exists = userRepository.existsByEmail(email);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee-manager/check-username")
    @ResponseBody
    public ResponseEntity<?> checkUsernameExists(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

}