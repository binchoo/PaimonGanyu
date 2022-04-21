package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.redeem.RedeemResultCallback;
import org.binchoo.paimonganyu.redeem.RedeemTask;

import java.util.Collection;

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
     */
    void redeem(Collection<RedeemTask> redeemTasks, RedeemResultCallback resultCallback);
}
