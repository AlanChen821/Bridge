package com.bridge.entity;

import com.bridge.entity.card.BidSuit;
import com.bridge.entity.card.PokerSuit;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
//import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class Bid {
    private String gameId; //  todo : both of gameId & playerId will be moved into token or other place.

    private String playerId;

    @NotNull
    private BidSuit bidSuit;

    @NotNull
    private Integer number;

    public Boolean validate(Bid lastBid) {
        if (BidSuit.PASS.equals(this.bidSuit)) {
            return true;
        } else {
            if (lastBid.getNumber() < this.getNumber()) {
                return true;
            } else if (lastBid.getNumber() > this.getNumber()) {
                return false;
            } else {    //  lastBid.getNumber() == this.getNumber()
                if (lastBid.getBidSuit().getLevel() >= this.getBidSuit().getLevel()) {
                    return false;
                }
                return true;
            }
        }
    }
}
