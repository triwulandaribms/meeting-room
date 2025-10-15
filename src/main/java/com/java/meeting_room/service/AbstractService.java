package com.java.meeting_room.service;

import com.java.meeting_room.entity.SysUser;
import com.java.meeting_room.model.Authentication;
import com.java.meeting_room.model.Response;

import java.util.Optional;

abstract class AbstractService {

    static Optional<Response<Object>> precondition(final Authentication authentication, SysUser.Role... role) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(Response.unauthenticated());
        }
        if (authentication.id() == null || authentication.id() < 0) {
            return Optional.of(Response.unauthorized());
        }
        for (SysUser.Role r : role) {
            if (r == authentication.role()) {
                return Optional.empty();
            }
        }
        return Optional.of(Response.unauthorized());
    }
}