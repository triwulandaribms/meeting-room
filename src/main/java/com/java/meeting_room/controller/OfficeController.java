package com.java.meeting_room.controller;

import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.MasterOfficeReq;
import com.java.meeting_room.model.request.OfficeReq;
import com.java.meeting_room.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @GetMapping("/list")
    public Response<Object> listOffice() {
        return officeService.listOffice();
    }

    @PostMapping("/add-master")
    public Response<Object> addMasterOffice(@RequestBody MasterOfficeReq req) {
        return officeService.addMasterOffice(req);
    }

    @PostMapping("/add-room")
    public Response<Object> addOffice(@RequestBody OfficeReq req) {
        return officeService.addOffice(req);
    }

    @PutMapping("/update/{id}")
    public Response<Object> updateOffice(
            @PathVariable Integer id,
            @RequestBody OfficeReq req) {
        return officeService.updateOffice(id, req);
    }
}
