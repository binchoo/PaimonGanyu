package org.binchoo.paimonganyu.redeem;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드 리딤 수행과 결과를 표상하는 객체입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRedeem {

    private final String botUserId;
    private final String uid;
    private final RedeemCode redeemCode;
    private boolean done;

    /**
     * @return 이 이력이 완수 이력인지 여부
     */
    public boolean isDone() {
        return this.done;
    }

    public UserRedeem markDone() {
        return new UserRedeem(botUserId, uid, redeemCode, true);
    }
}
