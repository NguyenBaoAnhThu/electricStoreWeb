package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.user.UserDTO;
import org.example.electricstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IUserService {
    List<User> findByIds(List<Integer> userIds);
    List<String> getUserNamesByIds(List<Integer> userIds);
    void saveAll(List<User> users);
    void deleteByIds(List<Integer> userIds);
    Page<User> searchByFieldAndKeyword(String field, String keyword, int page, int size);
    Page<User> findAll(int page, int size);
    void save(UserDTO userDTO);
    void update(UserDTO userDTO, BindingResult bindingResult);
    UserDTO findDTOById(int id);
    Boolean findById(int id);
    boolean existedByPhone(String phone);
    void updateSimple(UserDTO userDTO);
    String generateNextEmployeeCode();

}