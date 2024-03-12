package com.jobsearch.localjobsearch.service;

import com.jobsearch.localjobsearch.entity.employees.Message;

public interface IEmployeesMessage {
    <S extends Message> void assignMessageToEmployee(Long employeeId, S message);

    Message findById(Long messageId);

    void save(Message message);
}
