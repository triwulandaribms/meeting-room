package com.java.meeting_room.model;

import com.java.meeting_room.entity.User;

public record Authentication(Long id, User.Role role, boolean isAuthenticated) {
}