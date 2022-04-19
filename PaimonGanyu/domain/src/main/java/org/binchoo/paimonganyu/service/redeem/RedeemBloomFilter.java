package org.binchoo.paimonganyu.service.redeem;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.algorithm.BloomFilter;
import org.binchoo.paimonganyu.algorithm.MultiHashable;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryService;
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
public class RedeemBloomFilter implements RedeemHistoryService {

    private static final int DEFAULT_BLOOMFILTER_SIZE = 1000;

    private final int bloomFilterSize;
    private final UserRedeemCrudPort userRedeemCrudPort;
    private final Map<RedeemCode, BloomFilter<UserRedeemSearchWord>> bloomFilters;

    public RedeemBloomFilter(UserRedeemCrudPort userRedeemCrudPort) {
        this(DEFAULT_BLOOMFILTER_SIZE, userRedeemCrudPort);
    }

    public RedeemBloomFilter(int bloomFilterSize, UserRedeemCrudPort userRedeemCrudPort) {
        this.bloomFilterSize = bloomFilterSize;
        this.bloomFilters = new HashMap<>();
        this.userRedeemCrudPort = userRedeemCrudPort;
    }

    @Override
    public boolean hasRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        var historyToSearch = createUserRedeemToSearch(botUserId, ltuid, redeemCode);
        var searchWord = createSearchWord(historyToSearch);
        if (getOrCreateBloomFilter(redeemCode).assumeExists(searchWord)) {
            return userRedeemCrudPort.existMatches(historyToSearch);
            // 아이템 삽입이 보장되지 않으므로 실제로 쿼리를 날려 보아야 한다.
        } else {
            return false;
            // 아이템 미삽입이 보장되므로 바로 반환한다.
        }
    }

    /**
     * 완수 상태를 표상하는 UserRedeemComposite를 만든다.
     * 여기서 반환된 객체와 블룸필터를 대조하여서 대응하는 이력의 존재 여부를 확인할 수 있다.
     */
    private UserRedeem createUserRedeemToSearch(String botUserId, String ltuid, RedeemCode redeemCode) {
        var userRedeem = new UserRedeem(botUserId, ltuid, redeemCode);
        userRedeem.assumeDone();
        return userRedeem;
    }

    private UserRedeemSearchWord createSearchWord(UserRedeem toSearch) {
        return new UserRedeemSearchWord(toSearch);
    }

    private BloomFilter<UserRedeemSearchWord> getOrCreateBloomFilter(RedeemCode key) {
        return bloomFilters.computeIfAbsent(key, this::createBloomFilterOf);
    }

    private BloomFilter<UserRedeemSearchWord> createBloomFilterOf(RedeemCode redeemCode) {
        var bloomFilter = new BloomFilter<UserRedeemSearchWord>(bloomFilterSize);
        userRedeemCrudPort.findByRedeemCode(redeemCode).stream()
                .map(UserRedeemSearchWord::new)
                .forEach(bloomFilter::insert);
        return bloomFilter;
    }

    @Override
    public boolean hasNotRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        return !this.hasRedeemed(botUserId, ltuid, redeemCode);
    }

    /**
     * {@link UserRedeem}을 멀티 해싱하기 위한 클래스.
     * {@link StringBuilder}를 공유하므로 스레드 세이프하지 않음.
     */
    private static final class UserRedeemSearchWord implements MultiHashable {

        private static final StringBuilder stringBuilder = new StringBuilder();

        private final String botUserId;
        private final String ltuid;
        private final String code;
        private final boolean isDone;

        public UserRedeemSearchWord(UserRedeem userRedeem) {
            this.botUserId = userRedeem.getBotUserId();
            this.ltuid = userRedeem.getLtuid();
            this.code = userRedeem.getRedeemCode().getCode();
            this.isDone = userRedeem.isDone();
        }

        @Override
        public int[] getHashes() {
            var s0 = stringBuilder.append(botUserId).append(ltuid).toString();
            var s1 = stringBuilder.append(botUserId).append(ltuid).append(code).append(isDone).toString();
            stringBuilder.setLength(0);
            return new int[] {
                    s0.hashCode(), s0.hashCode() * s1.hashCode(),
            };
        }
    }

    public int getBloomFilterSize() {
        return this.bloomFilterSize;
    }
}