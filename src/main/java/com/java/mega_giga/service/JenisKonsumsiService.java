package com.java.mega_giga.service;

import com.java.mega_giga.entity.MasterJenisKonsumsi;
import com.java.mega_giga.model.request.MasterJenisKonsumsiReq;
import com.java.mega_giga.repository.MasterJenisKonsumsiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class JenisKonsumsiService {

    @Autowired
    private MasterJenisKonsumsiRepository repository;

    public ResponseEntity<?> addJenisKonsumsi(MasterJenisKonsumsiReq req) {
        try {
            MasterJenisKonsumsi jenisKonsumsi = new MasterJenisKonsumsi();
            jenisKonsumsi.setName(req.name());
            jenisKonsumsi.setMaxPrice(req.maxPrice());
            jenisKonsumsi.setCreatedAt(LocalDateTime.now());

            MasterJenisKonsumsi saved = repository.save(jenisKonsumsi);

            return ResponseEntity.status(201).body(Map.of(
                "message", "Data jenis konsumsi berhasil ditambahkan.",
                "data", Map.of(
                    "id", saved.getId(),
                    "name", saved.getName(),
                    "maxPrice", saved.getMaxPrice(),
                    "createdAt", saved.getCreatedAt()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Terjadi kesalahan pada server.",
                "error", e.getMessage()
            ));
        }
    }
}
