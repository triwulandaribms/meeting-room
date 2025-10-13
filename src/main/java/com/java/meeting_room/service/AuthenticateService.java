package com.java.meeting_room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.meeting_room.repository.SysUserRepository;



@Service
public class AuthenticateService {

    @Autowired
    private SysUserRepository sysUserRepository;
    
}
