package com.bridge.entity.card;

import lombok.Getter;

@Getter
public enum CallType {
    NO_KING(5),
    SPADE(4),
    HEART(3),
    DIAMOND(2),
    CLUB(1),
    PASS(0);

    private int level;

    CallType(int level) {
        this.level = level;
    }

    public boolean isPass() {
        return this.equals(PASS);
    }
}
