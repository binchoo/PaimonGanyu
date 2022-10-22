package org.binchoo.paimonganyu.infra.traveler.web;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.GameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.traveler.TravelerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class GameRecordApiAdapterTest {

    @InjectMocks
    GameRecordApiAdapter apiAdapter;

    @Mock
    Hoyopass hoyopass;

    @Mock
    GameRecordApi gameRecordApi;

    @DisplayName("비교자에 맞게 정렬되어 TravelerStatus들을 반환한다.")
    @ArgumentsSource(ComparatorProvider.class)
    @ParameterizedTest
    void testSpecificComparator(Comparator<TravelerStatus> comparator) {
        var result = apiAdapter.getStatusOf(testHoyopass(), comparator);

        var manuallySorted = new ArrayList<>(result);
        manuallySorted.sort(comparator);

        assertThat(result).containsExactlyElementsOf(manuallySorted);
        log.debug(result.toString());
    }

    @BeforeEach
    void mockApi() {
        Hoyopass pass = testHoyopass();
        for (int i = 0; i < pass.getUids().size(); i++) {
            when(gameRecordApi.getDailyNote(any(), eq(String.valueOf(i)), any()))
                    .thenReturn(testDailyNote());
        }
    }

    private Hoyopass testHoyopass() {
        when(hoyopass.getLtuid()).thenReturn("ltuid");
        when(hoyopass.getLtoken()).thenReturn("ltoken");
        when(hoyopass.getUids()).thenReturn(testUids());
        return hoyopass;
    }

    private List<Uid> testUids() {
        Random rand = new Random();
        return IntStream.range(0, 4).mapToObj(i-> Uid.builder()
                .characterLevel(rand.nextInt())
                .uidString(String.valueOf(i))
                .isLumine(false)
                .region(Region.values()[i]).build()
        ).collect(Collectors.toList());
    }

    private HoyoResponse<DailyNote> testDailyNote() {
        HoyoResponse<DailyNote> res = new HoyoResponse<>();
        DailyNote note = new DailyNote();
        Random rand = new Random();
        note.setCurrentResin(rand.nextInt());
        note.setCurrentHomeCoin(rand.nextInt());
        res.setData(note);
        return res;
    }

    private static class ComparatorProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            var comp = new Comparator[] {
                    Comparator.comparing(TravelerStatus::getCurrentHomeCoin)
                            .thenComparing(TravelerStatus::getCurrentResin)
                            .thenComparing(TravelerStatus::getLevel),
                    Comparator.comparing(TravelerStatus::getCurrentHomeCoin).reversed()
                            .thenComparing(TravelerStatus::getCurrentResin)
                            .thenComparing(TravelerStatus::getLevel),
                    Comparator.comparing(TravelerStatus::getCurrentHomeCoin)
                            .thenComparing(TravelerStatus::getCurrentResin).reversed()
                            .thenComparing(TravelerStatus::getLevel),
                    Comparator.comparing(TravelerStatus::getCurrentHomeCoin)
                            .thenComparing(TravelerStatus::getCurrentResin)
                            .thenComparing(TravelerStatus::getLevel).reversed()
            };
            return Arrays.stream(comp).map(Arguments::of);
        }
    }
}