package org.binchoo.paimonganyu.service.redeem;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.algorithm.BloomFilter;
import org.binchoo.paimonganyu.algorithm.MultiHashable;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 리딤 이력의 존재 여부를 조회하는 {@link RedeemHistoryPort} 구현체입니다.
 * <p> 리딤코드 별 리딤 이력 스냅샷을 따서 상수 크기 자료구조인 {@link BloomFilter}에 저장합니다.
 * <p> SQL 관계 연산을 쓰지 못하는 경우를 상정해 블룸필터를 도입하여 쿼리 수를 줄입니다.
 * <p> 스냅샷을 쓰는 결정은 유효합니다. 코드 리딤은 멱등 연산이므로 {@code FalseNegative} 판정의 영향력이 없습니다.
 * <p> 블룸필터를 쓰는 결정은 유효합니다. {@code FalsePositive}인 경우에만 DB 쿼리를 하면 되는데
 * <p> 'Positive' 판단 확률이 상대적으로 낮기 때문입니다. 자세한 내용은 블룸필터를 참조하십시오.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@Service
public class RedeemBloomFilter implements RedeemHistoryPort {

    public static final int DEFAULT_BLOOMFILTER_SIZE = 1000;

    private final int bloomFilterSize;
    private final UserRedeemCrudPort userRedeemCrud;
    private final Map<RedeemCode, BloomFilter<SearchWord>> bloomFilters;

    @Autowired
    public RedeemBloomFilter(UserRedeemCrudPort userRedeemCrud) {
        this(DEFAULT_BLOOMFILTER_SIZE, userRedeemCrud);
    }

    public RedeemBloomFilter(int bloomFilterSize, UserRedeemCrudPort userRedeemCrud) {
        this.bloomFilterSize = (bloomFilterSize) > 0? bloomFilterSize : DEFAULT_BLOOMFILTER_SIZE;
        this.bloomFilters = new HashMap<>();
        this.userRedeemCrud = userRedeemCrud;
    }

    @Override
    public boolean hasRedeemed(String botUserId, String uid, RedeemCode redeemCode) {
        var targetedItem = finishedUserRedeem(botUserId, uid, redeemCode);
        var searchWord = new SearchWord(targetedItem);
        var bloomFilter = getOrCreateBloomFilter(redeemCode);
        if (bloomFilter.probablyContains(searchWord)) {
            // 아이템 삽입이 보장되지 않으므로 실제로 쿼리를 날려 보아야 한다.
            return userRedeemCrud.existMatches(targetedItem);
        }
        // 아이템 미삽입이 보장되므로 바로 반환한다.
        return false;
    }

    private UserRedeem finishedUserRedeem(String botUserId, String uid, RedeemCode redeemCode) {
        return UserRedeem.builder()
                .botUserId(botUserId)
                .uid(uid)
                .redeemCode(redeemCode)
                .done(true)
                .build();
    }

    private BloomFilter<SearchWord> getOrCreateBloomFilter(RedeemCode key) {
        return bloomFilters.computeIfAbsent(key, this::createBloomFilterOf);
    }

    private BloomFilter<SearchWord> createBloomFilterOf(RedeemCode redeemCode) {
        var bloomFilter = new BloomFilter<SearchWord>(bloomFilterSize);
        userRedeemCrud.findByRedeemCode(redeemCode).stream()
                .map(SearchWord::new)
                .forEach(bloomFilter::insert);
        return bloomFilter;
    }

    /**
     * {@link UserRedeem}을 멀티 해싱하기 위한 클래스.
     * {@link StringBuilder}를 공유하므로 스레드 세이프하지 않음.
     */
    private static final class SearchWord implements MultiHashable {

        private int[] hashes;

        public SearchWord(UserRedeem userRedeem) {
            multiHashing(userRedeem);
        }

        private void multiHashing(UserRedeem userRedeem) {
            this.hashes = new int[] {
                    userRedeem.hashCode(),
                    (userRedeem.getBotUserId().hashCode()
                            + userRedeem.getUid().hashCode()*31
                            + userRedeem.getRedeemCode().hashCode())*37
            };
        }

        @Override
        public int[] getHashes() {
            return this.hashes;
        }

        @Override
        public int hashCode() {
            return this.hashes[0];
        }
    }

    @Override
    public boolean hasNotRedeemed(String botUserId, String uid, RedeemCode redeemCode) {
        return !hasRedeemed(botUserId, uid, redeemCode);
    }

    public int getBloomFilterSize() {
        return this.bloomFilterSize;
    }

    @Override
    public List<UserRedeem> findAll() {
        return userRedeemCrud.findAll();
    }

    @Override
    public List<UserRedeem> findByUser(UserHoyopass user) {
        return userRedeemCrud.findByUser(user);
    }

    @Override
    public List<UserRedeem> findByUser(UserHoyopass user, int limit) {
        return userRedeemCrud.findByUser(user, limit);
    }
}
