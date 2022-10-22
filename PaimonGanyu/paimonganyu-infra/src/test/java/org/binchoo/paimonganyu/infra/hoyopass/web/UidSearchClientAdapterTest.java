package org.binchoo.paimonganyu.infra.hoyopass.web;

import org.binchoo.paimonganyu.hoyoapi.tool.DataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.AccountApi;
import org.binchoo.paimonganyu.hoyoapi.GameRecordApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author jbinchoo
 * @since 2022/06/27
 */
@ExtendWith(MockitoExtension.class)
class UidSearchClientAdapterTest {

    @Mock
    AccountApi accountApi;

    @Mock
    GameRecordApi gameRecordApi;

    @Mock
    DataSwitchConfigurer configurer;

    @InjectMocks
    UidSearchClientAdapter adapter;

    @Test
    void findUids() {
    }
}