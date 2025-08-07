package com.java.mega_giga.controller;

import com.java.mega_giga.model.request.BookingReq;
import com.java.mega_giga.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add-booking")
    public ResponseEntity<?> addBooking(@RequestBody BookingReq req) {
        return bookingService.addBooking(req);
    }

    @GetMapping("/list-summary")
    public ResponseEntity<?> listSummaryBooking(
        @RequestParam(required = false) String bookingDate,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "1") int page
    ) {
        return bookingService.listSummaryBooking(bookingDate, limit, page);
    }
}
