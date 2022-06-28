package org.binchoo.paimonganyu.hoyoapi.pojo.enums;

/**
 * @author : jbinchoo
 * @since : 2022-06-16
 */
public enum HoyoGame {
    HONKAI_IMPACT(1), GENSHIN_IMPACT(2);

    private int id;

    HoyoGame(int id) {
        this.id = id;
    }

    public int gameId() {
        return id;
    }
}
