package org.binchoo.paimonganyu.service.redeem;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.algorithm.BloomFilter;
import org.binchoo.paimonganyu.algorithm.MultiHashable;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryPort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 이 구현체는 각 리딤코드 별로 {@link BloomFilter}를 지연 생성하므로 쿼리 수를 줄입니다. 싱글턴으로 사용되도록 의도되었습니다.
 * <p> 스냅샷을 쓰는 결정은 유효합니다. {@code FalseNegative}는 코드 리딤 요청을 누락시키지 않기 때문입니다.
 * <p> 블룸필터를 쓰는 결정은 유효합니다. {@code FalsePositive}인 경우에만 DB 쿼리를 하면 되는데
 * '했다고 판단'할 확률이 상대적으로 낮기 때문입니다.
 * <p> 블룸필터는 고정된 크기로 기능을 달성합니다. 자세한 내용은 블룸필터를 참조하십시오.
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
        var itemToSearch = new UserRedeem(botUserId, uid, redeemCode, true);
        var searchWord = new SearchWord(itemToSearch);
        var bloomFilter = getOrCreateBloomFilter(redeemCode);
        if (bloomFilter.containsProbably(searchWord)) {
            return userRedeemCrud.existMatches(itemToSearch);
            // 아이템 삽입이 보장되지 않으므로 실제로 쿼리를 날려 보아야 한다.
        } else {
            return false;
            // 아이템 미삽입이 보장되므로 바로 반환한다.
        }
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

        private static final StringBuilder stringBuilder = new StringBuilder();

        private final String botUserId;
        private final String uid;
        private final String code;
        private final boolean isDone;

        public SearchWord(UserRedeem userRedeem) {
            this.botUserId = userRedeem.getBotUserId();
            this.uid = userRedeem.getUid();
            this.code = userRedeem.getRedeemCode().getCode();
            this.isDone = userRedeem.isDone();
        }

        @Override
        public int[] getHashes() {
            var s0 = stringBuilder.append(botUserId).append(uid).toString();
            var s1 = stringBuilder.append(code).append(isDone).toString();
            stringBuilder.setLength(0);
            return new int[] {
                    s0.hashCode(), s0.hashCode() + s1.hashCode(),
            };
        }
    }

    @Override
    public boolean hasNotRedeemed(String botUserId, String uid, RedeemCode redeemCode) {
        return !hasRedeemed(botUserId, uid, redeemCode);
    }

    public int getBloomFilterSize() {
        return this.bloomFilterSize;
    }
}