package org.binchoo.paimonganyu.redeem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;


/**
 * 코드 리딤 태스크를 표상합니다. 작업 큐로 발송되어 작업자에 의해 소비됩니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedeemTask {

    private String botUserId;
    private HoyopassCredentials credentials;
    private Uid uid;
    private RedeemCode redeemCode;

    public String getJson(ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(String.format(
                    "Could not deserialize a CodeRedeemTask: %s", this), e);
            throw new IllegalStateException(e);
        }
    }

    @JsonIgnore
    public String getLtuid() {
        return this.credentials.getLtuid();
    }

    @JsonIgnore
    public String getCookieToken() {
        return this.credentials.getCookieToken();
    }

    @JsonIgnore
    public String getUidString() {
        return this.uid.getUidString();
    }

    @JsonIgnore
    public String getRegionString() {
        return this.uid.getRegion().lowercase();
    }

    @JsonIgnore
    public String getCodeString() {
        return this.redeemCode.getCode();
    }
}
