package com.bridge.entity;

import com.bridge.utils.LocalDateTimeUtils;
import com.bridge.entity.card.CallType;
import com.bridge.entity.user.Player;
import com.bridge.enumeration.GameStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
//@Table
//@NoArgsConstructor
public class Game {

    public Game() {
        this.gameId = "1";
        this.players = new ArrayList<>();
        this.createTime = LocalDateTimeUtils.getStringOfNow();
        this.status = GameStatus.WAITING;

    }

    public Game(Player firstPlayer) {
        this();

        this.players.add(firstPlayer);
    }

    @Id
    private String gameId;

    private CallType trump;
    private Integer level;
    private List<Player> players;
    private List<Round> rounds;
    private List<Call> callHistory;
    private GameStatus status;
    private String createTime;
    private String updateTime;

    public void addNewPlayer(Player newPlayer) {
        this.players.add(newPlayer);
        if (this.players.size() == 4) {
            this.setStatus(GameStatus.CALLING);
        }
    }
}
