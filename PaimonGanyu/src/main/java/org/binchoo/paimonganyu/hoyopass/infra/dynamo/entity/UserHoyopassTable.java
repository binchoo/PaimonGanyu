package org.binchoo.paimonganyu.hoyopass.infra.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = UserHoyopassTable.TABLE_NAME)
public class UserHoyopassTable {

    public static final String TABLE_NAME = "UserHoyopass";

    /**
     * 카카오 챗봇이 식별한 유저 고유 아이디
     */
    @DynamoDBHashKey
    private String botUserId;

    /**
     * 유저의 통행증 리스트
     */
    @DynamoDBAttribute
    private List<HoyopassDocument> hoyopasses;

    public UserHoyopass toDomain() {
        return UserHoyopass.builder()
                .botUserId(this.botUserId)
                .hoyopasses(hoyopasses.stream()
                        .map(HoyopassDocument::toDomain).collect(Collectors.toList()))
                .build();
    }

    public static UserHoyopassTable fromDomain(UserHoyopass userHoyopass) {
        return new UserHoyopassTable(userHoyopass.getBotUserId(),
                userHoyopass.getHoyopasses().stream().map(HoyopassDocument::fromDomain).collect(Collectors.toList()));
    }
}
