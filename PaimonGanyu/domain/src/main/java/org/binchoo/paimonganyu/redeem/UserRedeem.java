package org.binchoo.paimonganyu.redeem;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드 리딤 수행 이력을 표상하는 객체입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public class UserRedeem {

    private final String botUserId;
    private final String ltuid;
    private final RedeemCode redeemCode;

    private UserRedeemStatus status = UserRedeemStatus.QUEUED;

    public UserRedeem(String botUserId, String ltuid, RedeemCode redeemCode, boolean isDone) {
        this(botUserId, ltuid, redeemCode);
        if (isDone) this.status = UserRedeemStatus.COMPLETED;
    }

    /**
     * 이력이 {@code 완료(Completed)} 또는 {@code 중복(Duplicate)}으로 기록되었다면
     * 관련 유저에게 리딤 코드가 잘 들어간 것이므로 {@code 완수(Done)}로 판단합니다.
     * @return 이 이력이 완수 이력인지 여부
     */
    public boolean isDone() {
        return isCompleted() || isDuplicate();
    }

    private boolean isCompleted() {
        return UserRedeemStatus.COMPLETED.equals(this.status);
    }

    private boolean isDuplicate() {
        return UserRedeemStatus.DUPLICATE.equals(this.status);
    }
}
