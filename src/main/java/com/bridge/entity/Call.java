package com.bridge.entity;

import com.bridge.entity.card.CallType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
//import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class Call implements Serializable {
    private Long gameId; //  todo : both of gameId & playerId will be moved into token or other place.

    private String playerId;

    @NotNull
    private CallType callType;

    @NotNull
    private Integer number;

    public Boolean validate(Call lastCall) {
        if (CallType.PASS.equals(this.callType)) {
            return true;
        } else {
            if (lastCall.getNumber() < this.getNumber()) {
                return true;
            } else if (lastCall.getNumber() > this.getNumber()) {
                return false;
            } else {    //  lastCall.getNumber() == this.getNumber()
                if (lastCall.getCallType().getLevel() >= this.getCallType().getLevel()) {
                    return false;
                }
                return true;
            }
        }
    }
}
