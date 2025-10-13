package com.java.meeting_room.controller;

import com.java.meeting_room.model.request.AuthReq;
import com.java.meeting_room.service.AuthenticateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticateService authenticateService;

    
}
