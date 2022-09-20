package org.binchoo.paimonganyu.chatbot.views.dailycheck;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckStatus;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ListCard;
import org.binchoo.paimonganyu.ikakao.type.Button;
import org.binchoo.paimonganyu.ikakao.type.ListItem;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.buttons.BlockButton;
import org.binchoo.paimonganyu.ikakao.type.subtype.Link;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
public class DailyCheckListView extends SkillResponseView implements MessageSourceAware {

    private static final String THUMB_FAIL = "dailycheck_fail";
    private static final String THUMB_DUPL = "dailycheck_duplicate";
    private static final String THUMB_COMPL = "dailycheck_complete";
    private static final String DAILY_CHECK_URL = "https://act.hoyolab.com/ys/event/signin-sea-v3/index.html?act_id=e202102251931481&lang=ko-kr";

    public DailyCheckListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse render(Object content) {
        return renderSkillResponse((List<List<UserDailyCheck>>) content);
    }

    private SkillResponse renderSkillResponse(List<List<UserDailyCheck>> dailyCheckCollection) {
        return SkillResponse.builder()
                .template(SkillTemplate.builder()
                        .addOutput(CarouselView.builder()
                                .carousel(renderCarousel(dailyCheckCollection))
                                .build())
                        .quickReplies(quickReplies().findByFallbackMethod(getFallbacks()))
                        .build())
                .build();
    }

    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] { FallbackMethods.Home, FallbackMethods.ListUserDailyCheck};
    }

    private Carousel renderCarousel(List<List<UserDailyCheck>> trials) {
        return Carousel.builder()
                .type("listCard")
                .items(renderListCards(trials))
                .build();
    }

    private Collection<? extends CanCarousel> renderListCards(List<List<UserDailyCheck>> dailyCheckCollection) {
        List<ListCard> listCards = new ArrayList<>();
        for (List<UserDailyCheck> dailyCheckPerPass : dailyCheckCollection) {
            if (!dailyCheckPerPass.isEmpty()) 
                listCards.add(renderListCard(dailyCheckPerPass));
        }
        return listCards;
    }

    private ListCard renderListCard(List<UserDailyCheck> dailyChecks) {
        return ListCard.builder()
                .header(renderHeader(dailyChecks))
                .items(renderListItems(dailyChecks))
                .buttons(renderButtons(dailyChecks))
                .build();
    }

    private ListItem renderHeader(List<UserDailyCheck> dailyChecks) {
        UserDailyCheck first = dailyChecks.get(0);
        String ltuid = first.getLtuid();
        return ListItem.builder()
                .title(String.format("통행증 번호: %s", ltuid))
                .build();
    }

    private List<ListItem> renderListItems(List<UserDailyCheck> dailyChecks) {
        return dailyChecks.stream()
                .map(this::renderListItem)
                .collect(Collectors.toList());
    }

    private ListItem renderListItem(UserDailyCheck userDailyCheck) {
        UserDailyCheckStatus status = userDailyCheck.getStatus();
        String title, description, imageUrl;
        if (status.isDuplicate()) {
            title = "중복";
            description = "이미 출석 했었네...?";
            imageUrl = images().findByName(THUMB_DUPL);
        } else if (status.isFailed()) {
            title = "실패..";
            description = "아잇.. 왜 오류가 난거야";
            imageUrl = images().findByName(THUMB_FAIL);
        } else if (status.isCompleted()) {
            title = "성공!";
            description = "페이몬이 대신 출첵해줬어!";
            imageUrl = images().findByName(THUMB_COMPL);
        } else {
            throw new RuntimeException("UserDailyCheckStatus of [" + status + "] is not supported.");
        }
        title = title + ' ' + printTime(userDailyCheck.getTimestamp());

        Link link = Link.builder()
                .web(DAILY_CHECK_URL)
                .build();

        return ListItem.builder()
                .description(description)
                .imageUrl(imageUrl)
                .title(title)
                .link(link)
                .build();
    }

    private String printTime(LocalDateTime timestamp) {
        return timestamp.format(DateTimeFormatter.ofPattern("M월 d일 H시 m분", Locale.KOREA));
    }

    private List<Button> renderButtons(List<UserDailyCheck> dailyChecks) {
        boolean isButtonRequired = !dailyChecks.get(0).isDone();
        return isButtonRequired? renderBlockButton() : Collections.emptyList();
    }

    private List<Button> renderBlockButton() {
        return List.of(BlockButton.builder()
                .blockId(blocks().findByFallbackMethod(FallbackMethods.DailyCheckIn))
                .label("호요랩 출석 요청")
                .build());
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        // TODO: i18n
    }
}
