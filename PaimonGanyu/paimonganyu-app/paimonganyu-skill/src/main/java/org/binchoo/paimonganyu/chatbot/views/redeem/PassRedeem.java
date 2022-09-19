package org.binchoo.paimonganyu.chatbot.views.redeem;

import lombok.Data;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/09/20
 */
@Data
public class PassRedeem {

    private Hoyopass hoyopass;
    private List<RedeemCodeStatistic> codeStatistics;

    public PassRedeem(Hoyopass pass, Map<String, List<UserRedeem>> codeRedeem) {
        this.hoyopass = pass;
        for (Map.Entry<String, List<UserRedeem>> entry : codeRedeem.entrySet()) {
            if (this.codeStatistics == null)
                this.codeStatistics = new ArrayList<>();
            this.codeStatistics.add(new RedeemCodeStatistic(entry.getKey(), entry.getValue()));
        }
    }

    public List<RedeemCodeStatistic> getStatistics() {
        return Collections.unmodifiableList(codeStatistics);
    }

    public boolean isEmpty() {
        return this.codeStatistics.isEmpty();
    }

    public static class RedeemCodeStatistic {

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-mm-dd HH:ss");

        private String code;
        private String reason;
        private LocalDateTime date;
        private int successCount;
        private int total;

        public RedeemCodeStatistic(String code, List<UserRedeem> redeems) {
            this.code = code;
            this.reason = redeems.stream().findAny().map(UserRedeem::getReason).orElse("");
            this.date = redeems.stream().findAny().map(UserRedeem::getDate).orElse(LocalDateTime.MAX);
            this.successCount = (int) redeems.stream().filter(UserRedeem::isDone).count();
            this.total = redeems.size();
        }

        public String getCode() {
            return this.code;
        }

        public String getReason() {
            return this.reason;
        }

        public LocalDateTime getDate() {
            return this.date;
        }

        public String getDateString() {
            return this.date.format(RedeemCodeStatistic.formatter);
        }

        public double getSuccessRate() {
            return this.successCount / (double) this.total;
        }

        public int getSuccessCount() {
            return this.successCount;
        }

        public int getFailCount() {
            return this.total - this.successCount;
        }

        public int getTotalCount() {
            return this.total;
        }
    }

    public static List<PassRedeem> organize(UserHoyopass user, List<UserRedeem> userRedeems, int limit) {
        List<PassRedeem> result = new ArrayList<>();
        Map<String, List<UserRedeem>> uidRedeem = userRedeems.stream()
                .collect(Collectors.groupingBy(UserRedeem::getUid));

        for (Hoyopass pass : user.getHoyopasses()) {
            List<UserRedeem> passRedeem = new ArrayList<>();
            for (Uid uid : pass.getUids()) {
                String uidString = uid.getUidString();
                if (uidRedeem.containsKey(uidString)) {
                    passRedeem.addAll(uidRedeem.get(uidString));
                }
            }
            if (passRedeem.size() > 0) {
                result.add(new PassRedeem(pass, passRedeem.stream().collect(
                        Collectors.groupingBy(it-> it.getRedeemCode().getCode()))));
            }
        }
        return result.stream().limit(limit)
                .collect(Collectors.toList());
    }
}
