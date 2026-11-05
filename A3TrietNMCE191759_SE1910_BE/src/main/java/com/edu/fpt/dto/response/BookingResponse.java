package com.edu.fpt.dto.response;

import com.edu.fpt.entity.BookingDetailId;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Integer id;
    private LocalDate bookingDate;
    private BigDecimal totalPrice;
    private CustomerResponse customerResponse;
    private Short bookingStatus;
    private BookingDetailResponse bookingDetailResponse;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookingDetailResponse {
        private BookingDetailId id;
        private RoomResponse roomResponse;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal actualPrice;
    }


}
