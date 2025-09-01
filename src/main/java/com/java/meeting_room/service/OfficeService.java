package com.java.meeting_room.service;

import com.java.meeting_room.model.Response;
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
                return Response.responseBadRequest("field wajib diisi.");
            }

            Optional<MasterOffice> existing = masterOfficeRepo.findByOfficeName(req.officeName());
            if (existing.isPresent()) {
                return Response.responseError(
                        Map.of("message", "officeName tidak boleh sama."),
                        409,
                        "Conflict");
            }

            MasterOffice officeBaru = new MasterOffice();
            officeBaru.setOfficeName(req.officeName());
            officeBaru.setCreatedAt(LocalDateTime.now());

            MasterOffice saved = masterOfficeRepo.save(officeBaru);

            Map<String, Object> responseData = Map.of(
                    "id", saved.getId(),
                    "officeName", saved.getOfficeName(),
                    "createdAt", saved.getCreatedAt());

            return Response.responseCreateSukses(responseData);
        } catch (Exception e) {
            return Response.responseError(
                    Map.of("error", e.getMessage()),
                    500,
                    "Terjadi kesalahan pada server.");
        }
    }

    public ResponseEntity<?> addOffice(OfficeReq req) {
        try {
            if (req.roomName() == null || req.id_master_office() == null) {
                return Response.responseBadRequest("field wajib diisi.");
            }

            Optional<MasterOffice> master = masterOfficeRepo.findById(req.id_master_office());
            if (master.isEmpty()) {
                return Response.responseError(
                        Map.of("message", "Master office tidak ditemukan."),
                        404,
                        "Not Found");
            }

            Office office = new Office();
            office.setRoomName(req.roomName());
            office.setMasterOffice(master.get());
            office.setCreatedAt(LocalDateTime.now());

            Office saved = officeRepo.save(office);

            Map<String, Object> responseData = Map.of(
                    "roomName", saved.getRoomName(),
                    "createdAt", saved.getCreatedAt(),
                    "id_master_office", saved.getMasterOffice().getId());

            return Response.responseCreateSukses(responseData);
        } catch (Exception e) {
            return Response.responseError(
                    Map.of("error", e.getMessage()),
                    500,
                    "Terjadi kesalahan pada server.");
        }
    }

    public ResponseEntity<?> editOffice(){
        try {
            
        } catch (Exception e) {
        }
    }

    public ResponseEntity<?> hapusOffice(){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public ResponseEntity<?> listOffice(){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
