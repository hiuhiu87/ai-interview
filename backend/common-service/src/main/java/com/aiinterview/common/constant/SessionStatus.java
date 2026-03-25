package com.aiinterview.common.constant;

import lombok.Getter;

@Getter
public enum SessionStatus {
    PLANNED("planted"),
    ONGOING("ongoing"),
    COMPLETED("completed");

    private final String text;

    SessionStatus(String text) {
        this.text = text;
    }

}
