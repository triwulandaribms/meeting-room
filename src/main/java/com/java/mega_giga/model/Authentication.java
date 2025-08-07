package com.java.mega_giga.model;

import com.java.mega_giga.entity.User;

public record Authentication(Long id, User.Role role, boolean isAuthenticated) {
}