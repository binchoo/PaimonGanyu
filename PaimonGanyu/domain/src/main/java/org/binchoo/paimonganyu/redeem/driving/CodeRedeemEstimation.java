package org.binchoo.paimonganyu.redeem.driving;

import org.binchoo.paimonganyu.redeem.CodeRedeemTask;
import org.binchoo.paimonganyu.redeem.options.EstimationOption;

import java.util.List;

/**
 * <p> 코드 리딤 태스크를 견적냅니다.
 * <p> 견적에 필요한 정보는 두 가지 입니다. 1)코드 리딤 대상인 유저들 2)코드 리딤 대상인 리딤 코드들
 * <p> 두 자료는 {@link EstimationOption}로부터 제공받습니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface CodeRedeemEstimation {

    /**
     * <p> 주어진 견적 옵션으로 코드 리딤 태스크 사양을 견적냅니다.
     * <p> 제공되는 유저 집합 * 제공되는 리딤 코드 집합의 곱만큼 코드 리딤 태스크가 산출됩니다.
     * <p> 구현체는 이미 수행된 태스크 이력을 조회하여 반환에서 제외할 수 있습니다.
     * @param estimationOption 견적 옵션
     * @return 산출된 {@link CodeRedeemTask} 목록
     */
    List<CodeRedeemTask> generate(EstimationOption estimationOption);
}
