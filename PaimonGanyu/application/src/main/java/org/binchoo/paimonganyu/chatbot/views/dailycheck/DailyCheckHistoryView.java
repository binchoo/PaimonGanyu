package org.binchoo.paimonganyu.chatbot.views.dailycheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.resources.BlockIds;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckTrial;
import org.binchoo.paimonganyu.ikakao.SkillResponse;

import java.util.Collection;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
public class DailyCheckHistoryView extends AbstractSkillResopnseView {

    public DailyCheckHistoryView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse renderResponse(Map<String, ?> model) {
        return null;
    }

    public SkillResponse renderSkillResponse(Collection<UserDailyCheckTrial> histories) {
        assert !histories.isEmpty();
        return null;
    }
}
