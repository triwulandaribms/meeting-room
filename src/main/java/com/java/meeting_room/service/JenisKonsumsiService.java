package com.java.meeting_room.service;

import com.java.meeting_room.entity.MasterJenisKonsumsi;
import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.MasterJenisKonsumsiReq;
import com.java.meeting_room.model.response.JenisKonsumsiDto;
import com.java.meeting_room.repository.MasterJenisKonsumsiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class JenisKonsumsiService {

    @Autowired
    private MasterJenisKonsumsiRepository masterJenisKonsumsiRepo;


    public ResponseEntity<?> listJenisKonsumsi() {
        try {

            List<JenisKonsumsiDto> list = masterJenisKonsumsiRepo.findAllActive();

            return Response.responseSuksesList(list);

        } catch (Exception e) {
            return Response.responseError(
                    Map.of("error", e.getMessage()),
                    500,
                    "Terjadi kesalahan pada server"
            );
        }
    }

    public ResponseEntity<?> addJenisKonsumsi(MasterJenisKonsumsiReq req) {
        try {

            if (req.name() == null || req.name().trim().isEmpty()) {
                return Response.responseBadRequest("Nama jenis konsumsi tidak boleh kosong");
            }
            if (req.maxPrice() == null) {
                return Response.responseBadRequest("Harga maksimum tidak boleh kosong");
            }

            if (!masterJenisKonsumsiRepo.cekName(req.name().trim()).isEmpty()) {
                return Response.responseBadRequest("Nama jenis konsumsi sudah ada");
            }

            MasterJenisKonsumsi jenisKonsumsi = new MasterJenisKonsumsi();
            jenisKonsumsi.setName(req.name().trim());
            jenisKonsumsi.setMaxPrice(req.maxPrice());
            jenisKonsumsi.setCreatedAt(LocalDateTime.now());

            MasterJenisKonsumsi saved = masterJenisKonsumsiRepo.save(jenisKonsumsi);

            return Response.responseCreateSukses(Map.of(
                    "id", saved.getId(),
                    "name", saved.getName(),
                    "maxPrice", saved.getMaxPrice(),
                    "createdAt", saved.getCreatedAt()));

        } catch (Exception e) {
            return Response.responseError(
                    Map.of("error", e.getMessage()), 500, "Terjadi kesalahan pada server");
        }
    }

    public ResponseEntity<?> updateJenisKonsumsi(Integer id, MasterJenisKonsumsiReq req) {
        try {
            if (req.name() == null || req.name().trim().isEmpty()) {
                return Response.responseBadRequest("Nama jenis konsumsi tidak boleh kosong");
            }
            if (req.maxPrice() == null) {
                return Response.responseBadRequest("Harga maksimum tidak boleh kosong");
            }

            var cekId = masterJenisKonsumsiRepo.findById(id);
            if (cekId.isEmpty()) {
                return Response.responseBadRequest("Data jenis konsumsi dengan ID tersebut tidak ditemukan");
            }

            if (!masterJenisKonsumsiRepo.cekName(req.name().trim()).isEmpty()) {
                return Response.responseBadRequest("Nama jenis konsumsi sudah ada");
            }

            MasterJenisKonsumsi jenisKonsumsi = cekId.get();
            jenisKonsumsi.setName(req.name().trim());
            jenisKonsumsi.setMaxPrice(req.maxPrice());

            MasterJenisKonsumsi saved = masterJenisKonsumsiRepo.save(jenisKonsumsi);

            return Response.responseSukses(Map.of(
                    "id", saved.getId(),
                    "name", saved.getName(),
                    "maxPrice", saved.getMaxPrice()), "Data jenis konsumsi berhasil diupdate");

        } catch (Exception e) {
            return Response.responseError(
                    Map.of("error", e.getMessage()), 500, "Terjadi kesalahan pada server");
        }
    }

    public ResponseEntity<?> deleteJenisKonsumsi(Integer id) {
        try {
            Optional<MasterJenisKonsumsi> cekData = masterJenisKonsumsiRepo.findActiveById(id);
    
            if (cekData.isEmpty()) {
                return Response.responseBadRequest("Data jenis konsumsi tidak ditemukan atau sudah dihapus");
            }
    
            MasterJenisKonsumsi data = cekData.get();
            data.setDeletedAt(LocalDateTime.now());
            masterJenisKonsumsiRepo.save(data);
    
            return Response.responseSukses("Data jenis konsumsi berhasil dihapus", null);
        } catch (Exception e) {
            return Response.responseError(
                    Map.of("error", e.getMessage()),
                    500,
                    "Terjadi kesalahan pada server"
            );
        }
    }
    

    
    
    

}
