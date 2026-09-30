package com.ecom.app.user.controller;

import com.ecom.app.user.dto.UserRequest;
import com.ecom.app.user.dto.UserResponse;
import com.ecom.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody UserRequest userRequest
            )
    {
        userService.adduser(userRequest);

        return ResponseEntity.ok(
                "User Added Successfully"
        );
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser()
    {
        return new ResponseEntity<>(
                userService.fetchAllUser(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id
    )
    {

        return userService.fetchUser(id)
                .map(
                        ResponseEntity :: ok
                )
                .orElseGet(
                        () -> new ResponseEntity<>(
                                HttpStatus.NOT_FOUND
                        )
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest updatedUserRequest
    )
    {
        boolean updated = userService.updateUser(id, updatedUserRequest);

        if(updated){
            return ResponseEntity.ok(
                    "Details Updated Successfully"
            );
        }

        return new ResponseEntity<>(
                "User is unaccessible",
                HttpStatus.BAD_REQUEST
        );
    }
}
