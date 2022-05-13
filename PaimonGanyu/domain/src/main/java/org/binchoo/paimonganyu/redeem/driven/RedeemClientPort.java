package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.redeem.RedeemResultCallback;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * 미호요 측 코드 리딤 기능에 접근하는 포트입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface RedeemClientPort {

    /**
     * 코드 리딤 요청을 전송합니다. 요청 결과를 수신하면 주입된 콜백을 실행하여 반응합니다.
     * @param redeemTasks 리딤 태스크 집단
     * @param resultCallback 각 리딤 태스크 요청 결과에 반응하는 콜백
     * @return 유저 리뎀션 이력 - {@link UserRedeem} 리스트
     */
    List<UserRedeem> redeem(@NonNull Collection<RedeemTask> redeemTasks,
                            @Nullable RedeemResultCallback resultCallback);
}
