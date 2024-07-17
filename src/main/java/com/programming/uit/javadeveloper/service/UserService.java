package com.programming.uit.javadeveloper.service;

import com.programming.uit.javadeveloper.dto.Address;
import com.programming.uit.javadeveloper.dto.UserRequestDTO;
import com.programming.uit.javadeveloper.dto.respone.PageResponse;
import com.programming.uit.javadeveloper.dto.respone.UserDetailResponse;
import com.programming.uit.javadeveloper.util.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService  {

    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse getAllUsers(int pageNo, int pageSize);

    List<UserDetailResponse> getAllUsers_(int pageNo, int pageSize);
}
