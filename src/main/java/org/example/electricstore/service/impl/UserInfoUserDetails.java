package org.example.electricstore.service.impl;

import org.example.electricstore.model.Role;
import org.example.electricstore.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserInfoUserDetails implements UserDetails {
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getEncrytedPassword();
        this.authorities = new ArrayList<>();

        // Lấy danh sách role trực tiếp từ user
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                // ROLE_ADMIN, ROLE_EMPLOYEE
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName().name());
                authorities.add(authority);
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}