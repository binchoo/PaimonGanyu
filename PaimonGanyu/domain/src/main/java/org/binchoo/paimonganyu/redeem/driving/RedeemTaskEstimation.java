package org.binchoo.paimonganyu.redeem.driving;

import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.options.EstimationOption;

import java.util.List;

/**
 * <p> 코드 리딤 태스크를 견적냅니다. 견적에 필요한 정보는 두 가지 입니다.
 * <p> {@code 1)코드 리딤 대상인 유저}
 * <p> {@code 2)코드 리딤 대상인 리딤 코드}
 * <p> 두 정보는 {@link EstimationOption}로부터 제공받습니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface RedeemTaskEstimation {

    /**
     * <p> 주어진 견적 옵션으로 코드 리딤 태스크 사양을 견적냅니다.
     * <p> 제공되는 {@code 유저 집합 x 제공되는 리딤 코드 집합}의 곱만큼 코드 리딤 태스크가 산출됩니다.
     * <p> 구현체는 이미 수행된 태스크 이력을 반환에서 제외할 수 있습니다.
     * @param estimationOption 견적 옵션
     * @return 산출된 {@link RedeemTask} 목록
     */
    List<RedeemTask> generateTasks(EstimationOption estimationOption);
}
