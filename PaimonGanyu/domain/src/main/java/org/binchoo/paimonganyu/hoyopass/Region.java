package org.binchoo.paimonganyu.hoyopass;

public enum Region {

    OS_USA, OS_EURO, OS_ASIA, OS_CHT;

    public String lowercase() {
        return this.name().toLowerCase();
    }

    public String uppercase() {
        return this.name().toUpperCase();
    }

    public String suffixLowerCase() {
        return this.cutPrefix().toLowerCase();
    }

    public String suffixLargeCase() {
        return this.cutPrefix().toUpperCase();
    }

    private String cutPrefix() {
        return this.name().substring(3);
    }

    public static Region fromString(String s) {
        try {
            return Region.valueOf(s.toUpperCase());
        } catch (Exception e) {
        }
        return OS_ASIA;
    }
}