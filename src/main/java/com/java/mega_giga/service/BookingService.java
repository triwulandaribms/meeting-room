package com.java.mega_giga.service;

import java.time.LocalDateTime;
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
import com.java.mega_giga.model.response.BookingResponseDto;
import com.java.mega_giga.repository.BookingConsumptionRepository;
import com.java.mega_giga.repository.BookingRepository;
import com.java.mega_giga.repository.MasterJenisKonsumsiRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.*;

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

            System.out.println("CEK STARTHOUR: " + startHour);
            System.out.println("CEK ENDTIME: " + endHour);
            System.out.println("CEK TIME VALID: " + isValidTime);

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

            System.out.println("CEK KONSUMSI: " + konsumsiSet);

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

    public ResponseEntity<?> listSummaryBooking(String bookingDate, int limit, int page) {
        try {
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Booking> bookings;
            long totalData;
    
            ZoneId zoneJakarta = ZoneId.of("Asia/Jakarta");
    
            if (bookingDate != null && !bookingDate.isEmpty()) {
                YearMonth ym = YearMonth.parse(bookingDate);
                LocalDateTime startDate = ym.atDay(1).atStartOfDay();
                LocalDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59);
    
                bookings = bookingRepository.findAllWithConsumptionsBetween(startDate, endDate, pageable);
                totalData = bookingRepository.countAllByBookingDateBetween(startDate, endDate);
            } else {
                bookings = bookingRepository.findAll(pageable).getContent();
                totalData = bookingRepository.count();
            }
    
            Map<String, Integer> harga = Map.of(
                "Snack Siang", 20000,
                "Makan Siang", 30000,
                "Snack Sore", 20000
            );
    
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
                    .computeIfAbsent(room, r -> new ArrayList<>());
    
                List<Map<String, Object>> dataList = result.get(yearMonth).get(office).get(room);
    
                if (dataList.isEmpty()) {
                    Map<String, Object> defaultData = new LinkedHashMap<>();
                    defaultData.put("presentasi pemakaian", "0%");
                    defaultData.put("nominal konsumsi", 0);
    
                    Map<String, Integer> breakdown = new LinkedHashMap<>();
                    breakdown.put("snack siang", 0);
                    breakdown.put("makan siang", 0);
                    breakdown.put("snack sore", 0);
    
                    defaultData.put("breakdown", breakdown);
                    dataList.add(defaultData);
                }
    
                Map<String, Object> current = dataList.get(0);
                Map<String, Integer> breakdown = (Map<String, Integer>) current.get("breakdown");
                int nominalKonsumsi = (int) current.get("nominal konsumsi");
    
                for (BookingConsumption konsumsi : booking.getBookingConsumptions()) {
                    String jenis = konsumsi.getJenisKonsumsi().getName();
                    int jumlahPeserta = booking.getParticipants();
    
                    switch (jenis) {
                        case "Snack Siang" -> {
                            breakdown.compute("snack siang", (k, v) -> v + jumlahPeserta);
                            nominalKonsumsi += jumlahPeserta * harga.get("Snack Siang");
                        }
                        case "Makan Siang" -> {
                            breakdown.compute("makan siang", (k, v) -> v + jumlahPeserta);
                            nominalKonsumsi += jumlahPeserta * harga.get("Makan Siang");
                        }
                        case "Snack Sore" -> {
                            breakdown.compute("snack sore", (k, v) -> v + jumlahPeserta);
                            nominalKonsumsi += jumlahPeserta * harga.get("Snack Sore");
                        }
                    }
                }
    
                int totalPeserta = breakdown.values().stream().mapToInt(Integer::intValue).sum();
                int kapasitas = 100; 
                String presentasePemakaian = kapasitas > 0
                    ? String.format("%.1f%%", (totalPeserta * 100.0 / kapasitas))
                    : "0%";
    
                current.put("presentasi pemakaian", presentasePemakaian);
                current.put("nominal konsumsi", nominalKonsumsi);
            }
    
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("currentPage", page);
            response.put("perPage", limit);
            response.put("totalData", totalData);
            response.put("totalPage", (int) Math.ceil((double) totalData / limit));
            response.put("data", result);
    
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Terjadi kesalahan server.",
                "error", e.getMessage()
            ));
        }
    }
    


}
