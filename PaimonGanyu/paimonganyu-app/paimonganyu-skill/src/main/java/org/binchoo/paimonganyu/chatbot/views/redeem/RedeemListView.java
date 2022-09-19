package org.binchoo.paimonganyu.chatbot.views.redeem;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ListCard;
import org.binchoo.paimonganyu.ikakao.type.ListItem;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-09-17
 */
public class RedeemListView extends SkillResponseView {

    private static final String THUMB_FAIL = "redeem_fail";
    private static final String THUMB_COMPL = "redeem_complete";

    public RedeemListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse render(Object content) {
        return renderSkillResponse((List<PassRedeem>) content);
    }

    public SkillResponse renderSkillResponse(List<PassRedeem> passRedeems) {
        return SkillResponse.builder()
                .template(SkillTemplate.builder()
                        .addOutput(CarouselView.builder()
                                .carousel(renderCarousel(passRedeems))
                                .build())
                        .quickReplies(quickReplies().findByFallbackMethod(getFallbacks()))
                        .build())
                .build();
    }

    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] { FallbackMethods.Home, FallbackMethods.ListUserRedeemText};
    }

    private Carousel renderCarousel(List<PassRedeem> passRedeems) {
        return Carousel.builder()
                .type("listCard")
                .items(renderListCards(passRedeems))
                .build();
    }

    private Collection<? extends CanCarousel> renderListCards(List<PassRedeem> passRedeems) {
        List<ListCard> listCards = new ArrayList<>();
        for (PassRedeem passRedeem : passRedeems) {
            if (!passRedeem.isEmpty())
                listCards.add(renderListCard(passRedeem));
        }
        return listCards;
    }

    private ListCard renderListCard(PassRedeem passRedeem) {
        return ListCard.builder()
                .header(renderHeader(passRedeem))
                .items(renderLineItems(passRedeem))
                .build();
    }

    private ListItem renderHeader(PassRedeem passRedeem) {
        String ltuid = passRedeem.getHoyopass().getLtuid();
        return ListItem.builder()
                .title(String.format("통행증 번호: %s", ltuid))
                .build();
    }

    private List<ListItem> renderLineItems(PassRedeem passRedeem) {
        return passRedeem.getCodeStatistics().stream()
                .map(this::renderListItem)
                .collect(Collectors.toList());
    }

    private ListItem renderListItem(PassRedeem.RedeemCodeStatistic statistic) {
        String imageUrl, successFail;

        if (statistic.getSuccessRate() == 1.d) {
            imageUrl = images().findByName(THUMB_COMPL);
            successFail = "성공";
        } else {
            int success = statistic.getSuccessCount();
            imageUrl = images().findByName(THUMB_FAIL);
            if (success == 0)
                successFail = "실패";
            else
                successFail = String.format("%s/%s", success, statistic.getTotalCount());
        }

        String title = String.format("%s %s", printTime(statistic.getDate()), statistic.getReason());
        String description = String.format("%s %s", statistic.getCode(), successFail);

        return ListItem.builder()
                .description(description)
                .imageUrl(imageUrl)
                .title(title)
                .build();
    }

    private String printTime(LocalDateTime timestamp) {
        return timestamp.format(DateTimeFormatter.ofPattern("M월d일", Locale.KOREA));
    }
}
