package com.jobsearch.localjobsearch.service.impl;

import com.jobsearch.localjobsearch.dao.EducationEmployeesDao;
import com.jobsearch.localjobsearch.entity.employees.Education;
import com.jobsearch.localjobsearch.service.IEducationEmployees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.lang.reflect.Field;

@Service
public class EducationsEmployeesImpl implements IEducationEmployees {

    @Autowired
    private EducationEmployeesDao educationEmployeesService;


    @Override
    public Education createEducation(Education education) {
        return educationEmployeesService.save(education);
    }

    @Override
    public Education findById(Long id) throws NotFoundException {
        return educationEmployeesService.findById(id).orElse(null);

    }



    @Override
    public void updateEducation(Education education) throws ChangeSetPersister.NotFoundException, IllegalAccessException {
        Education existingUser = educationEmployeesService.findById(education.getEducationID())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Field[] fields = Education.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(education);
            if (value != null) {
                field.set(existingUser, value);
            }
        }
        educationEmployeesService.save(existingUser);
    }

    @Override
    public void delete(Education education) {
        educationEmployeesService.delete(education);
    }


}
