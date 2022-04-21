package org.binchoo.paimonganyu.redeem.driving;

import org.binchoo.paimonganyu.redeem.RedeemResult;
import org.binchoo.paimonganyu.redeem.RedeemTask;

import java.util.Collection;
import java.util.List;

/**
 * {@link RedeemTask}에 명세된 코드 리딤 작업을 수행합니다.
 * @author : jbinchoo
 * @since : 2022-04-21
 */
public interface RedeemerService {

    /**
     * {@link RedeemTask}에 명세된 코드 리딤 작업을 수행합니다.
     * @param redeemTask 리딤 태스크 명세
     * @return 리딤 태스크 수행 결과
     */
    RedeemResult redeem(RedeemTask redeemTask);

    /**
     * {@link RedeemTask}에 명세된 코드 리딤 작업을 수행합니다.
     * @param redeemTasks 리딤 태스크 명세 집단
     * @return 리딤 태스크 수행 결과 집단
     */
    List<RedeemResult> redeem(Collection<RedeemTask> redeemTasks);
}
