package com.programming.uit.javadeveloper.service.impl;

import com.programming.uit.javadeveloper.dto.AddressDTO;
import com.programming.uit.javadeveloper.dto.UserRequestDTO;
import com.programming.uit.javadeveloper.dto.respone.PageResponse;
import com.programming.uit.javadeveloper.dto.respone.UserDetailResponse;
import com.programming.uit.javadeveloper.exception.ResourceNotFoundException;
import com.programming.uit.javadeveloper.model.Address;
import com.programming.uit.javadeveloper.model.User;
import com.programming.uit.javadeveloper.repository.UserRepository;
import com.programming.uit.javadeveloper.service.UserService;
import com.programming.uit.javadeveloper.util.UserStatus;
import com.programming.uit.javadeveloper.util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public long saveUser(UserRequestDTO request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(request.getStatus())
                .type(UserType.valueOf(request.getType().toUpperCase()))
                .addresses(convertToAddress(request.getAddresses()))
                .build();
        request.getAddresses().forEach(a ->
                user.saveAddress(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build()));
        userRepository.save(user);
        log.info("User has added successfully, userId={}", user.getId());
        return user.getId();
    }


    @Override
    public void updateUser(long userId, UserRequestDTO request) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        if (!request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());
        user.setType(UserType.valueOf(request.getType().toUpperCase()));
        user.setAddresses(convertToAddress(request.getAddresses()));
        userRepository.save(user);

        log.info("User has updated successfully, userId={}", userId);

    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);

        log.info("User status has changed successfully, userId={}", userId);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("User has deleted permanent successfully, userId={}", userId);
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        return UserDetailResponse.builder()
                .id(userId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .status(user.getStatus())
                .type(user.getType().name())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUsers_(int pageNo, int pageSize) {
        int p = 0;
        if (pageNo > 0) {
            p = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(p, pageSize);
        Page<User> users = userRepository.findAll(pageable);


        return users.stream().map(user ->
                        UserDetailResponse.builder()
                                .id((user.getId()))
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .dateOfBirth(user.getDateOfBirth())
                                .gender(user.getGender())
                                .phone(user.getPhone())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .status(user.getStatus())
                                .type(user.getType().name())
                                .build()).
                toList();
    }

    @Override
    public List<UserDetailResponse> getAllUsersSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if (pageNo > 0) {
            p = pageNo - 1;
        }
        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));


        Page<User> users = userRepository.findAll(pageable);

        return users.stream().map(user ->
                        UserDetailResponse.builder()
                                .id((user.getId()))
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .dateOfBirth(user.getDateOfBirth())
                                .gender(user.getGender())
                                .phone(user.getPhone())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .status(user.getStatus())
                                .type(user.getType().name())
                                .build()).
                toList();
    }

    @Override
    public List<UserDetailResponse> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int p = 0;
        if (pageNo > 0) {
            p = pageNo - 1;
        }
        List<Sort.Order> orders = new ArrayList<>();

        for (String sortBy : sorts) {
            if (StringUtils.hasLength(sortBy)) {

                // firstName:asc|desc
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }


        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));


        Page<User> users = userRepository.findAll(pageable);

        return users.stream().map(user ->
                        UserDetailResponse.builder()
                                .id((user.getId()))
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .dateOfBirth(user.getDateOfBirth())
                                .gender(user.getGender())
                                .phone(user.getPhone())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .status(user.getStatus())
                                .type(user.getType().name())
                                .build()).
                toList();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        return convertToPageResponse(users, pageable);

    }

    private Set<Address> convertToAddress(Set<AddressDTO> addresses) {
        Set<Address> result = new HashSet<>();

        addresses.forEach(a ->
                result.add(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build()));

        return result;
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }
    private PageResponse<?> convertToPageResponse(Page<User> users, Pageable pageable) {
        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .items(response)
                .build();
    }
}
