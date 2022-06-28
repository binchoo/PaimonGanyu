package org.binchoo.paimonganyu.infra.hoyopass.web;

import org.binchoo.paimonganyu.hoyoapi.DataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

/**
 * @author jbinchoo
 * @since 2022/06/27
 */
@ExtendWith(MockitoExtension.class)
class UidSearchClientAdapterTest {

    @Mock
    HoyolabAccountApi accountApi;

    @Mock
    HoyolabGameRecordApi gameRecordApi;

    @Mock
    DataSwitchConfigurer configurer;

    @InjectMocks
    UidSearchClientAdapter adapter;

    @Test
    void findUids() {
    }
}