package org.binchoo.paimonganyu.chatbot.views.redeem;

import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.ikakao.SkillResponse;

/**
 * @author : jbinchoo
 * @since : 2022-09-17
 */
public class RedeemListView extends SkillResponseView {

    public RedeemListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse render(Object modelContent) {
        return null;
    }
}
