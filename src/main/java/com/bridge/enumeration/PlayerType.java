package com.bridge.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum PlayerType {
    USER(1, "普通會員"),
    TEST(2, "測試會員");

    private final int code;
    private final String message;

    PlayerType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonCreator
    public static Optional<PlayerType> getByCode(int code) {
        for (PlayerType type : values()) {
            if (type.code == code) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}
