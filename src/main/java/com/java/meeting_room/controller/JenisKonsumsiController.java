package com.java.meeting_room.controller;

import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.MasterJenisKonsumsiReq;
import com.java.meeting_room.service.JenisKonsumsiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/konsumsi")
public class JenisKonsumsiController {

    @Autowired
    private JenisKonsumsiService jenisKonsumsiService;

    @GetMapping("/list")
    public Response<Object> listJenisKonsumsi() {
        return jenisKonsumsiService.listJenisKonsumsi();
    }

    @PostMapping("/add")
    public Response<Object> addJenisKonsumsi(@RequestBody MasterJenisKonsumsiReq req) {
        return jenisKonsumsiService.addJenisKonsumsi(req);
    }

    @PutMapping("/update/{id}")
    public Response<Object> updateJenisKonsumsi(
            @PathVariable Integer id,
            @RequestBody MasterJenisKonsumsiReq req) {
        return jenisKonsumsiService.updateJenisKonsumsi(id, req);
    }

    @DeleteMapping("/delete/{id}")
    public Response<Object> deleteJenisKonsumsi(@PathVariable Integer id) {
        return jenisKonsumsiService.deleteJenisKonsumsi(id);
    }
}
