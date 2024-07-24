package com.programming.uit.javadeveloper.controller;

import com.programming.uit.javadeveloper.dto.UserRequestDTO;
import com.programming.uit.javadeveloper.dto.respone.*;
import com.programming.uit.javadeveloper.service.UserService;
import com.programming.uit.javadeveloper.exception.ResourceNotFoundException;
import com.programming.uit.javadeveloper.util.UserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Request add user, {} {}", user.getFirstName(), user.getLastName());

        try {
            long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully,", userId);
        } catch (Exception e) {
            log.error("error message = {} ", e.getMessage(), e.getCause());

            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequestDTO user) {
        log.info("Request update userId={}", userId);

        try {
            userService.updateUser(userId, user);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update user success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }
    @PatchMapping("/{userId}")
    public ResponseData<?> updateStatus(@Min(1) @PathVariable long  userId, @RequestParam UserStatus status) {
        log.info("Request change status, userId={}", userId);
        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Change status success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status fail");
        }
    }
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        log.info("Request delete userId={}", userId);

        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete user success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }


    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable @Min(1) long userId) {
        log.info("Request get user detail, userId={}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUser(userId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseData<List<UserDetailResponse>> getAllUser(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                             @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        log.info("Request get all user");
        return new ResponseData(HttpStatus.OK.value(), "users", userService.getAllUsers_(pageNo,pageSize));
    }
        @GetMapping("/list_sort")
    public ResponseData<List<UserDetailResponse>> getAllUserSort(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                             @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                 @RequestParam( required = false) String sortBy) {
        log.info("Request get all user");
        return new ResponseData(HttpStatus.OK.value(), "users", userService.getAllUsersSortBy(pageNo,pageSize,sortBy));
    }
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ResponseData<List<UserDetailResponse>> getAllUsersWithSortByMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                @RequestParam(required = false) String... sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts));
    }

    @GetMapping("/list-with-sort-by-multiple-columns-page")
    public ResponseData<?> getAllUsersWithSortPage(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                @RequestParam(required = false) String sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortBy(pageNo, pageSize, sorts));
    }
}