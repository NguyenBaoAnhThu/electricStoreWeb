package org.example.electricstore.mapper.user;

import org.example.electricstore.DTO.user.UserDTO;
import org.example.electricstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEncrytedPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setEmployeeCode(userDTO.getEmployeeCode());
        user.setEmployeeName(userDTO.getEmployeeName());
        user.setEmployeeBirthday(userDTO.getEmployeeBirthday());
        user.setEmployeeAddress(userDTO.getEmployeeAddress());
        user.setEmployeePhone(userDTO.getEmployeePhone());

        return user;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setEmployeeCode(user.getEmployeeCode());
        userDTO.setEmployeeName(user.getEmployeeName());
        userDTO.setEmployeeBirthday(user.getEmployeeBirthday());
        userDTO.setEmployeeAddress(user.getEmployeeAddress());
        userDTO.setEmployeePhone(user.getEmployeePhone());
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            userDTO.setRole(user.getRoles().get(0).getRoleName().name());
        }

        return userDTO;
    }
}