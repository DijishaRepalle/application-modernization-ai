package com.bilvantis.util;

import com.bilvantis.constants.ProcessErrorEnumConstants;
import lombok.Getter;

import static com.bilvantis.constants.ProcessErrorEnumConstants.PROCESS_ERROR_ID;

@Getter
public enum ProcessErrorEnum {
    PROCESS_STEPS_NOT_FOUND(PROCESS_ERROR_ID, ProcessErrorEnumConstants.PROCESS_STEPS_NOT_FOUND),
    PROCESS_ALREADY_IN_PROGRESS(PROCESS_ERROR_ID, "Process with project code already exists and is in progress");


    private final String errorId;
    private final String message;

    ProcessErrorEnum(String errorId, String message) {
        this.errorId = errorId;
        this.message = message;
    }

}
