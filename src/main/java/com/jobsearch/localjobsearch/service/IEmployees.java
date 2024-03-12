package com.jobsearch.localjobsearch.service;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.entity.employees.Employees;

import java.util.List;

public interface IEmployees {
    Employees findById(Long id);

    Employees createEmployee(Employees employee);


    List<Users> findUsersByPublicProfile();
}
