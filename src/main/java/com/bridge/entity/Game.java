package com.bridge.entity;

import com.bridge.utils.LocalDateTimeUtils;
import com.bridge.entity.card.CallType;
import com.bridge.entity.user.Player;
import com.bridge.enumeration.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
//@NoArgsConstructor
public class Game {

    public Game() {
        this.createTime = LocalDateTimeUtils.getStringOfNow();
    }

    private String gameId;
    private CallType trump;
    private Integer level;
    private List<Player> players;
    private List<Round> rounds;
    private List<Call> callHistory;
    private GameStatus status;
    private String createTime;
    private String updateTime;
}
