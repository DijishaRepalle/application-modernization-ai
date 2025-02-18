package com.bilvantis.serviceImpl;

import java.util.Map;

public class ProcessConstants {
    public static final Map<String, String> PROCESS_PREFIX_MAP = Map.of(
            "scan-code", "JOB",
            "Code_Revamp", "CMR"
    );

    public static final String DEFAULT_PREFIX = "JOB";
}
