package com.bridge.entity;

import com.bridge.entity.card.BidSuit;
import com.bridge.entity.card.Card;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Play extends BaseClass {
    private Card card;
}
