package org.binchoo.paimonganyu.chatbot.views.redeem;

import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;

import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-09-17
 */
public class RedeemListTextView extends SkillResponseView {

    public RedeemListTextView() {
        super();
    }

    @Override
    protected SkillResponse render(Object modelContent) {
        return null;
    }

    private SkillResponse renderSkillResponse() {
        var text = new SimpleTextView(new SimpleText(errorExplain.getExplanation()));

        var quickReplies = errorExplain.getFallbacks()
                .stream().map(this.quickReplies::findByFallbackMethod)
                .collect(Collectors.toList());

        return SkillTemplate.builder()
                .addOutput(text)
                .quickReplies(quickReplies)
                .build();
    }
}
