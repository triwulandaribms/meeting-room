package com.java.meeting_room.model;

import com.java.meeting_room.entity.SysUser;

public record Authentication(Long id, SysUser.Role role, boolean isAuthenticated) {
}