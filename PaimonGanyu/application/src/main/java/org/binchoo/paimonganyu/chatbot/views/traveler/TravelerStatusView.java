package org.binchoo.paimonganyu.chatbot.views.traveler;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.traveler.TravelerStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jbinchoo
 * @since 2022/06/14
 */
public class TravelerStatusView extends AbstractSkillResopnseView {

    public TravelerStatusView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse renderResponse(Map<String, ?> model) {
        return null;
    }

    public SkillResponse renderSkillResponse(Collection<TravelerStatus> status) {
        return SkillResponse.builder()
                .template(renderSkillTemplate(status))
                .build();
    }

    private SkillTemplate renderSkillTemplate(Collection<TravelerStatus> status) {
        return SkillTemplate.builder()
                .outputs(renderOutputs(status))
                .quickReplies(getQuickReplies())
                .build();
    }

    private Collection<? extends QuickReply> getQuickReplies() {
        return List.of(quickReplyRepo()
                .findById(FallbackMethods.Home));
    }

    private Collection<? extends Component> renderOutputs(Collection<TravelerStatus> status) {

    }
}
