package com.bridge.entity;

import com.bridge.entity.card.BidSuit;
import jakarta.validation.constraints.NotNull;

public class Play extends BaseClass {
    @NotNull
    private BidSuit bidSuit;

    @NotNull
    private Integer number;
}
