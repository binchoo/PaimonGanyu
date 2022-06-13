package org.binchoo.paimonganyu.chatbot.view.uid;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.resource.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resource.Images;
import org.binchoo.paimonganyu.chatbot.resource.QuickReplies;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.BasicCard;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.Thumbnail;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class ListUidsView extends MappingJackson2JsonView {

    public static final String UIDS = "uids";

    private static final String IMAGEKEY_AETHER = "aether";
    private static final String IMAGEKEY_LUMINE = "lumine";
    private static final String PREFIX_LEVEL = "Lv.";

    private final Images imageRegistry;
    private final QuickReplies quickReplies;

    public ListUidsView(Images imageRegistry, QuickReplies quickReplies) {
        this.imageRegistry = imageRegistry;
        this.quickReplies = quickReplies;
        this.setExtractValueFromSingleKeyModel(true);
    }

    public ListUidsView(ObjectMapper objectMapper, Images imageRegistry, QuickReplies quickReplies) {
        super(objectMapper);
        this.imageRegistry = imageRegistry;
        this.quickReplies = quickReplies;
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
                return imageRegistry.findById(IMAGEKEY_AETHER);
            else
                return imageRegistry.findById(IMAGEKEY_LUMINE);
        }
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model == null)
            throw new NullPointerException("Model is null.");

        if (!model.containsKey(UIDS))
            throw new IllegalArgumentException("Model [" + model + "] does not containers List<Uid>");

        Object uids = model.get(UIDS);
        Map<String, SkillResponse> newModel = new HashMap<>();
        newModel.put("View", createResponse((List<Uid>) uids));

        super.render(newModel, request, response);
    }

    public SkillResponse createResponse(List<Uid> uids) {
        List<UidValue> modelValues = uids.stream().map(UidValue::new)
                .collect(Collectors.toList());

        return SkillResponse.builder()
                .template(templateOf(modelValues))
                .build();
    }

    private SkillTemplate templateOf(List<UidValue> modelValues) {
        var fallbacks = getFallbacks();
        return SkillTemplate.builder()
                .addOutput(CarouselView.builder()
                        .carousel(carouselOf(modelValues))
                        .build())
                .quickReplies(quickRepliesOf(fallbacks))
                .build();
    }

    private FallbackMethod[] getFallbacks() {
        return new FallbackMethod[] {FallbackMethods.Home};
    }

    private Carousel carouselOf(List<UidValue> modelValues) {
        var carouselBuilder = Carousel.builder()
                .type("basicCard");

        for (UidValue item : modelValues)
            carouselBuilder.addItem(basicCardOf(item));

        return carouselBuilder.build();
    }

    private List<QuickReply> quickRepliesOf(FallbackMethod[] fallbacks) {
        return Arrays.stream(fallbacks).map(this.quickReplies::findById)
                .collect(Collectors.toList());
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
