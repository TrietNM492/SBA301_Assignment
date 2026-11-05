package com.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff/books")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffBookingController {

}
