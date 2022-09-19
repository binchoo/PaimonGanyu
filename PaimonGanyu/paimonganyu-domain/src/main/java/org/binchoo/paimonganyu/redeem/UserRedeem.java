package org.binchoo.paimonganyu.redeem;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 코드 리딤 수행과 결과를 표상하는 객체입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Getter
@Builder
@EqualsAndHashCode(exclude = "date")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRedeem {

    private final String botUserId;
    private final String uid;
    private final RedeemCode redeemCode;
    private final String reason;
    private boolean done;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    /**
     * @return 이 이력이 완수 이력인지 여부
     */
    public boolean isDone() {
        return this.done;
    }

    public UserRedeem markDone() {
        return new UserRedeem(botUserId, uid, redeemCode, reason, true, date);
    }

    @Override
    public String toString() {
        // TODO: change string format
        return String.format("%s %s %s", done, date, redeemCode.getCode());
    }
}
