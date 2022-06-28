package org.binchoo.paimonganyu.hoyoapi.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserGameRole {

    String gameUid;
    String gameBiz;
    int level;
    String nickname;
    String region; // "os_asia", "os_usa", ...
    String regionName;
    boolean isChosen;
    boolean isOfficial;

    public boolean isGenshinImpactRole() {
        return gameBiz.equals("hk4e_global");
    }
}