package com.java.meeting_room.model.request;

public record AuthReq (
    String email,
    String password,
    String role
) {
    
}
