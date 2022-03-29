package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.binchoo.paimonganyu.config.HoyoApiAspectJConfig;
import org.binchoo.paimonganyu.hoyoapi.response.HoyoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {HoyoApiAspectJConfig.class})
@ExtendWith(MockitoExtension.class)
class DataInspectionAspectTest {

    @Autowired
    DataInspectionAspect dataInspectionAspect;

    @MockBean
    HoyoResponse<Object> badHoyoResponse;

    @Test
    public void inspectData() throws ClassNotFoundException {
        when(badHoyoResponse.getData()).thenReturn(null);

        assertThrows(NullPointerException.class, ()-> {
            dataInspectionAspect.inspectResponseData(badHoyoResponse);
        });
    }
}