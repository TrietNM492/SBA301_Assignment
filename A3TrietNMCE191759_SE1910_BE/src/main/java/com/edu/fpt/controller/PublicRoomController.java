package com.edu.fpt.controller;

import com.edu.fpt.dto.response.RoomResponse;
import com.edu.fpt.service.RoomService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class PublicRoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RoomResponse>>> getRooms(
            @RequestParam(required = false) String roomNumber, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Rooms retrieved successfully",
                roomService.getAllRooms(roomNumber, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> getRoom(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Room retrieved successfully",
                roomService.getRoomById(id)));
    }
}
