package com.ecom.app.user.service;

import com.ecom.app.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private List<User> userList = new ArrayList<>();
    private Long nextId = 1L;
    public List<User> fetchAllUser()
    {
        return userList;
    }


    public void adduser(User user)
    {
        user.setId(nextId++);
        userList.add(user);

    }

    public Optional<User> fetchUser(Long id)
    {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public boolean updateUser(Long id, User updateUser)
    {
        return userList.stream()
                .filter(
                        user -> user.getId().equals(id)
                )
                .findFirst()
                .map(
                        existingUser -> {
                            existingUser.setFirstName(updateUser.getFirstName());
                            existingUser.setLastName(updateUser.getLastName());

                            return true;
                        }
                ).orElse(false);
    }

}
