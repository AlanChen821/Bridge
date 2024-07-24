package com.bridge.entity.user;

import com.bridge.entity.card.Card;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Player implements Serializable {
    private String id;

    private String account;

    private List<Card> cards;

    private Integer points;
}
