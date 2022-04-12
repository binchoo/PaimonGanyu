package org.binchoo.paimonganyu.hoyopass;

public enum Region {

    OS_USA, OS_EURO, OS_ASIA, OS_CHT;

    public String lowercase() {
        return this.name().toLowerCase();
    }

    public static Region fromString(String s) {
        return Region.valueOf(s.toUpperCase());
    }
}