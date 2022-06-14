package org.binchoo.paimonganyu.chatbot.views.dailycheck;

import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.type.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
public class DailyCheckTrialListView extends AbstractSkillResopnseView {

    public static final String TRIALS = "trials";

    public DailyCheckTrialListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse renderResponse(Map<String, ?> model) {
        Object value = model.get(TRIALS);
        if (model.get(TRIALS) != null)
            return renderSkillResponse((List<List<UserDailyCheck>>) value);

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("Model[%s] cannot be rendered by %s bean.", model, this.getBeanName()));
    }

    public SkillResponse renderSkillResponse(List<List<UserDailyCheck>> trials) {
        return null;
    }
}
