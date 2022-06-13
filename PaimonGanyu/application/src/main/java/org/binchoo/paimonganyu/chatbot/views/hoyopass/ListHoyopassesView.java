package org.binchoo.paimonganyu.chatbot.views.hoyopass;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.resources.BlockIds;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ListCard;
import org.binchoo.paimonganyu.ikakao.type.ListItem;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.buttons.BlockButton;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class ListHoyopassesView extends AbstractSkillResopnseView {

    public static final String HOYOPASSES = "hoyopasses";

    public ListHoyopassesView(Images images, QuickReplies quickReplies, BlockIds blockIds) {
        super(images, quickReplies, blockIds);
    }

    public ListHoyopassesView(ObjectMapper objectMapper, Images images, QuickReplies quickReplies, BlockIds blockIds) {
        super(objectMapper, images, quickReplies, blockIds);
    }

    @Override
    protected SkillResponse renderResponse(Map<String, ?> model) {
        return renderSkillResponse((List<Hoyopass>) model.get(HOYOPASSES));
    }

    public SkillResponse renderSkillResponse(List<Hoyopass> hoyopasses) {
        return SkillResponse.builder()
                .template(SkillTemplate.builder()
                        .outputs(List.of(CarouselView.builder()
                                .carousel(Carousel.builder()
                                        .type("listCard")
                                        .items(hoyopasses.stream().map(hoyopass-> ListCard.builder()
                                                .header(getCardHeader(hoyopass))
                                                .items(hoyopass.getUids().stream().map(uid-> ListItem.builder()
                                                        .title(getItemTitle(uid))
                                                        .imageUrl(getItemImage(uid))
                                                        .description(getItemDescription(uid))
                                                        .build())
                                                        .collect(Collectors.toList()))
                                                .addButton(BlockButton.builder()
                                                        .label("출석체크")
                                                        .blockId(blockIdRepo().findByName(FallbackMethods.DoDailyCheck))
                                                        .build())
                                                .addButton(BlockButton.builder()
                                                        .label("제거하기")
                                                        .blockId(blockIdRepo().findByName(FallbackMethods.DeleteHoyopass))
                                                        .build())
                                                .build())
                                                .collect(Collectors.toList()))
                                        .build())
                                .build()))
                        .quickReplies(quickRepliesOf(getFallbacks()))
                        .build())
                .build();
    }

    private ListItem getCardHeader(Hoyopass hoyopass) {
        return ListItem.builder()
                .title("통행증 번호:" + hoyopass.getLtuid())
                .build();
    }

    private String getItemTitle(Uid uid) {
        return String.format("$s Lv.%d %s",
                uid.getRegion().lowercase(), uid.getCharacterLevel(), uid.getCharacterName());
    }

    private String getItemDescription(Uid uid) {
        return String.format("UID: %s", uid.getUidString());
    }

    private String getItemImage(Uid uid) {
        return imageRepo().findById((uid.getIsLumine())? "lumine_ic_front":"aether_ic_front");
    }


    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] {FallbackMethods.Home};
    }
}
