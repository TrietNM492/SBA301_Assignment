package com.edu.fpt.dto.projection;

import java.time.LocalDate;

public interface CustomerProjection {

    Integer getId();
    String getCustomerFullName();
    String getTelephone();
    String getEmailAddress();
    LocalDate getCustomerBirthday();
    Short getCustomerStatus();
}
