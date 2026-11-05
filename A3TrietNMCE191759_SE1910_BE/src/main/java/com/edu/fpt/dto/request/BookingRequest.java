package com.edu.fpt.dto.request;

import com.edu.fpt.dto.response.BookingResponse;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.dto.response.RoomResponse;
import com.edu.fpt.entity.BookingDetailId;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    private LocalDate bookingDate = LocalDate.now();
    private CustomerResponse customerResponse;
    private BookingDetailRequest bookingDetailResponse;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookingDetailRequest {
        private Integer roomId;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
