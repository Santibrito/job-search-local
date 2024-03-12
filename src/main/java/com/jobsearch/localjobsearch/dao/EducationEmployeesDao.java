package com.jobsearch.localjobsearch.dao;

import com.jobsearch.localjobsearch.entity.employees.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface EducationEmployeesDao extends JpaRepository<Education, Long>, CrudRepository<Education,Long> {

}
