package org.example.electricstore.repository;

import org.example.electricstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    User findByUsernameWithRoles(@Param("username") String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUserIdNot(String email, Integer userId);

    boolean existsByEmployeePhone(String phone);

    boolean existsByEmployeePhoneAndUserIdNot(String phone, Integer userId);

    @Query("SELECT u FROM User u WHERE " +
            "(:type = 'all' AND (LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR u.employeePhone LIKE CONCAT('%', :keyword, '%'))) " +
            "OR (:type = 'employeeName' AND LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "OR (:type = 'employeePhone' AND u.employeePhone LIKE CONCAT('%', :keyword, '%'))")
    Page<User> searchByKeywordAndType(@Param("keyword") String keyword, @Param("type") String type, Pageable pageable);
    @Query("SELECT u FROM User u WHERE " +
            "(:field = 'name' AND LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:field = 'phone' AND u.employeePhone LIKE CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("field") String field, @Param("keyword") String keyword, Pageable pageable);
    @Query(value = "SELECT employee_code FROM users WHERE employee_code LIKE 'NV%' ORDER BY CAST(SUBSTRING(employee_code, 3) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findMaxEmployeeCode();
}