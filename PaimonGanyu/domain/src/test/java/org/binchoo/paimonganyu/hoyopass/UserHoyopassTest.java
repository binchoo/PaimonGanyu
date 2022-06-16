package org.binchoo.paimonganyu.hoyopass;

import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityExceedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils.*;

/**
 * @author : jbinchoo
 * @since : 2022/04/15
 */

@ExtendWith(MockitoExtension.class)
class UserHoyopassTest {

    @Mock
    UidSearchClientPort mockSearchPort;

    private List<Hoyopass> givenNHoyopasses(int n) {
        List<Hoyopass> mockHoyopasses = new ArrayList<>();
        for (int i = 0; i < n; i++)
            mockHoyopasses.add(getMockHoyopass());
        return mockHoyopasses;
    }

    @Test
    void givenOneHoyopass_registerHoyopass_successes() {
        List<Hoyopass> hoyopassList = givenNHoyopasses(1);
        Hoyopass hoyopass = hoyopassList.get(0);

        UserHoyopass userHoyopass = new UserHoyopass();
        userHoyopass.addComplete(hoyopass);

        assertThat(userHoyopass.getHoyopasses().get(0)).isEqualTo(hoyopass);
    }

    @Test
    void givenTwoHoyopasses_registerHoyopass_successes() {
        List<Hoyopass> mockHoyopasses = givenNHoyopasses(2);

        UserHoyopass userHoyopass = new UserHoyopass();
        mockHoyopasses.forEach(userHoyopass::addComplete);

        assertThat(userHoyopass.getSize()).isEqualTo(2);
        IntStream.of(0, 1).forEach(i-> {
            assertThat(userHoyopass.getHoyopasses().get(i))
                    .isEqualTo(mockHoyopasses.get(i));
        });
    }

    @Test
    void whenTwoHoyopassesAlreadyRegistered_registerHoyopass_fails() {
        List<Hoyopass> hoyopassList = givenNHoyopasses(2);
        Hoyopass additionalHoyopass = getMockHoyopass();

        UserHoyopass userHoyopass = new UserHoyopass();
        hoyopassList.forEach(userHoyopass::addComplete);

        assertThrows(QuantityExceedException.class, ()->
                userHoyopass.addComplete(additionalHoyopass));
    }

    @Test
    void givenInvalidHoyopass_registerHoyopass_fails() {
        HoyopassCredentials invalidCred = new HoyopassCredentials("foo", "bar", "foobar");

        doThrow(RuntimeException.class).when(mockSearchPort).findUids(any());

        UserHoyopass userHoyopass = new UserHoyopass();
        assertThrows(InactiveStateException.class, ()->
                userHoyopass.addIncomplete(invalidCred, mockSearchPort));
    }

    @Test
    void givenValidHoyopass_registerHoyopass_successes() {
        int uidCount = 3;
        List<Uid> mockUidList = new ArrayList<>();
        for(int i = 0; i < uidCount; i ++) mockUidList.add(getMockUid());
        when(mockSearchPort.findUids(any())).thenReturn(mockUidList);

        UserHoyopass userHoyopass = new UserHoyopass();
        userHoyopass.addIncomplete(
                new HoyopassCredentials("foo", "bar", "foobar"), mockSearchPort);

        assertThat(userHoyopass.getSize()).isEqualTo(1);
        assertThat(userHoyopass.listUids()).isEqualTo(mockUidList);
    }

    @Test
    void givenDuplicateHoyopass_registerHoyopass_fails() {
        Hoyopass mockHoyopass = getMockHoyopass();

        UserHoyopass userHoyopass = new UserHoyopass();
        userHoyopass.addComplete(mockHoyopass);

        assertThrows(DuplicationException.class, ()->
                userHoyopass.addComplete(mockHoyopass));
    }

    @Test
    void testConstructor_withBotUserId() {
        String botUserId = "foobar";

        UserHoyopass userHoyopass = new UserHoyopass("foobar");

        assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
    }

    @Test
    void testConstructor_withBotUserId_withHoyopassList() {
        String botUserId = "foobar";
        int count = 2;
        List<Hoyopass> hoyopassList = givenNHoyopasses(count);

        UserHoyopass userHoyopass = new UserHoyopass("foobar", hoyopassList);

        assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
        assertThat(userHoyopass.getSize()).isEqualTo(count);
        for(int i = 0; i < count; i++)
            assertThat(userHoyopass.getHoyopasses().get(i))
                    .isEqualTo(hoyopassList.get(i));
    }

    @Test
    void testListUids() {
        int count = 2;
        List<Hoyopass> hoyopassList = givenNHoyopasses(count);

        UserHoyopass userHoyopass = new UserHoyopass("foobar", hoyopassList);
        List<Uid> userUids = userHoyopass.listUids();

        userHoyopass.getHoyopasses().forEach(hoyopass-> {
            verifyUidListContainsAllUids(userUids, hoyopass);
        });
    }

    @Test
    void testListUids_withIndex() {
        int count = 2;
        List<Hoyopass> hoyopassList = givenNHoyopasses(count);

        UserHoyopass userHoyopass = new UserHoyopass("foobar", hoyopassList);

        for (int i = 0; i < count; i++) {
            List<Uid> userUids = userHoyopass.listUids(0);
            verifyUidListContainsAllUids(userUids, userHoyopass.getHoyopasses().get(0));
        }
    }

    private void verifyUidListContainsAllUids(List<Uid> userUids, Hoyopass hoyopass) {
        hoyopass.getUids().forEach(uid-> assertThat(uid).isIn(userUids));
    }

    @Test
    void testListUids_withOutBoundIndex() {
        List<Hoyopass> hoyopassList = givenNHoyopasses(2);
        UserHoyopass userHoyopass = new UserHoyopass("foobar", hoyopassList);

        List<Uid> userUids = userHoyopass.listUids(-100);
        assertThat(userUids).isEmpty();

        userUids = userHoyopass.listUids(100);
        assertThat(userUids).isEmpty();
    }

    @Test
    void testDeleteAt() {
        List<Hoyopass> hoyopassList = givenNHoyopasses(2);
        UserHoyopass userHoyopass = new UserHoyopass("foobar", hoyopassList);

        Hoyopass hoyopass = userHoyopass.deleteAt(0);

        assertThat(hoyopass).isEqualTo(hoyopassList.get(0));
        assertThat(userHoyopass.getSize()).isEqualTo(1);

        hoyopass = userHoyopass.deleteAt(0);

        assertThat(hoyopass).isEqualTo(hoyopassList.get(1));
        assertThat(userHoyopass.getSize()).isZero();
    }


    @Test
    void testDeleteAt_withOutBoundIndex() {
        int minusIndex = -100;
        int hugeIndex = 1000;
        List<Hoyopass> hoyopassList = givenNHoyopasses(2);

        UserHoyopass userHoyopass = new UserHoyopass("foobar", hoyopassList);
        assertThrows(IndexOutOfBoundsException.class, ()->
                userHoyopass.deleteAt(minusIndex));
        assertThrows(IndexOutOfBoundsException.class, ()->
                userHoyopass.deleteAt(hugeIndex));
    }

    @Test
    void givenSameBotUserIdAndHoyopass_twoUserHoyopasses_areEqual() {
        String sameBotUserId = "foobar";
        Hoyopass sameHoyopass = getMockHoyopass();
        UserHoyopass userHoyopass1 = new UserHoyopass(sameBotUserId,
                Collections.singletonList(sameHoyopass));
        UserHoyopass userHoyopass2 = new UserHoyopass(sameBotUserId,
                Collections.singletonList(sameHoyopass));

        assertThat(userHoyopass1).isEqualTo(userHoyopass2);
    }

    @Test
    void givenSameBotUserIdAndHoyopassList_twoUserHoyopasses_areEqual() {
        String sameBotUserId = "foobar";
        List<Hoyopass> sameHoyopassList = givenNHoyopasses(2);
        UserHoyopass userHoyopass1 = new UserHoyopass(sameBotUserId, sameHoyopassList);
        UserHoyopass userHoyopass2 = new UserHoyopass(sameBotUserId, sameHoyopassList);

        assertThat(userHoyopass1).isEqualTo(userHoyopass2);
    }

    @Test
    void givenDifferentHoyopasses_twoUserHoyopasses_areNotEqual() {
        String sameBotUserId = "foobar";
        Hoyopass aHoyopass = getMockHoyopass();
        Hoyopass otherHoyopass = getMockHoyopass();
        UserHoyopass userHoyopass1 = new UserHoyopass(sameBotUserId,
                Collections.singletonList(aHoyopass));
        UserHoyopass userHoyopass2 = new UserHoyopass(sameBotUserId,
                Collections.singletonList(otherHoyopass));

        assertThat(userHoyopass1).isNotEqualTo(userHoyopass2);
    }

    @Test
    void givenDifferentBotUserId_twoUserHoyopasses_areNotEqual() {
        String aBotUserId = "foobar";
        String otherBotUserId = "barfoo";
        List<Hoyopass> sameHoyopassList = givenNHoyopasses(2);
        UserHoyopass userHoyopass1 = new UserHoyopass(aBotUserId, sameHoyopassList);
        UserHoyopass userHoyopass2 = new UserHoyopass(otherBotUserId, sameHoyopassList);

        assertThat(userHoyopass1).isNotEqualTo(userHoyopass2);
    }

    @Test
    void testToString() {
        String botUserId = "foobar";
        List<Hoyopass> hoyopassList = givenNHoyopasses(2);
        UserHoyopass userHoyopass = new UserHoyopass(botUserId, hoyopassList);

        String toStringResult = userHoyopass.toString();

        assertThat(toStringResult)
                .contains(botUserId)
                .contains(userHoyopass.getHoyopasses().get(0).toString())
                .contains(userHoyopass.getHoyopasses().get(1).toString());

        System.out.println(toStringResult);
    }
}