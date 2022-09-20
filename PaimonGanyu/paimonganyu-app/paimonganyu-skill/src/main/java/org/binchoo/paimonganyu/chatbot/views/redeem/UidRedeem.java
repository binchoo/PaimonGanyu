package org.binchoo.paimonganyu.chatbot.views.redeem;

import lombok.Data;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/09/20
 */
@Data
public class UidRedeem {

    private Uid uid;
    private List<UserRedeem> redeems;
    private int totalCount;
    private int successCount;
    private double successRate;

    public UidRedeem(Uid uid, List<UserRedeem> redeems, int limit) {
        this.uid = uid;
        this.init(redeems, limit);
    }

    private void init(List<UserRedeem> redeems, int limit) {
        int sz = redeems.size();
        this.totalCount = Math.min(sz, limit);
        this.redeems = new ArrayList<>();
        for (int i = 0; i < this.totalCount; i++) {
            UserRedeem redeem = redeems.get(i);
            if (redeem.isDone()) this.successCount++;
            this.redeems.add(redeems.get(i));
        }
        this.successRate = successCount / (double) totalCount;
    }

    public boolean isEmpty() {
        return this.redeems.isEmpty();
    }

    public boolean isSuccessful() {
        return 1.d == successRate;
    }

    public String getRegion() {
        return uid.getRegion().uppercase();
    }

    public int getCharacterLevel() {
        return uid.getCharacterLevel();
    }

    public String getCharacterName() {
        return uid.getCharacterName();
    }

    public String getUidString() {
        return uid.getUidString();
    }

    public boolean isLumine() {
        return uid.getIsLumine();
    }

    public List<UserRedeem> getRedeems() {
        return redeems;
    }

    public static List<UidRedeem> organize(UserHoyopass user, List<UserRedeem> redeems, int limit) {
        Map<String, List<UserRedeem>> uidRedeem = redeems.stream()
                .collect(Collectors.groupingBy(UserRedeem::getUid));

        return uidRedeem.keySet().stream()
                .map(uid-> new UidRedeem(user.findUid(uid).orElse(nonExisting(uid)), uidRedeem.get(uid), limit))
                .collect(Collectors.toList());
    }

    private static Uid nonExisting(String uid) {
        return Uid.builder()
                .uidString(uid)
                .characterName("Deleted uid.")
                .build();
    }
}
