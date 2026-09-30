package com.ecom.app.user.service;

import com.ecom.app.user.dto.AddressDTO;
import com.ecom.app.user.dto.UserRequest;
import com.ecom.app.user.dto.UserResponse;
import com.ecom.app.user.entity.Address;
import com.ecom.app.user.entity.User;
import com.ecom.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;



    public List<UserResponse> fetchAllUser()
    {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }


    public void adduser(UserRequest userRequest)
    {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);

    }

    public Optional<UserResponse> fetchUser(Long id)
    {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updateUserRequest)
    {
        return userRepository.findById(id)
                .map(
                        existingUser -> {
                            updateUserFromRequest(existingUser, updateUserRequest);
                            userRepository.save(existingUser);
                            return true;
                        }
                ).orElse(false);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest)
    {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if(userRequest.getAddress() != null)
        {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user)
    {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if(user.getAddress() != null)
        {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            addressDTO.setCountry(user.getAddress().getCountry());
            response.setAddress(addressDTO);
        }

        return response;
    }

}
