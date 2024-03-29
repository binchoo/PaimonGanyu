package org.binchoo.paimonganyu.infra.redeem.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.CodeRedemptionAsyncApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.AccountIdCookieToken;
import org.binchoo.paimonganyu.hoyoapi.pojo.CodeRedemptionResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.redeem.RedeemResultCallback;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.RedemptionClientPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedemptionClientAdapter implements RedemptionClientPort {

    private final CodeRedemptionAsyncApi redemptionApi;

    @Override
    public List<UserRedeem> redeem(Collection<RedeemTask> redeemTasks, RedeemResultCallback resultCallback) {
        if (redeemTasks != null && !redeemTasks.isEmpty())
            return sendRequest(redeemTasks, resultCallback);
        return List.of();
    }

    /**
     * 리뎀션 API를 호출하여 유저 리뎀션 수행 이력을 Mono 타입의 리스트로 얻습니다.
     * 각 Mono에는 인자로 제공된 구독자가 붙습니다.
     * 이 메서드는 모든 API 호출에 대한 결과물이 산출될 때까지 대기합니다.
     * @param redeemTasks 리딤 태스크 명세
     * @return 유저 리뎀션 이력 {@link UserRedeem} 리스트
     */
    private List<UserRedeem> sendRequest(Collection<RedeemTask> redeemTasks, RedeemResultCallback resultCallback) {
        List<Mono<UserRedeem>> userRedeemList = new ArrayList<>();
        for (RedeemTask redeemTask : redeemTasks) {
            Mono<UserRedeem> userRedeem = sendRequest(redeemTask);
            if (resultCallback != null)
                userRedeem.subscribe(resultCallback::handleResult);
            userRedeemList.add(userRedeem);
        }
        return wait(userRedeemList);
    }

    private List<UserRedeem> wait(List<Mono<UserRedeem>> userRedeemMonos) {
        List<UserRedeem> userRedeems = new ArrayList<>();
        wait(userRedeemMonos, userRedeems);
        log.debug("User redemptions (count {}) have occurred: {}", userRedeems.size(), userRedeems);
        return userRedeems;
    }

    private void wait(List<Mono<UserRedeem>> userRedeemMonos, List<UserRedeem> resultContainer) {
        for (Mono<UserRedeem> userRedeemMono : userRedeemMonos) {
            UserRedeem userRedeemObj = userRedeemMono.block();
            if (resultContainer != null)
                resultContainer.add(userRedeemObj);
        }
    }

    /**
     * 리뎀션 API를 호출하여 유저 리뎀션 수행 이력을 Mono 타입 리스트로 얻습니다.
     * @param redeemTask 리딤 태스크 명세
     * @return 유저 리뎀션 이력 {@link UserRedeem}의 Mono 타입 리스트
     */
    private Mono<UserRedeem> sendRequest(RedeemTask redeemTask) {
        String accountId = redeemTask.getLtuid();
        String cookieToken = redeemTask.getCookieToken();
        String uid = redeemTask.getUidString();
        String server = redeemTask.getRegionString();
        String code = redeemTask.getCodeString();
        String reason = redeemTask.getReason();
        UserRedeem userRedeem = UserRedeem.builder()
                .botUserId(redeemTask.getBotUserId())
                .uid(uid)
                .redeemCode(redeemTask.getRedeemCode())
                .reason(reason)
                .build();

        var response = redemptionApi.redeem(
                new AccountIdCookieToken(accountId, cookieToken), uid, server, code);

        return wrap(response, userRedeem);
    }

    private Mono<UserRedeem> wrap(Mono<HoyoResponse<CodeRedemptionResult>> response, UserRedeem userRedeem) {
        return response
                .flatMap(hoyoResponse-> Mono.just(userRedeem.markDone()))
                .onErrorResume(e-> Mono.just(userRedeem));
    }
}
