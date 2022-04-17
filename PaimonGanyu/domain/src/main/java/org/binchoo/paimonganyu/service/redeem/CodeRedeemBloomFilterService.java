package org.binchoo.paimonganyu.service.redeem;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.algorithm.BloomFilter;
import org.binchoo.paimonganyu.algorithm.MultiHashable;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserCodeRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserCodeRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.CodeRedeemHistoryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p> 블룸필터를 사용하는 {@link CodeRedeemHistoryService} 구현체.
 * <p> 코드 리딤 이력의 존재 여부를 확인할 때 DB에만 의존할 경우, 최대 (유저 수)x(코드 수) 만큼의 쿼리가 발생하므로 좋지 않습니다.
 * <p> 이 구현체는 DB 접근을 1회의 스냅샷을 얻는 때로 한정하고 이후는 블룸필터와 스냅샷만을 비교합니다.
 * <p> 스냅샷을 쓰는 결정은 유효합니다. FalseNegative(했는데 안했다고 판단)는 코드 리딤 요청을 누락시키지 않기 때문입니다.
 * <p> 블룸필터를 쓰는 결정은 유효합니다. FalsePositive(안했는데 했다고 판단)인 경우에만 스냅샷을 확인하면 되는데
 * '했다고 판단'할 확률이 상대적으로 낮기 때문입니다. 자세한 내용은 블룸필터를 참조하십시오.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@Service
public class CodeRedeemBloomFilterService implements CodeRedeemHistoryService {

    private static final int DEFAULT_BLOOMFILTER_SIZE = 500;

    private final UserCodeRedeemCrudPort repository;
    private final int bloomFilterSize;

    private BloomFilter<UserCodeRedeemComposite> bloomFilter;
    private List<UserCodeRedeem> snapshot;

    public CodeRedeemBloomFilterService(UserCodeRedeemCrudPort repository) {
        this(DEFAULT_BLOOMFILTER_SIZE, repository);
    }

    public CodeRedeemBloomFilterService(int bloomFilterSize, UserCodeRedeemCrudPort repository) {
        this.bloomFilterSize = bloomFilterSize;
        this.repository = repository;
        this.takeSnapshot();
        this.initBloomFilter();
    }

    private void takeSnapshot() {
        this.snapshot = Collections.unmodifiableList(repository.findAll());
    }

    private void initBloomFilter() {
        bloomFilter = new BloomFilter<>(bloomFilterSize);
        snapshot.stream().map(UserCodeRedeemComposite::new)
                .forEach(composite-> bloomFilter.insert(composite));
    }

    @Override
    public boolean hasRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        UserCodeRedeem userCodeRedeem = new UserCodeRedeem(botUserId, ltuid, redeemCode);
        return hasTrulyRedeemed(userCodeRedeem);
    }

    private boolean hasTrulyRedeemed(UserCodeRedeem userCodeRedeem) {
        UserCodeRedeemComposite composite = new UserCodeRedeemComposite(userCodeRedeem);
        if (bloomFilter.contains(composite)) {
            // 아이템 삽입이 보장되지 않으므로 실제로 조회해보아야 한다.
            return repository.existsRedeemHistory(userCodeRedeem);
        } else {
            // 아이템 미삽입이 보장되므로 바로 반환한다.
            return false;
        }
    }

    @Override
    public boolean hasNotRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        return !this.hasRedeemed(botUserId, ltuid, redeemCode);
    }

    private static final class UserCodeRedeemComposite implements MultiHashable {

        private final StringBuilder stringBuilder = new StringBuilder();
        private int[] hashes = null;

        public UserCodeRedeemComposite(UserCodeRedeem userCodeRedeem) {
            calcHashes(userCodeRedeem);
        }

        private void calcHashes(UserCodeRedeem userCodeRedeem) {
            String botUserId = userCodeRedeem.getBotUserId();
            String ltuid = userCodeRedeem.getLtuid();
            String redeemCode = userCodeRedeem.getRedeemCode().getCode();
            int h0 = userCodeRedeem.hashCode();
            String s1 = stringBuilder.append(botUserId).append(ltuid).append(redeemCode).toString();
            String s2 = stringBuilder.reverse().toString();
            String s3 = stringBuilder.append(h0).toString();
            this.setHashes(h0, s1, s2, s3);
        }

        private void setHashes(int h0, String s1, String s2, String s3) {
            this.hashes =  new int[] {h0, h0 * s1.hashCode(), s2.hashCode(), h0 - s3.hashCode()};
        }

        @Override
        public int[] getHashes() {
            assert hashes != null;
            return hashes;
        }
    }

    public int getBloomFilterSize() {
        return this.bloomFilterSize;
    }
}