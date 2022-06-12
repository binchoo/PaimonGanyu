package org.binchoo.paimonganyu.chatbot.view;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.error.ErrorContext;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@RequiredArgsConstructor
@Component
public class ErrorResponseTemplate {

    private final QuickReplies quickReplies;

    public SkillResponse build(ErrorContext errorContext) {
        return SkillResponse.builder()
                .template(skillTemplate(errorContext))
                .build();
    }

    private SkillTemplate skillTemplate(ErrorContext errorContext) {
        var text = new SimpleTextView(new SimpleText(errorContext.getExplanation()));

        var quickReplies = errorContext.getFallbacks()
                .stream().map(this.quickReplies::findById)
                .collect(Collectors.toList());

        return SkillTemplate.builder()
                .addOutput(text)
                .quickReplies(quickReplies)
                .build();
    }
}
