package org.binchoo.paimonganyu.chatbot.views.hoyopass;

import org.binchoo.paimonganyu.chatbot.resources.Blocks;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ListCard;
import org.binchoo.paimonganyu.ikakao.type.ListItem;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.buttons.BlockButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class HoyopassListView extends SkillResponseView {

    public HoyopassListView(Images images, QuickReplies quickReplies, Blocks blocks) {
        super(images, quickReplies, blocks);
    }

    @Override
    protected SkillResponse renderSkillResponse(Object modelContent) {
        return this.renderSkillResponse((List<Hoyopass>) modelContent);
    }

    public SkillResponse renderSkillResponse(List<Hoyopass> hoyopasses) {
        return SkillResponse.builder()
                .template(SkillTemplate.builder()
                        .outputs(List.of(CarouselView.builder()
                                .carousel(Carousel.builder()
                                        .type("listCard")
                                        .items(listCardsOf(hoyopasses))
                                        .build())
                                .build()))
                        .quickReplies(quickReplies().findByFallbackMethod(getFallbacks()))
                        .build())
                .build();
    }

    private Collection<? extends CanCarousel> listCardsOf(List<Hoyopass> hoyopasses) {
        List<ListCard> listCardArray = new ArrayList<>();
        for (int i = 0; i < hoyopasses.size(); i++) {
            Hoyopass hoyopass = hoyopasses.get(i);
            ListCard listCard = ListCard.builder()
                    .header(getListCardHeader(hoyopass))
                    .items(getListItems(hoyopass))
                    .addButton(BlockButton.builder()
                            .label("출석체크")
                            .messageText("이 통행증 계정으로 출석체크 대신해줘")
                            .blockId(blocks().findByFallbackMethod(FallbackMethods.DailyCheckIn))
                            .extra(Map.of("index", i))
                            .build())
                    .addButton(BlockButton.builder()
                            .label("제거하기")
                            .messageText("이 통행증 지워줬으면 해")
                            .blockId(blocks().findByFallbackMethod(FallbackMethods.DeleteHoyopass))
                            .extra(Map.of("index", i))
                            .build())
                    .build();
            listCardArray.add(listCard);
        }
        return listCardArray;
    }

    private List<ListItem> getListItems(Hoyopass hoyopass) {
        return hoyopass.getUids().stream()
                .map(uid -> ListItem.builder()
                        .title(getItemTitle(uid))
                        .imageUrl(getItemImage(uid))
                        .description(getItemDescription(uid))
                        .build())
                .collect(Collectors.toList());
    }

    private ListItem getListCardHeader(Hoyopass hoyopass) {
        return ListItem.builder()
                .title("통행증 번호:" + hoyopass.getLtuid())
                .build();
    }

    private String getItemTitle(Uid uid) {
        return String.format("Lv.%d %s", uid.getCharacterLevel(), uid.getCharacterName());
    }

    private String getItemDescription(Uid uid) {
        return String.format("UID: %s %s", uid.getRegion().suffixLargeCase(), uid.getUidString());
    }

    private String getItemImage(Uid uid) {
        return images().findByName((uid.getIsLumine())? "lumine_ic_front":"aether_ic_front");
    }

    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] {FallbackMethods.Home};
    }
}
