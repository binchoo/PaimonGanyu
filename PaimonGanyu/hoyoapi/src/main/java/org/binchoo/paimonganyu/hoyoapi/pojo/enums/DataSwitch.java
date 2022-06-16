package org.binchoo.paimonganyu.hoyoapi.pojo.enums;

/**
 * @author : jbinchoo
 * @since : 2022-06-16
 */
public enum DataSwitch {

    ChronicleOnProfile(1), CharacterDetails(2), DailyNotes(3);

    private int id;

    DataSwitch(int id) {
        this.id = id;
    }

    public int switchId() {
        return id;
    }
}
