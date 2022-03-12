package org.binchoo.paimonganyu.hoyopass.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@DynamoDBTable(tableName = Uid.TABLE_NAME)
public class Uid {
    public static final String TABLE_NAME = "uid";

    @DynamoDBHashKey
    private String uid;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute
    private Region region;

    @DynamoDBAttribute
    private Integer characterLevel;

    @DynamoDBAttribute
    private String characterName;

    @DynamoDBAttribute
    @DynamoDBConvertedBool(DynamoDBConvertedBool.Format.Y_N)
    private Boolean isLumine;

    public Uid(String uid, Region region, int characterLevel, String characterName, boolean isLumine) {
        this.uid = uid;
        this.region = region;
        this.characterLevel = characterLevel;
        this.characterName = characterName;
        this.isLumine = isLumine;
    }

    public enum Region {
        OS_USA, OS_EURO, OS_ASIA, OS_CHT;

        public String lowercase() {
            return this.name().toLowerCase();
        }
    }
}
