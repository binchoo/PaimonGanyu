package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeExceptionMappings;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
class HoyoResponseInspectionAspectTest {

    HoyoResponseInspectionAspect hoyoResponseInspectionAspect = new HoyoResponseInspectionAspect();

    @Mock
    HoyoResponse<Object> badHoyoResponse;

    /**
     * <p> RetocdeException's static behavior initializes RetcodeExceptionMappings
     * <p> See {@link RetcodeException}'s static area.
     */
    @BeforeAll
    public static void bootstrapRetcodeMappings() {
        RetcodeException foobar = new RetcodeException();
    }

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inspectRetcode() throws ClassNotFoundException {
        RetcodeExceptionMappings mappings = RetcodeExceptionMappings.getInstance();
        for (Map.Entry<Integer, Class<RetcodeException>> entry : mappings.entrySet()) {
            testRetcodeHandling(entry.getKey(),entry.getValue());
        }
    }

    private void testRetcodeHandling(int retcode, Class<RetcodeException> expectedExceptionClass) {
        when(badHoyoResponse.getRetcode()).thenReturn(retcode);

        assertThrows(expectedExceptionClass, () -> {
            hoyoResponseInspectionAspect.inspectResponse(badHoyoResponse);
        });

        Mockito.reset(badHoyoResponse);
        log.info(String.format("retcode %d is handled to throw %s", retcode, expectedExceptionClass));
    }

    @Test
    void inspectData() throws ClassNotFoundException {
        when(badHoyoResponse.getRetcode()).thenReturn(0);
        when(badHoyoResponse.getData()).thenReturn(null);

        assertThrows(NullPointerException.class, ()-> {
           hoyoResponseInspectionAspect.inspectResponse(badHoyoResponse);
        });
    }
}
