package com.jobsearch.localjobsearch.service.impl;

import com.jobsearch.localjobsearch.dao.EmployeesDao;
import com.jobsearch.localjobsearch.dao.EmployeesMessagesDao;
import com.jobsearch.localjobsearch.entity.employees.Employees;
import com.jobsearch.localjobsearch.entity.employees.Message;
import com.jobsearch.localjobsearch.service.IEducationEmployees;
import com.jobsearch.localjobsearch.service.IEmployees;
import com.jobsearch.localjobsearch.service.IEmployeesMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeesMessageImpl implements IEmployeesMessage {

    @Autowired
    private IEmployees employeesService;

    @Autowired
    private EmployeesMessagesDao employeesMessageService;

    @Override
    public void assignMessageToEmployee(Long employeeId, Message message) {
        Employees employee = employeesService.findById(employeeId);
        if (employee != null) {
            message.setEmployee(employee);
            employee.getMessages().add(message);
            employeesService.createEmployee(employee);
        } else {
            throw new EntityNotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Message findById(Long messageId) {
        return employeesMessageService.findById(messageId).orElse(null);
    }

    @Override
    public void save(Message message) {
        employeesMessageService.save(message);
    }
}
