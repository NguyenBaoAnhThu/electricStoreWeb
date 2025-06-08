package org.example.electricstore.service.impl;

import org.example.electricstore.DTO.user.UserDTO;
import org.example.electricstore.enums.RoleEnums;
import org.example.electricstore.mapper.user.UserMapper;
import org.example.electricstore.model.Role;
import org.example.electricstore.model.User;
import org.example.electricstore.repository.IRoleRepository;
import org.example.electricstore.repository.IUserRepository;
import org.example.electricstore.service.interfaces.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final IRoleRepository roleRepository;

    public UserService(IUserRepository userRepository,
                       UserMapper userMapper,
                       IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findByIds(List<Integer> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public List<String> getUserNamesByIds(List<Integer> userIds) {
        return userRepository.findAllById(userIds).stream().map(User::getEmployeeName).toList();
    }

    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> userIds) {
        userRepository.deleteAllById(userIds);
    }

    @Override
    public Page<User> searchByFieldAndKeyword(String field, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if ("work".equals(field)) {
            return this.userRepository.searchByKeywordAndType(keyword, "employeeWork", pageable);
        }
        return this.userRepository.searchUsers(field, keyword, pageable);
    }

    @Override
    public Page<User> findAll(int page, int size) {
        return this.userRepository.findAll(PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional
    public void save(UserDTO userDTO) {
        if (this.userRepository.existsByEmployeePhone(userDTO.getEmployeePhone())) {
            throw new EntityNotFoundException("Phone number already exists");
        }
        if (this.userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EntityNotFoundException("Email already exists");
        }
        if (this.userRepository.existsByUsername(userDTO.getUsername())) {
            throw new EntityNotFoundException("Username already exists");
        }

        User user = this.userMapper.convertToUser(userDTO);

        user.setEncrytedPassword(passwordEncoder.encode(userDTO.getPassword()));

        List<Role> roleList = new ArrayList<>();
        RoleEnums roleEnum = (userDTO.getRole() != null && userDTO.getRole().equals("ROLE_ADMIN"))
                ? RoleEnums.ROLE_ADMIN
                : RoleEnums.ROLE_EMPLOYEE;

        Role defaultRole = this.roleRepository.findByRoleName(roleEnum);
        roleList.add(defaultRole);
        user.setRoles(roleList);

        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(UserDTO userDTO, BindingResult bindingResult) {
        User user = this.userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getEmail().equals(userDTO.getEmail())) {
            boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
            if (emailExists) {
                if (bindingResult != null) {
                    bindingResult.rejectValue("email", "error.userDTO", "Email đã tồn tại!");
                    return;
                } else {
                    throw new EntityNotFoundException("Email đã tồn tại!");
                }
            }
            user.setEmail(userDTO.getEmail());
        }

        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            user.setEncrytedPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (!user.getEmployeePhone().equals(userDTO.getEmployeePhone())) {
            boolean phoneExists = this.userRepository.existsByEmployeePhoneAndUserIdNot(
                    userDTO.getEmployeePhone(), userDTO.getUserId());
            if (phoneExists) {
                if (bindingResult != null) {
                    bindingResult.rejectValue("employeePhone", "error.userDTO", "Số điện thoại đã tồn tại!");
                    return;
                } else {
                    throw new EntityNotFoundException("Số điện thoại đã tồn tại!");
                }
            }
        }

        if (!user.getEmployeeBirthday().equals(userDTO.getEmployeeBirthday()) &&
                userDTO.getEmployeeBirthday().getYear() >= 2010) {
            if (bindingResult != null) {
                bindingResult.rejectValue("employeeBirthday", "error.userDTO", "Ngày sinh phải lớn hơn 15 tuổi!");
                return;
            } else {
                throw new EntityNotFoundException("Ngày sinh phải lớn hơn 15 tuổi!");
            }
        }

        user.setEmployeeName(userDTO.getEmployeeName());
        user.setEmployeeBirthday(userDTO.getEmployeeBirthday());
        user.setEmployeePhone(userDTO.getEmployeePhone());
        user.setEmployeeAddress(userDTO.getEmployeeAddress());

        this.userRepository.save(user);

        if (userDTO.getRole() != null) {
            RoleEnums roleEnum = userDTO.getRole().equals("ROLE_ADMIN")
                    ? RoleEnums.ROLE_ADMIN
                    : RoleEnums.ROLE_EMPLOYEE;

            Role role = this.roleRepository.findByRoleName(roleEnum);
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);

            this.userRepository.save(user);
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setEncrytedPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    }

    @Override
    public UserDTO findDTOById(int id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return this.userMapper.convertToUserDTO(user);
    }

    @Override
    public Boolean findById(int id) {
        return this.userRepository.existsById(id);
    }

    @Override
    public boolean existedByPhone(String phone) {
        return this.userRepository.existsByEmployeePhone(phone);
    }

    @Transactional
    public void updateSimple(UserDTO userDTO) {
        User user = this.userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEmail(userDTO.getEmail());

        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setEncrytedPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user.setEmployeeName(userDTO.getEmployeeName());
        user.setEmployeeBirthday(userDTO.getEmployeeBirthday());
        user.setEmployeePhone(userDTO.getEmployeePhone());
        user.setEmployeeAddress(userDTO.getEmployeeAddress());

        this.userRepository.save(user);
    }
    @Override
    public String generateNextEmployeeCode() {
        List<User> users = userRepository.findAll();
        int maxNumber = 0;

        for (User user : users) {
            String code = user.getEmployeeCode();
            if (code != null && code.startsWith("NV")) {
                try {
                    int currentNumber = Integer.parseInt(code.substring(2));
                    if (currentNumber > maxNumber) {
                        maxNumber = currentNumber;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return String.format("NV%04d", maxNumber + 1);
    }
}