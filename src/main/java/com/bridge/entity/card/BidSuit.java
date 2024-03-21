package com.bridge.entity.card;

import lombok.Getter;

@Getter
public enum BidSuit {
    NO_KING(5),
    SPADE(4),
    HEART(3),
    DIAMOND(2),
    CLUB(1),
    PASS(0);

    private int level;

    BidSuit(int level) {
        this.level = level;
    }

    public boolean isPass() {
        return this.equals(PASS);
    }
}
