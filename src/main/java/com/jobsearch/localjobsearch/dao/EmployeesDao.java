package com.jobsearch.localjobsearch.dao;

import com.jobsearch.localjobsearch.entity.employees.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeesDao extends JpaRepository<Employees, Long>, CrudRepository<Employees, Long> {
    Employees findByEmployeeID(Long id);

    List<Employees> findByPublicProfileTrue();
}
