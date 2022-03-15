package org.binchoo.paimonganyu.hoyopass.domain;

public enum Region {

    OS_USA, OS_EURO, OS_ASIA, OS_CHT;

    public String lowercase() {
        return this.name().toLowerCase();
    }
}