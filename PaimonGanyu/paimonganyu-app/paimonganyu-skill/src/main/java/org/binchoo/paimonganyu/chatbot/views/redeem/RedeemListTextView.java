package org.binchoo.paimonganyu.chatbot.views.redeem;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-09-17
 */
public class RedeemListTextView extends SkillResponseView {

    private static final String FORMAT_DATE = "yyyy/MM/dd HH:mm:SS";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
    private static final String LEGEND = "날짜|시간|코드|리딤사유|성패|UID";
    private static final String LINE_SPLIT = "\n_______________\n";

    public RedeemListTextView(QuickReplies quickReplies) {
        super(null, quickReplies, null);
    }

    @Override
    protected SkillResponse render(Object modelContent) {
        return this.renderSkillResponse((List<UserRedeem>) modelContent);
    }

    private SkillResponse renderSkillResponse(List<UserRedeem> userRedeems) {
        assert !userRedeems.isEmpty();
        return SkillResponse.builder()
                .template(renderSkillTemplate(userRedeems))
                .build();
    }

    private SkillTemplate renderSkillTemplate(List<UserRedeem> userRedeems) {
        return SkillTemplate.builder()
                .addOutput(SimpleTextView.builder()
                        .simpleText(SimpleText.builder()
                                .text(concatLogs(userRedeems))
                                .build())
                        .build())
                .quickReplies(quickReplies()
                        .findByFallbackMethod(FallbackMethods.Home, FallbackMethods.ListUserRedeem))
                .build();
    }

    private String concatLogs(List<UserRedeem> userRedeems) {
        StringBuilder sb = new StringBuilder(LEGEND);
        for (UserRedeem userRedeem : userRedeems) {
            sb.append(LINE_SPLIT);
            sb.append(dateFormatter.format(userRedeem.getDate()));
            sb.append(' ');
            sb.append(userRedeem.getRedeemCode().getCode());
            sb.append(' ');
            sb.append(userRedeem.getReason());
            sb.append(' ');
            sb.append(userRedeem.isDone()? "성공" : "실패");
            sb.append(' ');
            sb.append(userRedeem.getUid());
        }
        return sb.substring(0, sb.length() - 1);
    }
}
