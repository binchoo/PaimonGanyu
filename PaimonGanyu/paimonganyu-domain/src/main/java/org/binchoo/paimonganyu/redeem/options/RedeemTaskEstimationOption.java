package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemDeploy;
import org.binchoo.paimonganyu.redeem.RedeemTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 주어진 유저와 리딤 배포 명세를 토대로 코드 리딤 태스크 명세를 생성합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemTaskEstimationOption {

    private UserProvider userProvider = null;
    private RedeemDeployProvider redeemDeployProvider = null;

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
    public List<RedeemDeploy> getDeploys() {
        Objects.requireNonNull(redeemDeployProvider);
        return this.redeemDeployProvider.provide();
    }

    public RedeemTaskEstimationOption withDeployProvider(RedeemDeployProvider redeemDeployProvider) {
        this.redeemDeployProvider = redeemDeployProvider;
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
        return this.multiply(this.getUsers(), this.getDeploys());
    }

    private List<RedeemTask> multiply(List<UserHoyopass> users, List<RedeemDeploy> deploys) {
        List<RedeemTask> taskContainer = new ArrayList<>(users.size() * 2);
        for (UserHoyopass user : users) {
            for (RedeemDeploy deploy : deploys) {
                fillTaskContainer(taskContainer, user, deploy);
            }
        }
        return taskContainer;
    }

    private void fillTaskContainer(List<RedeemTask> taskContainer, UserHoyopass user, RedeemDeploy deploy) {
        String botUserId = user.getBotUserId();
        for (Hoyopass hoyopass : user.getHoyopasses()) {
            fillTaskContainer(taskContainer, botUserId, hoyopass, deploy);
        }
    }

    private void fillTaskContainer(List<RedeemTask> taskContainer,
                                   String botUserId, Hoyopass hoyopass, RedeemDeploy deploy) {
        HoyopassCredentials credentials = hoyopass.getCredentials();
        for (Uid uid : hoyopass.getUids()) {
            taskContainer.add(
                    createRedeemTask(botUserId, credentials, uid, deploy));
        }
    }

    private RedeemTask createRedeemTask(String botUserId,
                                        HoyopassCredentials credentials, Uid uid, RedeemDeploy deploy) {

        return RedeemTask.builder()
                .botUserId(botUserId)
                .credentials(credentials)
                .uid(uid)
                .redeemCode(deploy.getCode())
                .reason(deploy.getReason())
                .build();
    }

    @FunctionalInterface
    public interface RedeemDeployProvider {

        List<RedeemDeploy> provide();
    }

    @FunctionalInterface
    public interface UserProvider {

        List<UserHoyopass> provide();
    }
}
