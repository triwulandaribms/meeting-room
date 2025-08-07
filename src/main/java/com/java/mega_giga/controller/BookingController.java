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
    public ResponseEntity<?> listSummaryBooking(@RequestParam String bookingDate) {
        return bookingService.listSummaryBooking(bookingDate);
    }
    
}
