package com.edu.fpt.security.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserSecurityRes(
    Integer id,
    String email,
    String password,
    String name,
    String userType,
    Collection<? extends GrantedAuthority> authorities
) {
}
