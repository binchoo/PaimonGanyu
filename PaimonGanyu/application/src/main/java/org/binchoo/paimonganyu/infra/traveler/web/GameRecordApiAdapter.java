package org.binchoo.paimonganyu.infra.traveler.web;

import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.traveler.TravelerStatus;
import org.binchoo.paimonganyu.traveler.driven.GameRecordClientPort;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Component
public class GameRecordApiAdapter implements GameRecordClientPort {

    private final HoyolabGameRecordApi gameRecordApi;
    private Comparator<TravelerStatus> comparator;

    public GameRecordApiAdapter(HoyolabGameRecordApi gameRecordApi) {
        this.gameRecordApi = gameRecordApi;
        this.comparator = defaultComparator();
    }

    @Override
    public Collection<TravelerStatus> getStatusOf(UserHoyopass user, Comparator<TravelerStatus> comparator) {
        PriorityQueue<TravelerStatus> pq = newHeapUsing(comparator);
        for (Hoyopass pass : user.getHoyopasses()) {
            Collection<TravelerStatus> statuses = getStatusOf(pass, comparator);
            pq.addAll(statuses);
        }
        return flatHeap(pq);
    }

    @Override
    public Collection<TravelerStatus> getStatusOf(Hoyopass pass, Comparator<TravelerStatus> comparator) {
        PriorityQueue<TravelerStatus> pq = newHeapUsing(comparator);
        for (Uid uid : pass.getUids())
            pq.add(getStatusOf(uid, pass));
        return flatHeap(pq);
    }

    private PriorityQueue<TravelerStatus> newHeapUsing(Comparator<TravelerStatus> comparator) {
        return new PriorityQueue<>((comparator != null) ? comparator : this.comparator);
    }

    private Collection<TravelerStatus> flatHeap(PriorityQueue<TravelerStatus> pq) {
        List<TravelerStatus> statuses = new ArrayList<>();
        while (pq.size() > 0)
            statuses.add(pq.poll());
        return statuses;
    }

    @Override
    public TravelerStatus getStatusOf(Uid uid, Hoyopass pass) {
        LtuidLtoken ltlt = new LtuidLtoken(pass.getLtuid(), pass.getLtoken());
        String uidString = uid.getUidString();
        String server = uid.getRegion().lowercase();
        var result = gameRecordApi.getDailyNote(ltlt, uidString, server);
        return mapStatus(result, uid);
    }

    private TravelerStatus mapStatus(HoyoResponse<DailyNote> result, Uid uid) {
        DailyNote dailyNote = result.getData();
        assert dailyNote != null;
        return TravelerStatus.builder()
                .currentExpeditionNum(dailyNote.getCurrentExpeditionNum())
                .resinRecoverySeconds(dailyNote.getResinRecoveryTime())
                .currentHomeCoin(dailyNote.getCurrentHomeCoin())
                .currentResin(dailyNote.getCurrentResin())
                .maxHomeCoin(dailyNote.getMaxHomeCoin())
                .server(uid.getRegion().toString())
                .maxResin(dailyNote.getMaxResin())
                .level(uid.getCharacterLevel())
                .name(uid.getCharacterName())
                .lumine(uid.getIsLumine())
                .uid(uid.getUidString())
                .build();
    }

    @Override
    public void setTravelerStatusComparator(Comparator<TravelerStatus> comparator) {
        this.comparator = comparator;
    }

    public Comparator<TravelerStatus> defaultComparator() {
        return Comparator.comparing(TravelerStatus::getLevel)
                .thenComparing(TravelerStatus::getCurrentResin)
                .thenComparing(TravelerStatus::getCurrentExpeditionNum).reversed();
// same-as
//       return Comparator.comparing(TravelerStatus::getLevel, Comparator.reverseOrder())
//                .thenComparing(TravelerStatus::getCurrentResin, Comparator.reverseOrder())
//                .thenComparing(TravelerStatus::getCurrentExpeditionNum);
    }
}
