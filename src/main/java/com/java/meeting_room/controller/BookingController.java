package com.java.meeting_room.controller;

import com.java.meeting_room.model.Response;
import com.java.meeting_room.model.request.BookingReq;
import com.java.meeting_room.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add-booking")
    public Response<Object> addBooking(@RequestBody BookingReq req) {
        return bookingService.addBooking(req);
    }

    @GetMapping("/list-summary")
    public Response<Object> listSummaryBooking(
            @RequestParam(required = false) String bookingDate,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        return bookingService.listSummaryBooking(bookingDate, offset, limit);
    }
}
