package com.jobsearch.localjobsearch.service;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.entity.employees.Education;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Optional;

public interface IEducationEmployees {
    Education createEducation(Education education);

    Education findById(Long id);

    void updateEducation(Education education) throws ChangeSetPersister.NotFoundException, IllegalAccessException;

    void delete(Education education);
}
