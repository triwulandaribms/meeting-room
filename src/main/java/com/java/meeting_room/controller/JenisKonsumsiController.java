package com.java.meeting_room.controller;

import com.java.meeting_room.model.request.MasterJenisKonsumsiReq;
import com.java.meeting_room.service.JenisKonsumsiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/konsumsi")
public class JenisKonsumsiController {

    @Autowired
    private JenisKonsumsiService jenisKonsumsiService;

    @PostMapping("/add")
    public ResponseEntity<?> addJenisKonsumsi(@RequestBody MasterJenisKonsumsiReq req) {
        return jenisKonsumsiService.addJenisKonsumsi(req);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJenisKonsumsi(
            @PathVariable Integer id,
            @RequestBody MasterJenisKonsumsiReq req) {
        return jenisKonsumsiService.updateJenisKonsumsi(id, req);
    }

    // @DeleteMapping("/delete")
    // public ResponseEntity<?> deleteJenisKonsumsi(@RequestBody MasterJenisKonsumsiReq req) {
    //     return jenisKonsumsiService.deleteJenisKonsumsi(req);
    // }

    @GetMapping("/list")
    public ResponseEntity<?> listJenisKonsumsi() {
        return jenisKonsumsiService.listJenisKonsumsi();
    }

}
