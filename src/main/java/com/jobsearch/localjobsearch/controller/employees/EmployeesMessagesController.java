package com.jobsearch.localjobsearch.controller.employees;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.entity.employees.Employees;
import com.jobsearch.localjobsearch.entity.employees.Message;
import com.jobsearch.localjobsearch.service.IEmail;
import com.jobsearch.localjobsearch.service.IEmployees;
import com.jobsearch.localjobsearch.service.IEmployeesMessage;
import com.jobsearch.localjobsearch.service.IToken;
import com.jobsearch.localjobsearch.service.IUsers;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee/messages")
public class EmployeesMessagesController {

    @Autowired
    private IEmployeesMessage employeesMessageService;

    @Autowired
    private IEmail emailService;

    @Autowired
    private IUsers userService;

    @Autowired
    private IEmployees employeesService;

    @PostMapping("/{employeeId}/assign-message")
    public ResponseEntity<String> assignMessageToEmployee(
            @PathVariable Long employeeId,
            @RequestBody Message message) throws MessagingException {
        // hacer una validación que solamente por persona puede enviar 1 mensaje por dia a ese empleado.
        // hacer un endpoind donde le usuario pueda borrar los mensajes
        employeesMessageService.assignMessageToEmployee(employeeId, message);
        Users user = userService.findById(employeeId);
        Employees employees = employeesService.findById(employeeId);
        employees.setUnreadMessages(employees.getUnreadMessages() + 1);
        employeesService.createEmployee(employees);
        emailService.sendMessageEmployees(user.getEmail(),message.getMessage());
        return ResponseEntity.ok("Mensaje enviado con exito!");
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<?> assignMessageToEmployee(
            @PathVariable Long messageId) {
       Message message = employeesMessageService.findById(messageId);
       Employees employees = message.getEmployee();
       if(!message.isRead()){
            message.setRead(true);
            employees.setUnreadMessages(employees.getUnreadMessages() - 1);
            employeesMessageService.save(message);
        }
       employeesService.createEmployee(employees);
       return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
