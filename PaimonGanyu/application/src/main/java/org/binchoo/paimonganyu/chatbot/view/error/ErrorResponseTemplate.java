package org.binchoo.paimonganyu.chatbot.view.error;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.view.QuickReplies;
import org.binchoo.paimonganyu.chatbot.view.ResponseTemplate;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.springframework.context.annotation.Lazy;
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

    public SkillResponse build(ErrorExplain errorExplain) {
        return SkillResponse.builder()
                .template(skillTemplate(errorExplain))
                .build();
    }

    private SkillTemplate skillTemplate(ErrorExplain errorExplain) {
        var text = new SimpleTextView(new SimpleText(errorExplain.getExplanation()));

        var quickReplies = errorExplain.getFallbacks()
                .stream().map(this.quickReplies::findById)
                .collect(Collectors.toList());

        return SkillTemplate.builder()
                .addOutput(text)
                .quickReplies(quickReplies)
                .build();
    }
}
