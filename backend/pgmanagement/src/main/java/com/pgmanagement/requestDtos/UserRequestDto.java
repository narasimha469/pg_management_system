package com.pgmanagement.requestDtos;

import com.pgmanagement.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 20)
    @Pattern(
        regexp = "^[A-Za-z .'-]+$",
        message = "First name contains invalid characters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 20)
    @Pattern(
        regexp = "^[A-Za-z .'-]+$",
        message = "Last name contains invalid characters"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Phone number must be 10 digits and start with 6-9"
    )
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50)
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;

    @NotBlank(message = "Street is required")
    @Size(max = 100)
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    @Pattern(
        regexp = "^[A-Za-z .'-]+$",
        message = "City contains invalid characters"
    )
    private String city;

    @NotBlank(message = "Pincode is required")
    @Pattern(
        regexp = "^[1-9][0-9]{5}$",
        message = "Pincode must be 6 digits"
    )
    private String pincode;

    // Role is auto-assigned in backend for security; optional here
    private Role role; // ROLE_GUEST for guest, ROLE_OWNER for owner
}