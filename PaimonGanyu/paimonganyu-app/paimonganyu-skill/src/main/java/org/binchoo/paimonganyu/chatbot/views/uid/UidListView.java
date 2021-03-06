package org.binchoo.paimonganyu.chatbot.views.uid;

import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.BasicCard;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.Thumbnail;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class UidListView extends SkillResponseView {

    private static final String IMAGEKEY_AETHER = "aether_banner";
    private static final String IMAGEKEY_LUMINE = "lumine_banner";

    public UidListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    private final class UidValue {

        private final String server, uid, name;
        private final int level;
        private final boolean isLumine;

        public UidValue(Uid uid) {
            this.server = uid.getRegion().suffixLargeCase();
            this.uid = uid.getUidString();
            this.name = uid.getCharacterName();
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
    }

    @Override
    protected SkillResponse renderSkillResponse(Object modelContent) {
        return renderSkillResponse((List<Uid>) modelContent);
    }

    public SkillResponse renderSkillResponse(List<Uid> uids) {
        List<UidValue> modelValues = uids.stream().map(UidValue::new)
                .collect(Collectors.toList());

        return SkillResponse.builder()
                .template(templateOf(modelValues))
                .build();
    }

    private SkillTemplate templateOf(List<UidValue> modelValues) {
        return SkillTemplate.builder()
                .addOutput(SimpleTextView.builder()
                        .simpleText(new SimpleText("?????? " + modelValues.size() + "?????? ??????????????? ????????????!"))
                        .build())
                .addOutput(CarouselView.builder()
                        .carousel(carouselOf(modelValues))
                        .build())
                .quickReplies(quickReplies().findByFallbackMethod(getFallbacks()))
                .build();
    }

    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] {FallbackMethods.Home, FallbackMethods.ListHoyopass};
    }

    private Carousel carouselOf(List<UidValue> modelValues) {
        var carouselBuilder = Carousel.builder()
                .type("basicCard");

        for (UidValue item : modelValues)
            carouselBuilder.addItem(basicCardOf(item));

        return carouselBuilder.build();
    }

    private BasicCard basicCardOf(UidValue value) {
        return BasicCard.builder()
                .thumbnail(Thumbnail.builder()
                        .imageUrl(value.getImageUrl())
                        .fixedRatio(false)
                        .width(800).height(400)
                        .build())
                .title(value.getTitle())
                .description(value.getDescription())
                .build();
    }
}
