package com.aiinterview.common.constant;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("admin"),
    INTERVIEWER("interviewer"),
    HR("hr");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

}
