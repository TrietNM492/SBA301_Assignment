package com.edu.fpt.config;

import com.edu.fpt.entity.Customer;
import com.edu.fpt.entity.Staff;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if (staffRepository.count() == 0) {
            Staff cascasStaff = new Staff();
            cascasStaff.setEmailAddress("cascas@gmail.com");
            cascasStaff.setPassword(passwordEncoder.encode("123123"));
            cascasStaff.setRole((short) 1);
            cascasStaff.setStaffStatus((short) 1);
            cascasStaff.setStaffFullName("CasCake");
            cascasStaff.setHireDate(LocalDate.now());
            cascasStaff.setTelephone("0123456789");
            staffRepository.save(cascasStaff);

        }
        if (customerRepository.count() == 0) {
            Customer nahalux = new Customer();
            nahalux.setCustomerFullName("Nahalux");
            nahalux.setEmailAddress("nihihi@gmail.com");
            nahalux.setPassword(passwordEncoder.encode("123123"));
            nahalux.setTelephone("0987654321");
            nahalux.setCustomerStatus((short) 1);
            nahalux.setCustomerBirthday(LocalDate.of(2026, 11, 11));
            customerRepository.save(nahalux);
        }

    }
}
