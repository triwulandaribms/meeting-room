package com.java.meeting_room.controller;

import com.java.meeting_room.model.Authentication;
import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.MasterOfficeReq;
import com.java.meeting_room.model.request.OfficeReq;
import com.java.meeting_room.service.OfficeService;
import com.java.meeting_room.util.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secured/office")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/list")
    public Response<Object> listOffice() {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return officeService.listOffice(authentication);
    }

    @PostMapping("/add-master")
    public Response<Object> addMasterOffice(@RequestBody MasterOfficeReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return officeService.addMasterOffice(authentication, req);
    }

    @PostMapping("/add-room")
    public Response<Object> addOffice(@RequestBody OfficeReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return officeService.addOffice(authentication, req);
    }

    @PutMapping("/update/{id}")
    public Response<Object> updateOffice(
            @PathVariable Integer id,
            @RequestBody OfficeReq req
    ) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return officeService.updateOffice(authentication, id, req);
    }
}
