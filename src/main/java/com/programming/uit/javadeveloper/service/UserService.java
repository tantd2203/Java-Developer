package com.programming.uit.javadeveloper.service;

import com.programming.uit.javadeveloper.dto.UserRequestDTO;
import com.programming.uit.javadeveloper.dto.respone.PageResponse;
import com.programming.uit.javadeveloper.dto.respone.UserDetailResponse;
import com.programming.uit.javadeveloper.util.UserStatus;

import java.util.List;

public interface UserService  {

    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);


    List<UserDetailResponse> getAllUsers_(int pageNo, int pageSize);
    List<UserDetailResponse> getAllUsersSortBy(int pageNo, int pageSize, String sortBy);
    List<UserDetailResponse> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);
    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUsersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sortBy);

}
