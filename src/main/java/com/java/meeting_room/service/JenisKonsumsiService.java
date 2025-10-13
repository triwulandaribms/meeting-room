package com.java.meeting_room.service;

import com.java.meeting_room.entity.MasterJenisKonsumsi;
import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.MasterJenisKonsumsiReq;
import com.java.meeting_room.model.response.JenisKonsumsiDto;
import com.java.meeting_room.repository.MasterJenisKonsumsiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class JenisKonsumsiService {

    @Autowired
    private MasterJenisKonsumsiRepository masterJenisKonsumsiRepo;

    public Response<Object> listJenisKonsumsi() {
        try {
            List<JenisKonsumsiDto> list = masterJenisKonsumsiRepo.findAllActive();
            if (list == null || list.isEmpty()) {
                return Response.create("06", "04", "Data tidak ditemukan", null);
            }
            return Response.create("06", "00", "Sukses", Map.of("list", list));
        } catch (Exception e) {
            return Response.create("06", "99", "Terjadi kesalahan server: " + e.getMessage(), null);
        }
    }

    public Response<Object> addJenisKonsumsi(MasterJenisKonsumsiReq req) {
        try {
            if (req.name() == null || req.name().isBlank() || req.maxPrice() == null) {
                return Response.badRequest();
            }

            if (!masterJenisKonsumsiRepo.cekName(req.name().trim()).isEmpty()) {
                return Response.create("06", "02", "Nama jenis konsumsi sudah ada", null);
            }

            MasterJenisKonsumsi konsumsi = new MasterJenisKonsumsi();
            konsumsi.setName(req.name().trim());
            konsumsi.setMaxPrice(req.maxPrice());
            konsumsi.setCreatedAt(LocalDateTime.now());

            MasterJenisKonsumsi saved = masterJenisKonsumsiRepo.save(konsumsi);

            Map<String, Object> data = Map.of(
                    "id", saved.getId(),
                    "name", saved.getName(),
                    "maxPrice", saved.getMaxPrice());

            return Response.create("06", "00", "Sukses menambahkan jenis konsumsi", data);
        } catch (Exception e) {
            return Response.create("06", "99", "Terjadi kesalahan server: " + e.getMessage(), null);
        }
    }

    public Response<Object> updateJenisKonsumsi(Integer id, MasterJenisKonsumsiReq req) {
        try {
            Optional<MasterJenisKonsumsi> dataOpt = masterJenisKonsumsiRepo.findById(id);
            if (dataOpt.isEmpty()) {
                return Response.create("06", "04", "Data jenis konsumsi tidak ditemukan", null);
            }

            MasterJenisKonsumsi konsumsi = dataOpt.get();
            konsumsi.setName(req.name());
            konsumsi.setMaxPrice(req.maxPrice());
            masterJenisKonsumsiRepo.save(konsumsi);

            return Response.create("06", "00", "Sukses update jenis konsumsi", konsumsi);
        } catch (Exception e) {
            return Response.create("06", "99", "Terjadi kesalahan server: " + e.getMessage(), null);
        }
    }

    public Response<Object> deleteJenisKonsumsi(Integer id) {
        try {
            Optional<MasterJenisKonsumsi> dataOpt = masterJenisKonsumsiRepo.findActiveById(id);
            if (dataOpt.isEmpty()) {
                return Response.create("06", "04", "Data tidak ditemukan atau sudah dihapus", null);
            }

            MasterJenisKonsumsi data = dataOpt.get();
            data.setDeletedAt(LocalDateTime.now());
            masterJenisKonsumsiRepo.save(data);

            return Response.create("06", "00", "Data jenis konsumsi berhasil dihapus", null);
        } catch (Exception e) {
            return Response.create("06", "99", "Terjadi kesalahan server: " + e.getMessage(), null);
        }
    }
}
