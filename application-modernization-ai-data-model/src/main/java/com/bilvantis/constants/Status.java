package com.bilvantis.constants;

import lombok.Getter;

@Getter
public enum Status {

    SUCCESS("Success"),
    FAILURE("Failure"),
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    ON_HOLD("In Hold"),
    OPEN("Open");
    private final String status;

    Status(String status) {
        this.status = status;
    }
}
