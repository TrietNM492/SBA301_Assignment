package com.edu.fpt.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {

    @NotBlank(message = "Room number is required")
    String roomNumber;

    String roomDetailDescription;

    @Min(value = 1, message = "Capacity must be at least 1")
    Integer roomMaxCapacity;

    @NotNull(message = "Room Type ID is required")
    Integer roomTypeId;

    Short roomStatus;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    BigDecimal roomPricePerDay;
}
