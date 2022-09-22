package org.binchoo.paimonganyu.chatbot.views.uid;

import org.binchoo.paimonganyu.chatbot.resources.Blocks;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.BasicCard;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.Button;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.Thumbnail;
import org.binchoo.paimonganyu.ikakao.type.buttons.BlockButton;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class UidListView extends SkillResponseView {

    private static final String IMAGEKEY_AETHER = "aether_banner";
    private static final String IMAGEKEY_LUMINE = "lumine_banner";

    public UidListView(Images images, QuickReplies quickReplies, Blocks blocks) {
        super(images, quickReplies, blocks);
    }

    private final class UidDto {

        private final String server, uid, name;
        private final boolean isSingleUid;
        private final int level;
        private final boolean isLumine;

        public UidDto(Uid uid, boolean isSingleUid) {
            this.server = uid.getRegion().suffixLargeCase();
            this.uid = uid.getUidString();
            this.name = uid.getCharacterName();
            this.isSingleUid = isSingleUid;
            this.level = uid.getCharacterLevel();
            this.isLumine = uid.getIsLumine();
        }

        private String getDescription() {
            return String.format("%s %s", this.server, this.uid);
        }

        private String getTitle() {
            return String.format("Lv.%d %s", this.level, this.name);
        }

        private String getImageUrl() {
            if (this.isLumine)
                return images().findByName(IMAGEKEY_LUMINE);
            else
                return images().findByName(IMAGEKEY_AETHER);
        }

        public Button getButton() {
            if (isSingleUid) return null;
            return BlockButton.builder()
                    .label("얘는 없애줘")
                    .messageText(String.format("%s, %s는 이미 여행을 다녀왔어", name, isLumine? "그녀" : "그"))
                    .blockId(blocks().findByFallbackMethod(FallbackMethods.DeleteUid))
                    .extra(Map.of("uid", uid))
                    .build();
        }
    }

    @Override
    protected SkillResponse render(Object modelContent) {
        return renderSkillResponse((List<Hoyopass>) modelContent);
    }

    private SkillResponse renderSkillResponse(List<Hoyopass> passes) {
        List<UidDto> uids = passes.stream().flatMap(pass-> {
            boolean isSingleUid = pass.size() == 1;
            return pass.getUids().stream().map(uid-> new UidDto(uid, isSingleUid));
        }).collect(Collectors.toList());

        return SkillResponse.builder()
                .template(templateOf(uids))
                .build();
    }

    private SkillTemplate templateOf(List<UidDto> uids) {
        return SkillTemplate.builder()
                .addOutput(SimpleTextView.builder()
                        .simpleText(new SimpleText("이제 " + uids.size() + "명의 여행자들을 관리할게!"))
                        .build())
                .addOutput(CarouselView.builder()
                        .carousel(carouselOf(uids))
                        .build())
                .quickReplies(quickReplies().findByFallbackMethod(getFallbacks()))
                .build();
    }

    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] {FallbackMethods.HomeAliasStopTravelerRemovalLoop, FallbackMethods.ListHoyopass};
    }

    private Carousel carouselOf(List<UidDto> uids) {
        var carouselBuilder = Carousel.builder()
                .type("basicCard");

        for (UidDto item : uids)
            carouselBuilder.addItem(basicCardOf(item));

        return carouselBuilder.build();
    }

    private BasicCard basicCardOf(UidDto uid) {
        return BasicCard.builder()
                .thumbnail(Thumbnail.builder()
                        .imageUrl(uid.getImageUrl())
                        .fixedRatio(false)
                        .width(800).height(400)
                        .build())
                .title(uid.getTitle())
                .description(uid.getDescription())
                .addButton(uid.getButton())
                .build();
    }
}
