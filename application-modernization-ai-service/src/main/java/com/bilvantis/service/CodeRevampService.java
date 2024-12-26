package com.bilvantis.service;

import com.bilvantis.model.CodeRevamp;
import com.bilvantis.model.CodeRevampProcess;
import com.bilvantis.model.CodeRevampProcessSteps;
import org.springframework.transaction.annotation.Transactional;

public interface CodeRevampService {
    void createCodeRevampProcess(CodeRevamp codeRevamp);

    void createRevampProcessSteps(CodeRevampProcessSteps codeRevamp);

    void createRevampProcess(CodeRevampProcess codeRevamp);

    @Transactional
    void createCodeRevampProcessSchedule(String projectCode, String processName);
}
