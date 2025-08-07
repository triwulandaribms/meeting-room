package com.java.mega_giga.service;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.java.mega_giga.entity.Booking;
import com.java.mega_giga.entity.BookingConsumption;
import com.java.mega_giga.entity.MasterJenisKonsumsi;
import com.java.mega_giga.model.request.BookingReq;
import com.java.mega_giga.model.request.MasterJenisKonsumsiReq;
import com.java.mega_giga.model.response.BookingResponseDto;
import com.java.mega_giga.repository.BookingConsumptionRepository;
import com.java.mega_giga.repository.BookingRepository;
import com.java.mega_giga.repository.MasterJenisKonsumsiRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MasterJenisKonsumsiRepository konsumsiRepository;

    @Autowired
    private BookingConsumptionRepository bookingConsumptionRepository;

    public ResponseEntity<?> addBooking(BookingReq req) {

        if (req.bookingDate() == null || req.officeName() == null || req.startTime() == null
                || req.endTime() == null || req.participants() == null || req.roomName() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Semua field wajib diisi."));
        }

        try {
            ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");

            OffsetDateTime start = req.startTime().withOffsetSameInstant(ZoneOffset.ofHours(7));
            OffsetDateTime end = req.endTime().withOffsetSameInstant(ZoneOffset.ofHours(7));
            OffsetDateTime bookingDate = req.bookingDate().withOffsetSameInstant(ZoneOffset.ofHours(7));

            ZonedDateTime startJakarta = start.atZoneSameInstant(jakartaZone);
            ZonedDateTime endJakarta = end.atZoneSameInstant(jakartaZone);

            int startHour = startJakarta.getHour();
            int endHour = endJakarta.getHour();

            Set<String> konsumsiSet = new HashSet<>();
            boolean isValidTime = startHour >= 6 && startHour <= 20 && endHour >= 6 && endHour <= 20;

            // System.out.println("CEK STARTHOUR: " + startHour);
            // System.out.println("CEK ENDTIME: " + endHour);
            // System.out.println("CEK TIME VALID: " + isValidTime);

            if (isValidTime) {
                long daysBetween = ChronoUnit.DAYS.between(startJakarta.toLocalDate(), endJakarta.toLocalDate()) + 1;

                if (daysBetween > 1) {
                    konsumsiSet.add("Snack Siang");
                    konsumsiSet.add("Makan Siang");
                    konsumsiSet.add("Snack Sore");
                } else {
                    if (startHour < 12 && endHour >= 7)
                        konsumsiSet.add("Snack Siang");
                    if (startHour < 15 && endHour >= 13)
                        konsumsiSet.add("Makan Siang");
                    if (startHour <= 19 && endHour >= 16)
                        konsumsiSet.add("Snack Sore");
                }
            }

            Booking booking = new Booking();
            booking.setBookingDate(bookingDate);
            booking.setOfficeName(req.officeName());
            booking.setRoomName(req.roomName());
            booking.setParticipants(req.participants());
            booking.setStartTime(start);
            booking.setEndTime(end);
            booking.setCreateAt(OffsetDateTime.now(jakartaZone));

            Booking saved = bookingRepository.save(booking);

            // System.out.println("CEK KONSUMSI: " + konsumsiSet);

            List<MasterJenisKonsumsi> konsumsiList = konsumsiSet.isEmpty()
                    ? new ArrayList<>()
                    : konsumsiRepository.findByNameIn(konsumsiSet);

            for (MasterJenisKonsumsi konsumsi : konsumsiList) {
                BookingConsumption bc = new BookingConsumption();
                bc.setBooking(saved);
                bc.setJenisKonsumsi(konsumsi);
                bookingConsumptionRepository.save(bc);
            }

            BookingResponseDto dto = new BookingResponseDto();
            dto.setBookingDate(saved.getBookingDate());
            dto.setStartTime(saved.getStartTime());
            dto.setEndTime(saved.getEndTime());
            dto.setOfficeName(saved.getOfficeName());
            dto.setRoomName(saved.getRoomName());
            dto.setParticipants(saved.getParticipants());

            List<BookingResponseDto.ConsumptionDto> konsumsiDtos = konsumsiList.stream()
                    .map(k -> new BookingResponseDto.ConsumptionDto(k.getName()))
                    .toList();

            dto.setListConsumption(konsumsiDtos);

            return ResponseEntity.status(201).body(dto);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Terjadi kesalahan server: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> listSummaryBooking(String bookingDate) {

        try {

            List<Booking> bookings = new ArrayList<>();
            ZoneId zoneJakarta = ZoneId.of("Asia/Jakarta");

            if (bookingDate != null && !bookingDate.isEmpty()) {
                YearMonth ym = YearMonth.parse(bookingDate);
                ZonedDateTime startDate = ym.atDay(1).atStartOfDay(zoneJakarta);
                ZonedDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59).atZone(zoneJakarta);

                bookings = bookingRepository.findAllWithConsumptionsBetween(
                        startDate.toOffsetDateTime(),
                        endDate.toOffsetDateTime());
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Parameter bookingDate diperlukan."));
            }

            List<MasterJenisKonsumsiReq> jenisList = konsumsiRepository.findAllNameAndPrice();

            Map<String, Integer> jenisHarga = jenisList.stream()
                    .collect(Collectors.toMap(
                            MasterJenisKonsumsiReq::name,
                            MasterJenisKonsumsiReq::maxPrice));

            Map<String, Map<String, Map<String, List<Map<String, Object>>>>> result = new LinkedHashMap<>();

            for (Booking booking : bookings) {
                
                String office = booking.getOfficeName();
                String room = booking.getRoomName();
                String yearMonth = booking.getBookingDate()
                        .atZoneSameInstant(zoneJakarta)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM"));

                result
                        .computeIfAbsent(yearMonth, ym -> new LinkedHashMap<>())
                        .computeIfAbsent(office, o -> new LinkedHashMap<>())
                        .computeIfAbsent(room, r -> {
                            Map<String, Object> defaultData = new LinkedHashMap<>();
                            defaultData.put("presentasi pemakaian", "0%");
                            defaultData.put("nominal konsumsi", 0);

                            Map<String, Integer> detailKonsumsi = new LinkedHashMap<>();
                            detailKonsumsi.put("snack siang", 0);
                            detailKonsumsi.put("makan siang", 0);
                            detailKonsumsi.put("snack sore", 0);
                            defaultData.put("detail konsumsi", detailKonsumsi);

                            return new ArrayList<>(List.of(defaultData));
                        });

                List<Map<String, Object>> dataList = result.get(yearMonth).get(office).get(room);
                Map<String, Object> current = dataList.get(0);

                @SuppressWarnings("unchecked")
                Map<String, Integer> detailKonsumsi = (Map<String, Integer>) current.get("detail konsumsi");

                int nominalKonsumsi = (int) current.get("nominal konsumsi");

                for (BookingConsumption konsumsi : booking.getBookingConsumptions()) {
                    String jenis = konsumsi.getJenisKonsumsi().getName();
                    int peserta = booking.getParticipants();
                
                    String key = jenis.toLowerCase(); 
                
                    if (detailKonsumsi.containsKey(key)) {
                        detailKonsumsi.put(key, detailKonsumsi.get(key) + peserta);
                    } else {
                        detailKonsumsi.put(key, peserta);
                    }
                
                    int harga = jenisHarga.getOrDefault(jenis, 0);
                    nominalKonsumsi += peserta * harga;
                }
                

                int totalPeserta = detailKonsumsi.values().stream().mapToInt(Integer::intValue).sum();
                int kapasitas = 100;
                String presentase = kapasitas > 0
                        ? String.format("%.1f%%", (totalPeserta * 100.0 / kapasitas))
                        : "0%";

                current.put("presentasi pemakaian", presentase);
                current.put("nominal konsumsi", nominalKonsumsi);
            }

            return ResponseEntity.ok(Map.of("data", result));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Terjadi kesalahan server.",
                    "error", e.getMessage()));
        }
    }

}
