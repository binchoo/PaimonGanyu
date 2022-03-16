package org.binchoo.paimonganyu.hoyoapi.pojo;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class GenshinAvatars {

    private List<GenshinAvatar> avatars;

    public boolean containsLumine() {
        return avatars.stream().anyMatch(GenshinAvatar::isLumine);
    }
}