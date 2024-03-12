package com.jobsearch.localjobsearch.entity.employees;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceID;
    private String academicTitle;
    private String studyCenter;
    private String location;
    private String startDate;
    private String finishDate;
    private Boolean currentlyWork;

    @ManyToOne
    @JsonIgnore
    private Employees employee;
}
