package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeExceptionMappings;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.testconfig.hoyoapi.HoyoApiIntegrationConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@SpringJUnitConfig(classes = {HoyoApiIntegrationConfig.class})
@ExtendWith(MockitoExtension.class)
class HoyoResponseInspectionAspectTest {

    @Autowired
    HoyoResponseInspectionAspect hoyoResponseInspectionAspect;

    @MockBean
    HoyoResponse<Object> badHoyoResponse;

    /**
     * <p> RetocdeException's static behavior initializes RetcodeExceptionMappings
     * <p> See {@link RetcodeException}'s static area.
     */
    @BeforeAll
    public static void bootstrapRetcodeMappings() {
        RetcodeException foobar = new RetcodeException();
    }

    @Test
    public void inspectRetcode() throws ClassNotFoundException {
        RetcodeExceptionMappings mappings = RetcodeExceptionMappings.getInstance();
        for (Map.Entry<Integer, Class<RetcodeException>> entry : mappings.entrySet()) {
            testRetcodeHandling(entry.getKey(),entry.getValue());
        }
    }

    private void testRetcodeHandling(int retcode, Class<RetcodeException> expectedExceptionClass) {
        when(badHoyoResponse.getRetcode()).thenReturn(retcode);

        assertThrows(expectedExceptionClass, () -> {
            hoyoResponseInspectionAspect.inspectRetcode(badHoyoResponse);
        });

        Mockito.reset(badHoyoResponse);
        log.info(String.format("retcode %d is handled to throw %s", retcode, expectedExceptionClass));
    }
}
