package com.jobsearch.localjobsearch.entity.employees;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
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
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationID;
    private String academicTitle;
    private String studyCenter;
    private String location;
    private String startDate;
    private String finishDate;
    private Boolean currentlyStudying;

    @ManyToOne
    @JsonIgnore
    private Employees employee;

}
