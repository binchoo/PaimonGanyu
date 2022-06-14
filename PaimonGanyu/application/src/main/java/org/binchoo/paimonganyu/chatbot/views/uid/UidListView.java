package org.binchoo.paimonganyu.chatbot.views.uid;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.hoyopass.Region;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class UidListView extends AbstractSkillResopnseView {

    public static final String UIDS = "uids";

    private static final String IMAGEKEY_AETHER = "aether";
    private static final String IMAGEKEY_LUMINE = "lumine";
    private static final String PREFIX_LEVEL = "Lv.";

    public UidListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    public UidListView(ObjectMapper objectMapper, Images images, QuickReplies quickReplies) {
        super(objectMapper, images, quickReplies, null);
    }

    private final class UidValue {

        private final String server, uid, name;
        private final int level;
        private final boolean isLumine;

        public UidValue(Uid uid) {
            this.server = cutOff(uid.getRegion(), 3);
            this.uid = uid.getUidString();
            this.name = uid.getCharacterName();
            this.level = uid.getCharacterLevel();
            this.isLumine = uid.getIsLumine();
        }

        private String cutOff(Region region, int start) {
            String regionName = region.name();
            return regionName.substring(start).toUpperCase();
        }

        private String getDescription() {
            return String.format("%s %s", this.server, this.uid);
        }

        private String getTitle() {
            return String.format("%s%s %s", PREFIX_LEVEL, this.level, this.name);
        }

        private String getImageUrl() {
            if (this.isLumine)
                return imageRepo().findById(IMAGEKEY_LUMINE);
            else
                return imageRepo().findById(IMAGEKEY_AETHER);
        }
    }

    @Override
    protected SkillResponse renderResponse(Map<String, ?> model) {
        if (!model.containsKey(UIDS))
            throw new IllegalArgumentException("Model [" + model + "] does not contains List<Uid>");

        Object uids = model.get(UIDS);
        return renderSkillResponse((List<Uid>) uids);
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
                        .simpleText(new SimpleText(modelValues.size() + "명의 여행자들을 확인했어!"))
                        .build())
                .addOutput(CarouselView.builder()
                        .carousel(carouselOf(modelValues))
                        .build())
                .quickReplies(quickReplyRepo().findByFallbackMethod(getFallbacks()))
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
                        .fixedRatio(true)
                        .width(192).height(192)
                        .build())
                .title(value.getTitle())
                .description(value.getDescription())
                .build();
    }
}
