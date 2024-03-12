package com.jobsearch.localjobsearch.enums;

import lombok.Getter;


@Getter
public enum UserType {
    EMPLOYEE("EMPLOYEE"),
    CANDIDATE("CANDIDATE"),
    ADMINISTRATOR("ADMINISTRATOR");

    private final String value;

    UserType(String value) {
        this.value = value;
    }
}
