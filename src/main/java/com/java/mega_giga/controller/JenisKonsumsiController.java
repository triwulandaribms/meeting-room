package com.java.mega_giga.controller;

import com.java.mega_giga.model.request.MasterJenisKonsumsiReq;
import com.java.mega_giga.service.JenisKonsumsiService;

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

}
