package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingStatusRequest {
    @NotNull
    @Min(0)
    @Max(2)
    private Short bookingStatus;
}
