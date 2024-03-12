package com.jobsearch.localjobsearch.controller.employees;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.entity.employees.Education;
import com.jobsearch.localjobsearch.entity.employees.Employees;
import com.jobsearch.localjobsearch.service.IEducationEmployees;
import com.jobsearch.localjobsearch.service.IEmployees;
import com.jobsearch.localjobsearch.service.IToken;
import com.jobsearch.localjobsearch.service.IUsers;
import com.jobsearch.localjobsearch.utils.LoggerHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeesController {
    @Autowired
    private IEmployees employeeService;

    @Autowired
    private IUsers userService;

    @Autowired
    private IEducationEmployees educationEmployeesService;

    private static final LoggerHelper logger = new LoggerHelper();

    @Autowired
    private IToken tokenService;

    @PostMapping("create")
    public ResponseEntity<Employees> createEmployee(@RequestBody Employees employee, HttpServletRequest request) {
        Users currentUser = getCurrentUserFromToken(request);
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        employee.setUser(currentUser);
        try {
            Employees savedEmployee = employeeService.createEmployee(employee);
            userService.save(currentUser);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private Users getCurrentUserFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        } else {
            return null;
        }

        String userEmail = tokenService.getEmailFromToken(token);
        if (userEmail == null) {
            return null;
        }

        return userService.findByEmail(userEmail);
    }

    @GetMapping("/public-profile")
    public ResponseEntity<List<Users>> getEmployeesWithPublicProfile() {
        List<Users> employeesWithPublicProfile = employeeService.findUsersByPublicProfile();
        if (employeesWithPublicProfile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employeesWithPublicProfile);
    }


    @GetMapping("/info")
    public ResponseEntity<?> getUserById(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        } else {
            String errorMessage = "El token de autorización no se proporcionó correctamente.";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userEmail = tokenService.getEmailFromToken(token);
        if (userEmail == null) {
            String errorMessage = "No se pudo extraer el correo electrónico del token.";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Users currentUser = userService.findByEmail(userEmail);
        return ResponseEntity.ok(currentUser);
    }

}
