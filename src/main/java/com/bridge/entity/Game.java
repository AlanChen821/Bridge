package com.bridge.entity;

import com.bridge.utils.LocalDateTimeUtils;
import com.bridge.entity.card.CallType;
import com.bridge.entity.user.Player;
import com.bridge.enumeration.GameStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
//@Table
//@NoArgsConstructor
public class Game {

    @Id
    private Long id;

    private String roomName = "test";

    private CallType trump;
    private Integer level;

    @Transient
    private List<Player> players;

    @Transient
    private List<Round> rounds;

    @Transient
    private List<Call> callHistory;

    /**
     * @see com.bridge.enumeration.GameStatus
     */
    private Integer status;
    private String createTime;

    @Transient
    private String updateTime;

    public Game() {
        this.players = new ArrayList<>();
        this.createTime = LocalDateTimeUtils.getStringOfNow();
        this.status = GameStatus.WAITING.getCode();

    }

    public Game(Player firstPlayer) {
        this();

        this.players.add(firstPlayer);
    }

    public void addNewPlayer(Player newPlayer) {
        this.players.add(newPlayer);
    }
}
