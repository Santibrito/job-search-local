package com.jobsearch.localjobsearch.service.impl;

import com.jobsearch.localjobsearch.dao.EmployeesDao;
import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.entity.employees.Employees;
import com.jobsearch.localjobsearch.service.IEmployees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeesImpl implements IEmployees {

    @Autowired
    private EmployeesDao employeeRepository;


    @Override
    public Employees createEmployee(Employees employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employees findById(Long id){
        return employeeRepository.findByEmployeeID(id);
    }
    @Override
    public List<Users> findUsersByPublicProfile() {
        List<Employees> employeesWithPublicProfile = employeeRepository.findByPublicProfileTrue();
        return employeesWithPublicProfile.stream()
                .map(Employees::getUser)
                .collect(Collectors.toList());
    }
}
