package org.binchoo.paimonganyu.chatbot.views.dailycheck;

import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckTrial;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
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
        // TODO : Generify the code and move it to the parent.
        Object value = model.get(TRIALS);
        if (model.get(TRIALS) != null && Collection.class.isAssignableFrom(value.getClass())) {
            Collection<?> values = (Collection<?>) value;
            if (values.size() > 0) {
                List<?> valueList = new ArrayList<>(values);
                Object firstValue = valueList.get(0);
                if (UserDailyCheckTrial.class.isAssignableFrom(firstValue.getClass())) {
                    return renderSkillResponse((List<UserDailyCheckTrial>) valueList);
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("Model[%s] cannot be rendered by %s bean.", model, this.getBeanName()));
    }

    public SkillResponse renderSkillResponse(Collection<UserDailyCheckTrial> histories) {
        assert !histories.isEmpty();
        return null;
    }
}
