package com.mhtcet.test_engine_service.model;

public enum AttemptStatus {
    SECTION_1_ACTIVE,  // Minutes 0-90: Only Physics & Chemistry unlocked
    SECTION_2_ACTIVE,  // Minutes 90-180: Section 1 locked; Maths or Biology unlocked
    SUBMITTED          // Exam completed and locked
}
