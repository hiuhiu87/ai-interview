package com.aiinterview.common.constant;

import lombok.Getter;

@Getter
public enum TagType {

    GOOD("good"),
    BAD("bad"),
    NEUTRAL("neutral"),
    SKIP("skip");

    private final String text;

    TagType(String text) {
        this.text = text;
    }

}
