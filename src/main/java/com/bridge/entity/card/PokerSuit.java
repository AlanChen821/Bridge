package com.bridge.entity.card;

public enum PokerSuit {
    SPADE(4),
    HEART(3),
    DIAMOND(2),
    CLUB(1);

    private int order;

    PokerSuit(int order) {
        this.order = order;
    }

    public static PokerSuit getPokerSuit(int number) {
        int quotient = number / 13;
        switch (quotient) {
            case 0:
                return SPADE;
            case 1:
                return HEART;
            case 2:
                return DIAMOND;
            case 3:
            default:
                return CLUB;
        }
    }

    public int getOrder() {
        return this.order;
    }
}
