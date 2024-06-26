package com.bridge.entity.user;

import com.bridge.entity.card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Player {
    private String id;

    private String account;

    private List<Card> cards;

    private Integer points;
}
