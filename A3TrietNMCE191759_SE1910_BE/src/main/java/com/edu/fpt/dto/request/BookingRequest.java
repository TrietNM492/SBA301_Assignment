package com.edu.fpt.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    private LocalDate bookingDate = LocalDate.now();
    @NotEmpty(message = "At least one room is required")
    @Valid
    private List<BookingDetailRequest> bookingDetails = new ArrayList<>();


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookingDetailRequest {
        @NotNull(message = "Room ID is required")
        private Integer roomId;
        @NotNull(message = "Start date is required")
        private LocalDate startDate;
        @NotNull(message = "End date is required")
        private LocalDate endDate;
    }
}
