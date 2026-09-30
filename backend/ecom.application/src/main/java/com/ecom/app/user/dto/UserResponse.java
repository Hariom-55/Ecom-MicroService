package com.ecom.app.user.dto;


import com.ecom.app.user.entity.UserRole;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
public class UserResponse
{
    private Long id;
    private String firstName;
    private String LastName;
    private String email;
    private String phone;
    private UserRole role = UserRole.CUSTOMER;
    private AddressDTO address;
}
