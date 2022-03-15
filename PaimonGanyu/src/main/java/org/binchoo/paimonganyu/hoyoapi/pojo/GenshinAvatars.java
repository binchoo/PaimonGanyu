package org.binchoo.paimonganyu.hoyoapi.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Setter
@Getter
public class GenshinAvatars {

    private List<GenshinAvatar> avatars;

    public boolean containsLumine() {
        return 1 == avatars.stream()
                .filter(GenshinAvatar::isLumine)
                .count();
    }
}