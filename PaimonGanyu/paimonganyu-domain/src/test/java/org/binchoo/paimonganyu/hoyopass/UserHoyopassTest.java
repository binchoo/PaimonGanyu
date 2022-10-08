package org.binchoo.paimonganyu.hoyopass;

import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.ImmortalUidException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.ManyHoyopassException;
import org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils.mockHoyopass;
import static org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils.mockUid;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author : jbinchoo
 * @since : 2022/04/15
 */
@ExtendWith(MockitoExtension.class)
class UserHoyopassTest {

    @Mock
    UidSearchClientPort mockUidSearchClient;

    @Test
    void givenOneHoyopass_addComplete_successes() {
        List<Hoyopass> passes = hoyopasses(1);
        Hoyopass hoyopass = passes.get(0);

        UserHoyopass user = new UserHoyopass();
        user.addComplete(hoyopass);

        assertThat(user.getHoyopassAt(0)).isEqualTo(hoyopass);
    }

    @Test
    void givenTwoHoyopasses_addComplete_successes() {
        int createCount = 2;
        List<Hoyopass> passes = hoyopasses(createCount);

        UserHoyopass user = new UserHoyopass();
        passes.forEach(user::addComplete);

        assertThat(user.size()).isEqualTo(createCount);
        IntStream.range(0, createCount).forEach(i->
            assertThat(user.getHoyopassAt(i)).isEqualTo(passes.get(i)));
    }

    @Test
    void whenContainingTwoHoyopasses_additionalAddComplete_fails() {
        int createCount = 2;
        List<Hoyopass> passes = hoyopasses(createCount);
        Hoyopass third = mockHoyopass();

        UserHoyopass user = new UserHoyopass();
        passes.forEach(user::addComplete);

        assertThrows(ManyHoyopassException.class, ()->
                user.addComplete(third));
    }

    @Test
    void givenInvalidCredentials_addIncomplete_fails() {
        doThrow(IllegalArgumentException.class)
                .when(mockUidSearchClient).findUids(any());

        UserHoyopass user = new UserHoyopass();
        assertThrows(InactiveStateException.class, ()->
                user.addIncomplete(anyCredentials(), mockUidSearchClient));
    }

    @Test
    void givenValidCredentials_addIncomplete_successes() {
        int uidCount = 3;
        List<Uid> uids = uids(uidCount);

        when(mockUidSearchClient.findUids(any())).thenReturn(uids);

        UserHoyopass user = new UserHoyopass();
        user.addIncomplete(anyCredentials(), mockUidSearchClient);

        assertThat(user.size()).isEqualTo(1);
        assertThat(user.listUids()).isEqualTo(uids);
    }

    private HoyopassCredentials anyCredentials() {
        return new HoyopassCredentials("foo", "bar", "foobar");
    }

    @Test
    void givenDuplicateHoyopass_addComplete_fails() {
        Hoyopass pass = mockHoyopass();

        UserHoyopass user = new UserHoyopass();
        user.addComplete(pass);

        assertThrows(DuplicationException.class, ()->
                user.addComplete(pass));
    }

    @Test
    void construction_withBotUserId_successes() {
        String botUserId = "foobar";

        UserHoyopass user = new UserHoyopass("foobar");

        assertThat(user.getBotUserId()).isEqualTo(botUserId);
    }

    @Test
    void construction_withHoyopasses_successes() {
        List<Hoyopass> passes = hoyopasses(2);
        String botUserId = "foobar";

        UserHoyopass user = new UserHoyopass("foobar", passes);

        assertThat(user.size()).isEqualTo(2);
        assertThat(user.getBotUserId()).isEqualTo(botUserId);
        IntStream.range(0, 2).forEach(i->
                assertThat(user.listHoyopasses().get(i)).isEqualTo(passes.get(i)));
    }

    @Test
    void listUids_successes() {
        List<Hoyopass> passes = hoyopasses(2);

        UserHoyopass user = new UserHoyopass("foobar", passes);
        List<Uid> userUids = user.listUids();

        user.listHoyopasses().forEach(hoyopass-> {
            uidListEquals(userUids, hoyopass);
        });
    }

    @Test
    void listUides_withHoypassIndexing_successes() {
        List<Hoyopass> passes = hoyopasses(2);

        UserHoyopass user = new UserHoyopass("foobar", passes);

        IntStream.range(0, 2).forEach(i-> {
            List<Uid> uids = user.listUidsAt(i);
            uidListEquals(uids, user.getHoyopassAt(i));
        });
    }

    private void uidListEquals(List<Uid> uids, Hoyopass hoyopass) {
        hoyopass.getUids().forEach(uid-> assertThat(uid).isIn(uids));
    }

    @Test
    void listUids_withInvalidIndex_returnsEmptyList() {
        List<Hoyopass> passes = hoyopasses(2);
        UserHoyopass user = new UserHoyopass("foobar", passes);

        List<Uid> userUids = user.listUidsAt(-100);
        assertThat(userUids).isEmpty();

        userUids = user.listUidsAt(100);
        assertThat(userUids).isEmpty();
    }

    @Test
    void deleteAt_successes() {
        List<Hoyopass> passes = hoyopasses(2);
        UserHoyopass user = new UserHoyopass("foobar", passes);

        Hoyopass hoyopass = user.deleteAt(0);

        assertThat(hoyopass).isEqualTo(passes.get(0));
        assertThat(user.size()).isEqualTo(1);

        hoyopass = user.deleteAt(0);

        assertThat(hoyopass).isEqualTo(passes.get(1));
        assertThat(user.size()).isZero();
    }


    @Test
    void deleteAt_withInvalidIndex_throwsIndexOutOfBoundsException() {
        int minusIndex = -100;
        int hugeIndex = 1000;
        List<Hoyopass> passes = hoyopasses(2);

        UserHoyopass user = new UserHoyopass("foobar", passes);
        assertThrows(IndexOutOfBoundsException.class, ()->
                user.deleteAt(minusIndex));
        assertThrows(IndexOutOfBoundsException.class, ()->
                user.deleteAt(hugeIndex));
    }

    @Test
    void givenSameBotUserIdAndHoyopass_twoUsersAreEqual() {
        String botUserId = "foobar";
        Hoyopass pass = mockHoyopass();

        UserHoyopass user1 = new UserHoyopass(botUserId,
                Collections.singletonList(pass));

        UserHoyopass user2 = new UserHoyopass(botUserId,
                Collections.singletonList(pass));

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void givenSameBotUserIdAndHoyopasses_twoUsersAreEqual() {
        String botUserId = "foobar";
        List<Hoyopass> passes = hoyopasses(2);

        UserHoyopass user1 = new UserHoyopass(botUserId, passes);
        UserHoyopass user2 = new UserHoyopass(botUserId, passes);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void givenDifferentHoyopasses_twoUsersAreNotEqual() {
        String botUserId = "foobar";
        Hoyopass pass1 = mockHoyopass(), pass2 = mockHoyopass();

        UserHoyopass user1 = new UserHoyopass(botUserId,
                Collections.singletonList(pass1));

        UserHoyopass user2 = new UserHoyopass(botUserId,
                Collections.singletonList(pass2));

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void givenDifferentBotUserId_twoUserHoyopasses_areNotEqual() {
        String botUserId1 = "foobar", botUserId2 = "barfoo";
        List<Hoyopass> passes = hoyopasses(2);

        UserHoyopass user1 = new UserHoyopass(botUserId1, passes);
        UserHoyopass user2 = new UserHoyopass(botUserId2, passes);

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void toString_contains_stringsOfHoyopasses() {
        String botUserId = "foobar";
        List<Hoyopass> passes = hoyopasses(2);
        UserHoyopass user = new UserHoyopass(botUserId, passes);

        String userString = user.toString();
        assertThat(userString)
                .contains(botUserId)
                .contains(user.getHoyopassAt(0).toString())
                .contains(user.getHoyopassAt(1).toString());

        System.out.println(userString);
    }

    @Test
    void givenExistingUids_removeUid_successes() {
        String botUserId = "foobar";
        List<Hoyopass> passes = hoyopasses(2);
        UserHoyopass user = new UserHoyopass(botUserId, passes);

        Hoyopass passForDelete = passes.get(0);
        List<Uid> deletedUids = new ArrayList<>();
        for (int i = 0; i < passForDelete.size() - 1; i++) {
            Uid uidForDelete = passForDelete.getUids().get(0);
            deletedUids.add(user.deleteUid(uidForDelete.getUidString()));
        }

        for (Uid uid : deletedUids) {
            assertThat(user.findUid(uid.getUidString())).isNotPresent();
        }
        assertThat(user.size()).isEqualTo(2);
    }

    @Test
    void givenUnkownUids_removeUid_throwsException() {
        String botUserId = "foobar";
        List<Hoyopass> passes = hoyopasses(2);

        UserHoyopass user = new UserHoyopass(botUserId, passes);

        assertThrows(ImmortalUidException.class, ()-> user.deleteUid("unknown-uid"));
    }

    @Test
    void givenHoyopassWithSingleUid_removeUid_throwsException() {
        String botUserId = "foobar";
        UserHoyopass user = new UserHoyopass(botUserId, List.of(HoyopassMockUtils.mockHoyopass(1)));

        String uidString = user.listUids().get(0).getUidString();

        assertThrows(ImmortalUidException.class, ()-> user.deleteUid(uidString));
    }

    private List<Hoyopass> hoyopasses(int n) {
        return IntStream.range(0, n)
                .mapToObj(i-> mockHoyopass())
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Uid> uids(int uidCount) {
        return IntStream.range(0, uidCount)
                .mapToObj(i -> mockUid())
                .collect(Collectors.toList());
    }
}
