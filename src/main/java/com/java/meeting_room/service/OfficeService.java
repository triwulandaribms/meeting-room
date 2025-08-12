package com.java.meeting_room.service;

import com.java.meeting_room.model.request.MasterOfficeReq;
import com.java.meeting_room.model.request.OfficeReq;
import com.java.meeting_room.entity.MasterOffice;
import com.java.meeting_room.entity.Office;
import com.java.meeting_room.repository.MasterOfficeRepository;
import com.java.meeting_room.repository.OfficeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OfficeService {

    @Autowired
    private MasterOfficeRepository masterOfficeRepo;

    @Autowired
    private OfficeRepository officeRepo;

    public ResponseEntity<?> addMasterOffice(MasterOfficeReq req) {
        try {
            if (req.officeName() == null || req.officeName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "field wajib diisi."));
            }

            Optional<MasterOffice> existing = masterOfficeRepo.findByOfficeName(req.officeName());
            if (existing.isPresent()) {
                return ResponseEntity.status(409).body(Map.of("message", "officeName tidak boleh sama."));
            }

            MasterOffice newOffice = new MasterOffice();
            newOffice.setOfficeName(req.officeName());
            newOffice.setCreatedAt(LocalDateTime.now());

            MasterOffice saved = masterOfficeRepo.save(newOffice);

            return ResponseEntity.status(201).body(Map.of(
                "message", "Data master office berhasil ditambahkan.",
                "data", Map.of(
                    "id", saved.getId(),
                    "officeName", saved.getOfficeName(),
                    "createdAt", saved.getCreatedAt()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Terjadi kesalahan pada server.", "error", e.getMessage()));
        }
    }

    public ResponseEntity<?> addOffice(OfficeReq req) {
        try {
            if (req.roomName() == null || req.id_master_office() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "field wajib diisi."));
            }

            Optional<MasterOffice> master = masterOfficeRepo.findById(req.id_master_office());
            if (master.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("message", "Master office tidak ditemukan."));
            }

            Office office = new Office();
            office.setRoomName(req.roomName());
            office.setMasterOffice(master.get());
            office.setCreatedAt(LocalDateTime.now());

            Office saved = officeRepo.save(office);

            return ResponseEntity.status(201).body(Map.of(
                "message", "Data office berhasil ditambahkan.",
                "data", Map.of(
                    "roomName", saved.getRoomName(),
                    "createdAt", saved.getCreatedAt(),
                    "id_master_office", saved.getMasterOffice().getId()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Terjadi kesalahan pada server.", "error", e.getMessage()));
        }
    }
}
