package com.bridge.entity;

import com.bridge.entity.card.CallType;
import com.bridge.entity.user.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String gameId;
    private CallType trump;
    private Integer level;
    private List<Player> players;
    private List<Round> rounds;
    private List<Call> callHistory;
}
