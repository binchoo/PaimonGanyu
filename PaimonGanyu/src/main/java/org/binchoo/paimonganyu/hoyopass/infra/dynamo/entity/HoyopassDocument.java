package org.binchoo.paimonganyu.hoyopass.infra.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Builder;
import lombok.Getter;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.utils.dynamo.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(toBuilder = true)
@DynamoDBDocument
public class HoyopassDocument {

    protected static final String TABLE_NAME = "hoyopass";
    public static final String GLOBAL_RANGEINDEX_CREAT_AT = "hoyopassCreateAt";

    /**
     * 통행증 고유의 ltuid
     */
    private String ltuid;

    /**
     * ltuid에 대응하는 ltoken
     */
    private String ltoken;

    /**
     * 이 통행증에 연결된 UID들 리스트
     */
    private List<UidDocument> uidDocuments;

    /**
     * 해당 통행증 객체가 생성된 시간. 정렬 쿼리를 위한 {@link DynamoDBIndexRangeKey}를 적용.
     */
    @DynamoDBTypeConverted(
            converter = LocalDateTimeStringConverter.class)
    @DynamoDBIndexRangeKey(
            globalSecondaryIndexName = GLOBAL_RANGEINDEX_CREAT_AT)
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    public Hoyopass toDomain() {
        return Hoyopass.builder()
                .ltuid(this.ltuid)
                .ltoken(this.ltoken)
                .uids(this.uidDocuments.stream()
                        .map(UidDocument::toDomain).collect(Collectors.toList()))
                .build();
    }

    public static HoyopassDocument fromDomain(Hoyopass hoyopass) {
        return new HoyopassDocument(hoyopass.getLtuid(), hoyopass.getLtoken(),
            hoyopass.getUids().stream().map(UidDocument::fromDomain).collect(Collectors.toList()),
            hoyopass.getCreateAt());
    }
}
