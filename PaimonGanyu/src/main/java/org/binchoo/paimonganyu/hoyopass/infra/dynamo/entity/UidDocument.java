package org.binchoo.paimonganyu.hoyopass.infra.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBConvertedBool;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.binchoo.paimonganyu.hoyopass.domain.Region;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;

@Setter
@Getter
@Builder
@AllArgsConstructor
@DynamoDBDocument
public class UidDocument {

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

    public UidDocument() { }

    public Uid toDomain() {
        return Uid.builder()
                .uidString(this.uidString)
                .characterLevel(this.characterLevel).characterName(this.characterName)
                .region(this.region)
                .isLumine(this.isLumine)
                .build();
    }

    public static UidDocument fromDomain(Uid uid) {
        return UidDocument.builder()
                .uidString(uid.getUidString())
                .characterLevel(uid.getCharacterLevel()).characterName(uid.getCharacterName())
                .region(uid.getRegion())
                .isLumine(uid.getIsLumine())
                .build();
    }
}
