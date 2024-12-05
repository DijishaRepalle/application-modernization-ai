package com.bilvantis.constants;

import lombok.Getter;

@Getter
public enum ProcessErrorEnum {
    PROCESS_STEPS_NOT_FOUND("PS_404_001", "Process Steps Not Found");

    private final String errorId;
    private final String message;

    ProcessErrorEnum(String errorId, String message) {
        this.errorId = errorId;
        this.message = message;
    }

}
