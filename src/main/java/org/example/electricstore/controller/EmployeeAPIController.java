package org.example.electricstore.controller;

import org.example.electricstore.DTO.user.UserDTO;
import org.example.electricstore.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeAPIController {

    @Autowired
    private IUserService userService;

    @GetMapping("/getUserById/{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.findDTOById(id);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> edit(@RequestBody UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            userService.update(userDTO, null);

            response.put("status", "success");
            response.put("message", "Cập nhật thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}