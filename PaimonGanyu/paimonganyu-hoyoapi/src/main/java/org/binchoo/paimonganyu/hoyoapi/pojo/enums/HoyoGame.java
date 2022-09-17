package org.binchoo.paimonganyu.hoyoapi.pojo.enums;

/**
 * @author : jbinchoo
 * @since : 2022-06-16
 */
public enum HoyoGame {

    HONKAI_IMPACT(1, GameBiz.bh3_global),
    GENSHIN_IMPACT(2, GameBiz.hk4e_global);

    private int id;
    private GameBiz gameBiz;

    HoyoGame(int id, GameBiz gameBiz) {
        this.id = id;
        this.gameBiz = gameBiz;
    }

    public int gameId() {
        return this.id;
    }

    public GameBiz gameBiz() {
        return this.gameBiz;
    }

    public String gameBizString() {
        return this.gameBiz.toString();
    }

    public boolean gameBizEquals(String gameBiz) {
        return gameBiz.equalsIgnoreCase(this.gameBiz.toString());
    }

    public boolean gameBizEquals(GameBiz gameBiz) {
        return gameBiz.equals(this.gameBiz);
    }

    public enum GameBiz {
        hk4e_global, bh3_global;
    }
}
