package com.java.meeting_room.service;

import com.java.meeting_room.entity.Booking;
import com.java.meeting_room.entity.BookingConsumption;
import com.java.meeting_room.entity.MasterJenisKonsumsi;
import com.java.meeting_room.entity.SysUser;
import com.java.meeting_room.model.Authentication;
import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.BookingReq;
import com.java.meeting_room.model.request.MasterJenisKonsumsiReq;
import com.java.meeting_room.model.response.BookingResponseDto;
import com.java.meeting_room.model.response.SummaryResponseDto;
import com.java.meeting_room.repository.BookingConsumptionRepository;
import com.java.meeting_room.repository.BookingRepository;
import com.java.meeting_room.repository.MasterJenisKonsumsiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService extends AbstractService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MasterJenisKonsumsiRepository konsumsiRepository;

    @Autowired
    private BookingConsumptionRepository bookingConsumptionRepository;

    public Response<Object> addBooking(final Authentication authentication, final BookingReq req) {
        return precondition(authentication, SysUser.Role.ADMIN, SysUser.Role.USER).orElseGet(() -> {

        if (req.bookingDate() == null || req.officeName() == null ||
                req.startTime() == null || req.endTime() == null ||
                req.participants() == null || req.roomName() == null) {
            return Response.badRequest();
        }

        try {
            ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
            OffsetDateTime start = req.startTime().withOffsetSameInstant(ZoneOffset.ofHours(7));
            OffsetDateTime end = req.endTime().withOffsetSameInstant(ZoneOffset.ofHours(7));
            OffsetDateTime bookingDate = req.bookingDate().withOffsetSameInstant(ZoneOffset.ofHours(7));

            Set<String> konsumsiSet = new HashSet<>();
            int startHour = start.getHour();
            int endHour = end.getHour();

            if (startHour >= 6 && endHour <= 20) {
                if (startHour < 12)
                    konsumsiSet.add("Snack Siang");
                if (endHour >= 13)
                    konsumsiSet.add("Makan Siang");
                if (endHour >= 16)
                    konsumsiSet.add("Snack Sore");
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

            List<MasterJenisKonsumsi> konsumsiList = konsumsiSet.isEmpty()
                    ? new ArrayList<>()
                    : konsumsiRepository.findByNameIn(konsumsiSet);

            for (MasterJenisKonsumsi konsumsi : konsumsiList) {
                BookingConsumption bc = new BookingConsumption();
                bc.setBooking(saved);
                bc.setJenisKonsumsi(konsumsi);
                bookingConsumptionRepository.save(bc);
            }

            List<BookingResponseDto.ConsumptionDto> konsumsiDto = konsumsiList.stream()
                    .map(k -> new BookingResponseDto.ConsumptionDto(k.getName()))
                    .toList();

            BookingResponseDto dto = new BookingResponseDto(
                    saved.getBookingDate(),
                    saved.getOfficeName(),
                    saved.getStartTime(),
                    saved.getEndTime(),
                    konsumsiDto,
                    saved.getRoomName(),
                    saved.getParticipants());

            return Response.create("07", "00", "Sukses menambahkan booking", dto);
        } catch (Exception e) {
            return Response.create("07", "99", "Terjadi kesalahan server: " + e.getMessage(), null);
        }
    });
    }

    public Response<Object> listSummaryBooking(final Authentication authentication, final String bookingDate, Integer offset, Integer limit){

        return precondition(authentication, SysUser.Role.ADMIN).orElseGet(() -> {

        try {
            if (bookingDate == null || bookingDate.isEmpty()) {
                return Response.badRequest();
            }

            if (offset == null || offset < 0)
                offset = 0;
            if (limit == null || limit < 1)
                limit = 100;

            ZoneId zoneJakarta = ZoneId.of("Asia/Jakarta");
            YearMonth ym = YearMonth.parse(bookingDate, DateTimeFormatter.ofPattern("yyyy-MM"));
            ZonedDateTime startDate = ym.atDay(1).atStartOfDay(zoneJakarta);
            ZonedDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59).atZone(zoneJakarta);

            var pageable = PageRequest.of(offset, limit);
            var bookingPage = bookingRepository.findAllWithConsumptionsBetween(
                    startDate.toOffsetDateTime(), endDate.toOffsetDateTime(), pageable);
            var bookings = bookingPage.getContent();

            if (bookings.isEmpty()) {
                return Response.create("07", "04", "Data tidak ditemukan", null);
            }

            Map<String, Integer> jenisHarga = konsumsiRepository.findAllNameAndPrice()
                    .stream()
                    .collect(Collectors.toMap(
                            MasterJenisKonsumsiReq::name,
                            MasterJenisKonsumsiReq::maxPrice));

            Map<String, Map<String, List<Booking>>> grouped = bookings.stream()
                    .collect(Collectors.groupingBy(
                            Booking::getOfficeName,
                            LinkedHashMap::new,
                            Collectors.groupingBy(Booking::getRoomName)));

            Map<String, Object> result = new LinkedHashMap<>();

            grouped.forEach((office, roomMap) -> {
                Map<String, Object> roomSummary = new LinkedHashMap<>();

                roomMap.forEach((room, roomBookings) -> {
                    int totalParticipants = roomBookings.stream()
                            .mapToInt(Booking::getParticipants)
                            .sum();

                    Map<String, Long> konsumsiCount = roomBookings.stream()
                            .flatMap(b -> b.getBookingConsumptions().stream())
                            .collect(Collectors.groupingBy(
                                    bc -> bc.getJenisKonsumsi().getName(),
                                    Collectors.counting()));

                    double totalNominal = konsumsiCount.entrySet().stream()
                            .mapToDouble(e -> e.getValue() * jenisHarga.getOrDefault(e.getKey(), 0))
                            .sum();

                    Map<String, Object> konsumsiDetail = konsumsiCount.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> Map.of(
                                            "jumlah", e.getValue(),
                                            "hargaSatuan", jenisHarga.getOrDefault(e.getKey(), 0),
                                            "total", e.getValue() * jenisHarga.getOrDefault(e.getKey(), 0))));

                    double avgParticipants = (double) totalParticipants / roomBookings.size();
                    double presentasi = Math.min((avgParticipants / 100) * 100, 100.0);

                    roomSummary.put(room, Map.of(
                            "presentasiPemakaian", String.format("%.2f%%", presentasi),
                            "totalNominal", totalNominal,
                            "detailKonsumsi", konsumsiDetail));
                });

                result.put(office, roomSummary);
            });

            SummaryResponseDto dto = new SummaryResponseDto(
                    bookingPage.getTotalElements(),
                    limit,
                    bookingPage.getTotalPages(),
                    bookingPage.getNumber() + 1,
                    result);

            return Response.create("07", "00", "Sukses list summary booking", dto);
        } catch (Exception e) {
            return Response.create("07", "99", "Terjadi kesalahan server: " + e.getMessage(), null);
        }
    });
    }

}
