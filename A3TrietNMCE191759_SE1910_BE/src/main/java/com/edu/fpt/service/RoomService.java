package com.edu.fpt.service;

import com.edu.fpt.dto.request.RoomRequest;
import com.edu.fpt.dto.response.RoomResponse;
import com.edu.fpt.entity.RoomInformation;
import com.edu.fpt.entity.RoomType;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.BookingDetailRepository;
import com.edu.fpt.repository.RoomInformationRepository;
import com.edu.fpt.repository.RoomTypeRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomInformationRepository roomInformationRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final BookingDetailRepository bookingDetailRepository;

    public PageResponse<RoomResponse> getAllRooms(String roomNumber, Pageable pageable) {
        Page<RoomInformation> roomInformations = roomInformationRepository.findByRoomNumberContainingIgnoreCase(roomNumber, pageable);
        Page<RoomResponse> res = roomInformations.map(this::toRoomResponse);
        return new PageResponse<>(res);
    }

    public RoomResponse toRoomResponse(RoomInformation roomInformation) {
        RoomResponse response = RoomResponse.builder()
                .roomDetailDescription(roomInformation.getRoomDetailDescription())
                .roomStatus(roomInformation.getRoomStatus())
                .roomMaxCapacity(roomInformation.getRoomMaxCapacity())
                .roomTypeId(roomInformation.getRoomTypeID().getId())
                .roomTypeName(roomInformation.getRoomTypeID().getRoomTypeName())
                .roomId(roomInformation.getId())
                .roomNumber(roomInformation.getRoomNumber())
                .roomPricePerDay(roomInformation.getRoomPricePerDay())
                .build();
              return response;
    }

    public RoomResponse getRoomById(Integer id) {
        RoomInformation room = roomInformationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return toRoomResponse(room);
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Room Type Not Found!"));
        RoomInformation roomInformation = new RoomInformation();
        roomInformation.setRoomNumber(request.getRoomNumber());
        roomInformation.setRoomDetailDescription(roomInformation.getRoomDetailDescription());
        roomInformation.setRoomMaxCapacity(request.getRoomMaxCapacity());
        roomInformation.setRoomTypeID(roomType);
        roomInformation.setRoomStatus(request.getRoomStatus() != null ? request.getRoomStatus() : 1);
        roomInformation.setRoomPricePerDay(request.getRoomPricePerDay());
        return toRoomResponse(roomInformationRepository.save(roomInformation));
    }

    @Transactional
    public RoomResponse updateRoom(Integer id, RoomRequest request) {
        RoomInformation room = roomInformationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room Type not found"));

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomDetailDescription(request.getRoomDetailDescription());
        room.setRoomMaxCapacity(request.getRoomMaxCapacity());
        room.setRoomTypeID(roomType);
        room.setRoomStatus(request.getRoomStatus());
        room.setRoomPricePerDay(request.getRoomPricePerDay());

        return toRoomResponse(roomInformationRepository.save(room));
    }

    @Transactional
    public void deleteRoom(Integer id) {
        RoomInformation room = roomInformationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        boolean isRoomBooked = bookingDetailRepository.existsByRoomIDBookingNow(id, (short) 0, LocalDate.now());

        if (isRoomBooked) {
            room.setRoomStatus((short) 0);
            roomInformationRepository.save(room);

        } else {

            roomInformationRepository.delete(room);
        }
    }

    @Transactional
    public RoomResponse changeRoomStatus(Integer id) {
        RoomInformation room = roomInformationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setRoomStatus((short) (room.getRoomStatus() == 1 ? 0 : 1));
        return toRoomResponse(roomInformationRepository.save(room));
    }


}
