package org.binchoo.paimonganyu.chatbot.views.error;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@Lazy
@Component
public class DefaultErrorExplain implements ErrorExplain {

    @Override
    public String getExplanation() {
        return "알 수 없는 오류가 발생했습니다.";
    }
    @Override
    public Collection<FallbackMethod> getFallbacks() {
        return List.of(FallbackMethods.Home);
    }
}
