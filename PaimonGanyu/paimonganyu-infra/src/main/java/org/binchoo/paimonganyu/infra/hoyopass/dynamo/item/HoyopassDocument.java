package org.binchoo.paimonganyu.infra.hoyopass.dynamo.item;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.infra.utils.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@AllArgsConstructor
@DynamoDBDocument
public class HoyopassDocument {

    /**
     * 통행증 고유의 ltuid
     */
    private String ltuid;

    /**
     * ltuid에 대응하는 ltoken
     */
    private String ltoken;

    /**
     * ltuid에 대응하는 cookie_token
     */
    private String cookieToken;

    /**
     * 이 통행증에 연결된 UID들 리스트
     */
    private List<UidDocument> uidDocuments;

    /**
     * 해당 통행증 객체가 생성된 시간.
     */
    @DynamoDBTypeConverted(converter = LocalDateTimeStringConverter.class)
    private LocalDateTime createAt;

    public HoyopassDocument() {
        // required for dynamodb mapper
    }

    public Hoyopass toDomain() {
        return Hoyopass.builder()
                .credentials(HoyopassCredentials.builder()
                        .ltuid(ltuid)
                        .ltoken(ltoken)
                        .cookieToken(cookieToken)
                        .build())
                .uids(this.uidDocuments.stream()
                        .map(UidDocument::toDomain).collect(Collectors.toList()))
                .createAt(this.createAt)
                .build();
    }

    public static HoyopassDocument fromDomain(Hoyopass hoyopass) {
        return HoyopassDocument.builder()
                .ltuid(hoyopass.getLtuid())
                .ltoken(hoyopass.getLtoken())
                .cookieToken(hoyopass.getCookieToken())
                .uidDocuments(hoyopass.getUids().stream()
                        .map(UidDocument::fromDomain).collect(Collectors.toList()))
                .createAt(hoyopass.getCreateAt())
                .build();
    }
}
