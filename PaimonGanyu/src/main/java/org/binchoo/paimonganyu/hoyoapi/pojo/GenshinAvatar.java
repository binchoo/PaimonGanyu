package org.binchoo.paimonganyu.hoyoapi.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Setter
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GenshinAvatar {

    private long id;
    private String image;
    private String icon;
    private String name;
    private String element; // 원소 속성
    private int fetter; // 호감도
    private int level;
    private int rarity; // 몇 성급
    private Weapon weapon;
    private List<Reliquary> reliquaries; // 장착 성유물
    private List<Constellation> constellations; // 별자리
    private int activatedConstellationNum; // 별자리 돌파 수
    private List<Costume> costumes;

    public boolean isLumine() {
        return this.id == 10000007;
    }

    public boolean isAether() {
        return this.id == 10000005;
    }

    @ToString
    @Setter
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Weapon {

        private long id;
        private String name;
        private String icon;
        private int type;
        private int rarity;
        private int level;
        private int promoteLevel; // 무기 돌파 수
        private String typeName;
        private String desc;
        private int affixLevel; // 재련 횟수
    }

    @ToString
    @Setter
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Reliquary {

        private long id;
        private String name;
        private String icon;
        private int pos;
        private int rarity;
        private int level;
        private ReliquarySet set;
        private String posName;

        @ToString
        @Setter
        @Getter
        public static class ReliquarySet {

            private long id;
            private String name;
            private List<Affix> affixes;

            @ToString
            @Setter
            @Getter
            @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
            public static class Affix {

                private int activationNumber; // 성유물 세트 효과 발동 조건
                private String effect; // 성유물 세트 효과 설명
            }
        }
    }

    @ToString
    @Setter
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Constellation {

        private long id;
        private String name;
        private String icon;
        private String effect;
        private boolean isActivated;
        private int pos;
    }

    @ToString
    @Setter
    @Getter
    public static class Costume {

        private long id;
        private String name;
        private String icon;
    }
}