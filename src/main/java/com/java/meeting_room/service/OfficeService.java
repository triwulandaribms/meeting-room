package com.java.meeting_room.service;

import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.MasterOfficeReq;
import com.java.meeting_room.model.request.OfficeReq;
import com.java.meeting_room.model.response.officeDto;
import com.java.meeting_room.entity.MasterOffice;
import com.java.meeting_room.entity.Office;
import com.java.meeting_room.repository.MasterOfficeRepository;
import com.java.meeting_room.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OfficeService {

    @Autowired
    private MasterOfficeRepository masterOfficeRepo;

    @Autowired
    private OfficeRepository officeRepo;

    public Response<Object> listOffice() {
        try {
            List<officeDto> list = officeRepo.findAllOfficeActive();

            if (list == null || list.isEmpty()) {
                return Response.create("05", "04", "Data tidak ditemukan", null);
            }

            return Response.create("05", "00", "Sukses", Map.of("list", list));
        } catch (Exception e) {
            return Response.create("05", "99", "Terjadi kesalahan pada server: " + e.getMessage(), null);
        }
    }

    public Response<Object> addMasterOffice(MasterOfficeReq req) {
        try {
            if (req.officeName() == null || req.officeName().trim().isEmpty()) {
                return Response.badRequest();
            }

            Optional<MasterOffice> existing = masterOfficeRepo.findByOfficeName(req.officeName());
            if (existing.isPresent()) {
                return Response.create("05", "02", "officeName tidak boleh sama", null);
            }

            MasterOffice officeBaru = new MasterOffice();
            officeBaru.setOfficeName(req.officeName());
            officeBaru.setCreatedAt(LocalDateTime.now());

            MasterOffice saved = masterOfficeRepo.save(officeBaru);

            Map<String, Object> data = Map.of(
                    "id", saved.getId(),
                    "officeName", saved.getOfficeName(),
                    "createdAt", saved.getCreatedAt());

            return Response.create("05", "00", "Sukses membuat master office", data);
        } catch (Exception e) {
            return Response.create("05", "99", "Terjadi kesalahan pada server: " + e.getMessage(), null);
        }
    }

    public Response<Object> addOffice(OfficeReq req) {
        try {
            if (req.roomName() == null || req.id_master_office() == null) {
                return Response.badRequest();
            }

            Optional<MasterOffice> master = masterOfficeRepo.findById(req.id_master_office());
            if (master.isEmpty()) {
                return Response.create("05", "04", "Master office tidak ditemukan", null);
            }

            Office office = new Office();
            office.setRoomName(req.roomName());
            office.setMasterOffice(master.get());
            office.setCreatedAt(LocalDateTime.now());

            Office saved = officeRepo.save(office);

            Map<String, Object> data = Map.of(
                    "roomName", saved.getRoomName(),
                    "id_master_office", saved.getMasterOffice().getId(),
                    "createdAt", saved.getCreatedAt());

            return Response.create("05", "00", "Sukses menambahkan office", data);
        } catch (Exception e) {
            return Response.create("05", "99", "Terjadi kesalahan pada server: " + e.getMessage(), null);
        }
    }

    public Response<Object> updateOffice(Integer id, OfficeReq req) {
        try {
            if (req.roomName() == null || req.id_master_office() == null) {
                return Response.badRequest();
            }

            Optional<Office> officeOpt = officeRepo.findOfficeById(id);
            if (officeOpt.isEmpty()) {
                return Response.create("05", "04", "Office dengan id " + id + " tidak ditemukan", null);
            }

            Optional<MasterOffice> masterOpt = masterOfficeRepo.findMasterOfficeById(req.id_master_office());
            if (masterOpt.isEmpty()) {
                return Response.create("05", "04", "MasterOffice dengan id " + req.id_master_office() + " tidak ditemukan", null);
            }

            Office office = officeOpt.get();
            office.setRoomName(req.roomName().trim());
            office.setMasterOffice(masterOpt.get());
            office.setUpdatedAt(LocalDateTime.now());

            Office saved = officeRepo.save(office);

            Map<String, Object> data = Map.of(
                    "id", saved.getId(),
                    "roomName", saved.getRoomName(),
                    "id_master_office", saved.getMasterOffice().getId());

            return Response.create("05", "00", "Data office berhasil diupdate", data);
        } catch (Exception e) {
            return Response.create("05", "99", "Terjadi kesalahan pada server: " + e.getMessage(), null);
        }
    }
}
