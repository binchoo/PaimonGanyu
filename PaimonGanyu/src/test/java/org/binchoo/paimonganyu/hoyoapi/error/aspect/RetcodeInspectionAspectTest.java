package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.config.TestAopConfig;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.RetcodeExceptionMappings;
import org.binchoo.paimonganyu.hoyoapi.response.HoyoResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(classes = {TestAopConfig.class})
@ExtendWith(MockitoExtension.class)
class RetcodeInspectionAspectTest {

    @Autowired
    RetcodeInspectionAspect retcodeInspectionAspect;

    @MockBean
    HoyoResponse<Object> badHoyoResponse;

    /**
     * RetocdeException's static behavior initializes RetcodeExceptionMappings
     * See {@link RetcodeException}'s static area.
     */
    @BeforeAll
    public static void bootstrapRetcodeMappings() {
        RetcodeException.of(987654321);
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
            retcodeInspectionAspect.inspectRetcode(badHoyoResponse);
        });

        Mockito.reset(badHoyoResponse);
        log.info(String.format("retcode %d is handled to throw %s", retcode, expectedExceptionClass));
    }
}