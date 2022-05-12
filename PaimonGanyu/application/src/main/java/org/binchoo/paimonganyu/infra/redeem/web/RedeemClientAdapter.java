package org.binchoo.paimonganyu.infra.redeem.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.HoyoCodeRedemptionApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.AccountIdCookieToken;
import org.binchoo.paimonganyu.hoyoapi.pojo.CodeRedemptionResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.redeem.RedeemResultCallback;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.RedeemClientPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedeemClientAdapter implements RedeemClientPort {

    private final HoyoCodeRedemptionApi redemptionApi;

    @Override
    public void redeem(Collection<RedeemTask> redeemTasks, RedeemResultCallback resultCallback) {
        List<Mono<UserRedeem>> userRedeemList = sendRequest(redeemTasks);
        if (userRedeemList != null && resultCallback != null) {
            for (Mono<UserRedeem> userRedeem : userRedeemList) {
                resultCallback.handleResult(userRedeem.block());
            }
        }
    }

    /**
     * 비동기 리뎀션 API를 호출하여, 태스크 수행 이력의 Mono 리스트를 얻습니다.
     * @param redeemTasks 리딤 태스크 명세
     * @return 태스크 수행 이력 Mono 리스트
     */
    private List<Mono<UserRedeem>> sendRequest(Collection<RedeemTask> redeemTasks) {
        List<Mono<UserRedeem>> userRedeemList = null;
        for (RedeemTask redeemTask : redeemTasks) {
            Mono<UserRedeem> userRedeem = sendRequest(redeemTask);
            if (userRedeemList == null)
                userRedeemList = new ArrayList<>();
            userRedeemList.add(userRedeem);
        }
        return userRedeemList;
    }

    /**
     * 비동기 리뎀션 API를 호출한 뒤, 응답을 태스크 수행 이력의 Mono 타입으로 변환하는 구독자를 붙입니다.
     * @param redeemTask 리딤 태스크 명세
     * @return 태스크 수행 이력 Mono
     */
    private Mono<UserRedeem> sendRequest(RedeemTask redeemTask) {
        String accountId = redeemTask.getLtuid();
        String cookieToken = redeemTask.getCookieToken();
        String uid = redeemTask.getUidString();
        String server = redeemTask.getRegionString();
        String code = redeemTask.getCodeString();
        UserRedeem userRedeem = new UserRedeem(redeemTask.getBotUserId(), uid, redeemTask.getRedeemCode());

        var response = redemptionApi.redeem(
                new AccountIdCookieToken(accountId, cookieToken), uid, server, code);

        return wrap(response, userRedeem);
    }

    private Mono<UserRedeem> wrap(Mono<HoyoResponse<CodeRedemptionResult>> redemptionResponse, UserRedeem userRedeem) {
        return redemptionResponse.map((res)-> userRedeem.markDone())
                .onErrorReturn(userRedeem);
    }
}
