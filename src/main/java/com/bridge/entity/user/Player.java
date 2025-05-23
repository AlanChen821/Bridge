package com.bridge.entity.user;

import com.bridge.entity.card.Card;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Player implements Serializable {
    private Long id;

    private String account;

    private String accountMd5;

    private String accountBase64;

    private String password;

    private String name;

    /**
     * @see com.bridge.enumeration.PlayerType
     */
    private Integer type;

    private List<Card> cards;

    private Integer points;

    private String token;

    public Player(String account) {
        this.account = account;
        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = this.account.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
//            MessageDigest md = MessageDigest.getInstance("MD5");
        this.accountMd5 = DigestUtils.md5DigestAsHex(bytesOfMessage);

        final Base64.Encoder encoder = Base64.getEncoder();

    }

    public void encryptPassword() {
        byte[] bytesOfPassword;
        try {
            bytesOfPassword = this.password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.password = DigestUtils.md5DigestAsHex(bytesOfPassword);
    }
}
