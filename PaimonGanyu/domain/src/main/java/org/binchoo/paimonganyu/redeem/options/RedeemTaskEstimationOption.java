package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 주어진 유저와 리딤 코드로 코드 리딤 태스크 명세를 생성합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemTaskEstimationOption {

    private UserProvider userProvider = null;
    private CodeProvider codeProvider = null;

    /**
     * @return {@link} UserProvider가 나이브하게 제공한 모든 유저
     * @throws NullPointerException userProvider가 null일 때
     */
    public List<UserHoyopass> getUsers() {
        Objects.requireNonNull(userProvider);
        return this.userProvider.provide();
    }

    /**
     * @return {@link} CodeProvider가 나이브하게 제공한 리딤 코드들
     * @throws NullPointerException codeProvider가 null일 때
     */
    public List<RedeemCode> getCodes() {
        Objects.requireNonNull(codeProvider);
        return this.codeProvider.provide();
    }

    public RedeemTaskEstimationOption withCodeProvider(CodeProvider codeProvider) {
        this.codeProvider = codeProvider;
        return this;
    }

    public RedeemTaskEstimationOption withUserProvider(UserProvider userProvider) {
        this.userProvider = userProvider;
        return this;
    }

    /**
     * <p> 제공 받은 유저와 코드를 곱연산하여 리딤 태스크 명세를 작성합니다.
     * <p> {@code O(|uids|*|codes|)} = {@code O(|users|*2*4*|codes|)}
     * @return 리딤 태스크 명세 리스트
     * @throws NullPointerException userProvider 또는 codeProvider가 null일 때
     */
    public List<RedeemTask> estimateTask() {
        return this.multiply(this.getUsers(), this.getCodes());
    }

    private List<RedeemTask> multiply(List<UserHoyopass> users, List<RedeemCode> redeemCodes) {
        List<RedeemTask> taskContainer = new ArrayList<>(users.size() * 2);
        for (UserHoyopass user : users) {
            for (RedeemCode code : redeemCodes) {
                fillTaskContainer(taskContainer, user, code);
            }
        }
        return taskContainer;
    }

    private void fillTaskContainer(List<RedeemTask> taskContainer, UserHoyopass user, RedeemCode redeemCode) {
        String botUserId = user.getBotUserId();
        for (Hoyopass hoyopass : user.getHoyopasses()) {
            fillTaskContainer(taskContainer, botUserId, hoyopass, redeemCode);
        }
    }

    private void fillTaskContainer(List<RedeemTask> taskContainer,
                                   String botUserId, Hoyopass hoyopass, RedeemCode redeemCode) {
        HoyopassCredentials credentials = hoyopass.getCredentials();
        for (Uid uid : hoyopass.getUids()) {
            taskContainer.add(
                    createRedeemTask(botUserId, credentials, uid, redeemCode));
        }
    }

    private RedeemTask createRedeemTask(String botUserId,
                                        HoyopassCredentials credentials, Uid uid, RedeemCode redeemCode) {
        return RedeemTask.builder()
                .botUserId(botUserId)
                .credentials(credentials)
                .uid(uid)
                .redeemCode(redeemCode)
                .build();
    }

    @FunctionalInterface
    public interface CodeProvider {

        List<RedeemCode> provide();
    }

    @FunctionalInterface
    public interface UserProvider {

        List<UserHoyopass> provide();
    }
}
