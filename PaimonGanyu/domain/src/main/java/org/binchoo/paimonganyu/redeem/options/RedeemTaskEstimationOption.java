package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;

import java.util.List;
import java.util.Objects;

/**
 * 코드 리딤 태스크를 생성하는 전략입니다.
 * 코드 리딤 대상인 유저와 리딤 코드를 반환해야 합니다.
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
     * @return 코드 리딤 대상 코드들
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

    @FunctionalInterface
    public interface CodeProvider {

        List<RedeemCode> provide();
    }

    @FunctionalInterface
    public interface UserProvider {

        List<UserHoyopass> provide();
    }
}
