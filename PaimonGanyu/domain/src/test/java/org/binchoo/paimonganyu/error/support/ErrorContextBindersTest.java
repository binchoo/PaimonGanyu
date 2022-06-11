package org.binchoo.paimonganyu.error.support;

import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
class ErrorContextBindersTest {

    ThrowerAware<?> testException = Mockito.mock(ThrowerAware.class);
    ErrorContextBinder<?> errorContextBinder = Mockito.mock(ErrorContextBinder.class);

    @Test
    void findInstanceFor() {
        when(errorContextBinder.canExplain(testException.getClass())).thenReturn(true);
        ErrorContextBinders.addBinder(errorContextBinder);

        var errorContextBinder = ErrorContextBinders.findInstanceFor(
                (Class<? extends ThrowerAware<Object>>) testException.getClass());

        assertThat(errorContextBinder).isNotNull()
                .isEqualTo(errorContextBinder);
    }

    @Test
    void addNullBinder() {
        assertThrows(NullPointerException.class,
                ()-> ErrorContextBinders.addBinder(null));
    }
}