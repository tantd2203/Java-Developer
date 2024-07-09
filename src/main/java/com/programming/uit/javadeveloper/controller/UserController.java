package com.programming.uit.javadeveloper.controller;

import com.programming.uit.javadeveloper.dto.UserRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public String addUser(  @Valid @RequestBody UserRequestDTO user) {
        return "User added";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable int userId, @Valid @RequestBody UserRequestDTO user) {
        System.out.println("Update user");
        return "User updated";
    }

    @PatchMapping("/{userId}")
    public String changeUserStatus(@PathVariable int userId, @RequestParam boolean status) {
        return "User Status changed";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@Min (1) @PathVariable int userId){
        return "User deleted";
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        return new UserRequestDTO("Tan", "Java", "admin@mail.com ");
    }
}
