package com.edu.fpt.controller;

import com.edu.fpt.dto.request.RoomRequest;
import com.edu.fpt.dto.response.RoomResponse;
import com.edu.fpt.service.RoomService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/rooms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffRoomController {

    private final RoomService roomService;


    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RoomResponse>>> getAllRooms(
            @RequestParam(required = false) String roomNumber, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Rooms retrieved successfully",
                roomService.getAllRooms(roomNumber, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Room retrieved successfully",
                roomService.getRoomById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponse>> createRoom(@Valid @RequestBody RoomRequest request) {
        RoomResponse createdRoom = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Room created successfully", createdRoom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> updateRoom(
            @PathVariable Integer id,
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Room updated successfully",
                roomService.updateRoom(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Room deleted successfully", null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> changeRoomStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Room status updated successfully",
                roomService.changeRoomStatus(id)));
    }
}
