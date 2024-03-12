package com.jobsearch.localjobsearch.controller.employees;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.entity.employees.Education;
import com.jobsearch.localjobsearch.service.IEducationEmployees;
import com.jobsearch.localjobsearch.service.IToken;
import com.jobsearch.localjobsearch.service.IUsers;
import com.jobsearch.localjobsearch.utils.LoggerHelper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee/education")
public class EmployeesEducationsController {


    private static final LoggerHelper logger = new LoggerHelper();
    @Autowired
    private IEducationEmployees educationEmployeesService;

    @Autowired
    private IUsers userService;

    @Autowired
    private IToken tokenService;

    @PostMapping("create")
    public ResponseEntity<Education> createEducation(@RequestBody Education education, HttpServletRequest request) {
        Users currentUser = getCurrentUserFromToken(request);
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        education.setEmployee(currentUser.getEmployee());
        try {
            Education savedEducation = educationEmployeesService.createEducation(education);
            return new ResponseEntity<>(savedEducation, HttpStatus.CREATED);
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

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable Long id){
        try {
            Education education = educationEmployeesService.findById(id);
            educationEmployeesService.delete(education);
            return new ResponseEntity<>("Educación eliminada", HttpStatus.NO_CONTENT);
        }catch (DataAccessException exception){
            String errorMessage = "Error al eliminar la educación.";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage + exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    @PutMapping("update")
    public ResponseEntity<?> updateEducation(@RequestBody Education education) throws IllegalAccessException {
        try {
            educationEmployeesService.updateEducation(education);
            return new ResponseEntity<>("Educación actualizada", HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "Error al actualizar la educación";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
