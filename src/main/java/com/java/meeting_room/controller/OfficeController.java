package com.java.meeting_room.controller;

import com.java.meeting_room.model.request.MasterOfficeReq;
import com.java.meeting_room.model.request.OfficeReq;
import com.java.meeting_room.service.OfficeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @PostMapping("/add-master")
    public ResponseEntity<?> addMasterOffice(@RequestBody MasterOfficeReq req) {
        return officeService.addMasterOffice(req);
    }

    @PostMapping("/add-room")
    public ResponseEntity<?> addOffice(@RequestBody OfficeReq req) {
        return officeService.addOffice(req);
    }
}
