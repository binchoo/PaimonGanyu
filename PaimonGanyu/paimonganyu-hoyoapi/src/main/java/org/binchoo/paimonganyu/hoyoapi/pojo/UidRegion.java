package org.binchoo.paimonganyu.hoyoapi.pojo;

public enum UidRegion {

    OS_USA, OS_EURO, OS_ASIA, OS_CHT;

    public String lowercase() {
        return this.name().toLowerCase();
    }

    public static UidRegion fromString(String s) {
        return UidRegion.valueOf(s.toUpperCase());
    }
}