package com.jobsearch.localjobsearch.dao;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.enums.UserType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersDao extends JpaRepository<Users, Long> {

    List<Users> findByUserType(UserType userType);

    Users findByEmail(String email);

}
