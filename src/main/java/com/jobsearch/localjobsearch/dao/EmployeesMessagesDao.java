package com.jobsearch.localjobsearch.dao;


import com.jobsearch.localjobsearch.entity.employees.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesMessagesDao extends JpaRepository<Message,Long> {
}
