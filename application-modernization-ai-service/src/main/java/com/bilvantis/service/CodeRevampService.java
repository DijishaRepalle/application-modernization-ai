package com.bilvantis.service;

import com.bilvantis.model.CodeRevampProcess;
import com.bilvantis.model.CodeRevampProcessSteps;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CodeRevampService {

    void createRevampProcessSteps(List<CodeRevampProcessSteps> codeRevamp);

    void createRevampProcess(CodeRevampProcess codeRevamp);

    @Transactional
    void createCodeRevampProcessSchedule(String projectCode, String processName);
}
