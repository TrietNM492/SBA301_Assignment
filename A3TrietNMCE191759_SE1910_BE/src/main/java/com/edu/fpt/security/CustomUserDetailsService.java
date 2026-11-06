package com.edu.fpt.security;

import com.edu.fpt.entity.Customer;
import com.edu.fpt.entity.Staff;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.repository.StaffRepository;
import com.edu.fpt.security.model.UserSecurityRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] parts = username.split(":", 2);
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid Username format!");
        }

        String type = parts[0];
        String email = parts[1];
        if (type.equals("CUSTOMER")) {
            Customer customer = customerRepository.findByEmailAddressIgnoreCase(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Customer Not Found!"));
            if (!Short.valueOf((short) 1).equals(customer.getCustomerStatus())) {
                throw new UsernameNotFoundException("Customer account is inactive");
            }
            UserSecurityRes user = new UserSecurityRes(customer.getId(),
                    customer.getEmailAddress(),
                    customer.getPassword(),
                    customer.getCustomerFullName(),
                    type,
                    List.of(new SimpleGrantedAuthority("ROLE_" + type)));
            return new CustomUserDetails(user);
        } else if (type.equals("STAFF")) {
            Staff staff = staffRepository.findByEmailAddressIgnoreCase(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Staff not found: " + email));
            // Map Role (1=Admin, 2=Manager, 3=Receptionist) từ DB sang authority
            String role = switch (staff.getRole()) {
                case 1 -> "ROLE_ADMIN";
                case 2 -> "ROLE_MANAGER";
                case 3 -> "ROLE_RECEPTIONIST";
                default -> "ROLE_STAFF";
            };
            UserSecurityRes user = new UserSecurityRes(staff.getId(),
                    staff.getEmailAddress(),
                    staff.getPassword(),
                    staff.getStaffFullName(),
                    type,
                    List.of(
                            new SimpleGrantedAuthority("ROLE_STAFF"),
                            new SimpleGrantedAuthority(role)
                    ));
            return new CustomUserDetails(user);
        }
        throw new UsernameNotFoundException("Unknown user type: " + type);
    }

}
