package org.binchoo.paimonganyu.infra.traveler.web;

import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.DataNotPublicError;
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
        PriorityQueue<TravelerStatus> heap = newHeapUsing(comparator);
        for (Hoyopass pass : user.listHoyopasses()) {
            Collection<TravelerStatus> statuses = getStatusOf(pass, comparator);
            heap.addAll(statuses);
        }
        return flatHeap(heap);
    }

    @Override
    public Collection<TravelerStatus> getStatusOf(Hoyopass pass, Comparator<TravelerStatus> comparator) {
        PriorityQueue<TravelerStatus> heap = newHeapUsing(comparator);
        for (Uid uid : pass.getUids()) {
            Optional<TravelerStatus> status = getStatusOf(uid, pass);
            heap.add(status
                    .orElse(TravelerStatus.erroneous()));
        }
        return flatHeap(heap);
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
    public Optional<TravelerStatus> getStatusOf(Uid uid, Hoyopass pass) {
        LtuidLtoken ltlt = new LtuidLtoken(pass.getLtuid(), pass.getLtoken());
        String uidString = uid.getUidString();
        String server = uid.getRegion().lowercase();
        return fetchTravelerStatus(uid, ltlt, uidString, server);

    }

    private Optional<TravelerStatus> fetchTravelerStatus(Uid uid, LtuidLtoken ltlt, String uidString, String server) {
        TravelerStatus status = null;
        try {
            var result = gameRecordApi.getDailyNote(ltlt, uidString, server);
            status = buildTravelerStatus(result, uid);
        } catch (DataNotPublicError e) {
        }
        return Optional.ofNullable(status);
    }

    private TravelerStatus buildTravelerStatus(HoyoResponse<DailyNote> result, Uid uid) {
        TravelerStatus status = null;
        if (result != null) {
            DailyNote note = result.getData();
            if (note != null)
                status = buildTravelerStatus(uid, result.getData());
        }
        return status;
    }

    private TravelerStatus buildTravelerStatus(Uid uid, DailyNote dailyNote) {
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
    }
}
