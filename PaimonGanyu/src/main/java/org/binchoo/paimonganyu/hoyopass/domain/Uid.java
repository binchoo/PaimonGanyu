package org.binchoo.paimonganyu.hoyopass.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBConvertedBool;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@DynamoDBDocument
public class Uid {

    /**
     * 원신 계정의 UID
     */
    private String uidString;

    /**
     * 원신 계정의 레벨
     */
    private Integer characterLevel;

    /**
     * 원신 계정의 이름
     */
    private String characterName;

    /**
     * 원신 계정의 서버
     */
    @DynamoDBTypeConvertedEnum
    private Region region;

    /**
     * 여행자? 남행자?
     */
    @DynamoDBConvertedBool(DynamoDBConvertedBool.Format.Y_N)
    private Boolean isLumine;
}
