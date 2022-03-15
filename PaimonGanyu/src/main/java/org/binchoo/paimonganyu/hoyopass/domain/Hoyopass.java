package org.binchoo.paimonganyu.hoyopass.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.binchoo.paimonganyu.hoyopass.utils.dynamo.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@Builder(toBuilder = true)
@DynamoDBTable(tableName = Hoyopass.TABLE_NAME)
public class Hoyopass implements Comparable<Hoyopass> {

    protected static final String TABLE_NAME = "hoyopass";
    public static final String GLOBAL_RANGEINDEX_CREAT_AT = "hoyopassCreateAt";

    /**
     * 통행증 고유의 ltuid
     */
    @DynamoDBIndexHashKey
    private String ltuid;

    /**
     * ltuid에 대응하는 ltoken
     */
    private String ltoken;

    /**
     * 챗봇이 식별하는 유저 고유 값
     */
    private String botUserId;

    /**
     * 이 통행증에 연결된 UID들 리스트
     */
    private List<Uid> uids;

    /**
     * 해당 통행증 객체가 생성된 시간. 정렬 쿼리를 위한 {@link DynamoDBIndexRangeKey}를 적용.
     */
    @DynamoDBTypeConverted(
            converter = LocalDateTimeStringConverter.class)
    @DynamoDBIndexRangeKey(
            globalSecondaryIndexName = GLOBAL_RANGEINDEX_CREAT_AT)
    private LocalDateTime createAt;

    public Hoyopass() {
        this.createAt = LocalDateTime.now();
    }

    public Hoyopass(String botUserId, String ltuid, String ltoken, Uid... uids) {
        this();
        Collections.addAll(this.uids, uids);
    }

    public Hoyopass(String botUserId, String ltuid, String ltoken, List<Uid> uids, LocalDateTime createAt) {
        this.botUserId = botUserId;
        this.ltuid = ltuid;
        this.ltoken = ltoken;
        this.uids = uids;
        this.createAt = createAt;
    }

    @Override
    public int compareTo(Hoyopass o) {
        return this.createAt.compareTo(o.getCreateAt());
    }
}
