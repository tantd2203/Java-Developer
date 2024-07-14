package com.programming.uit.javadeveloper.repository;


import com.programming.uit.javadeveloper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
