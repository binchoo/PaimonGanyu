package org.binchoo.paimonganyu.chatbot.view.uid;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorFallbacks;
import org.binchoo.paimonganyu.chatbot.view.ResponseTemplate;
import org.binchoo.paimonganyu.chatbot.view.resource.Images;
import org.binchoo.paimonganyu.chatbot.view.resource.QuickReplies;
import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.BasicCardView;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.BasicCard;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.binchoo.paimonganyu.ikakao.type.Thumbnail;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/13
 */
@RequiredArgsConstructor
@Component
public class UidResponseTemplate implements ResponseTemplate {

    private static final String IMAGEKEY_AETHER = "aether";
    private static final String IMAGEKEY_LUMINE = "lumine";
    private static final String PREFIX_LEVEL = "Lv.";

    private final Images imageRegistry;
    private final QuickReplies quickReplies;

    @Override
    public SkillResponse render(ModelMap model) {
        List<UidModelMap.Item> items = (List<UidModelMap.Item>) model.getAttribute("items");

        assert items != null;
        return SkillResponse.builder()
                .template(render(items))
                .build();
    }

    private SkillTemplate render(List<UidModelMap.Item> items) {
        var carouselBuilder = Carousel.builder()
                .type("basicCard");

        for (UidModelMap.Item item : items)
            carouselBuilder.addItem(createCard(item));

        var quickReplies = Arrays.stream(getFallbacks()).map(this.quickReplies::findById)
                .collect(Collectors.toList());

        return SkillTemplate.builder()
                .addOutput(CarouselView.builder()
                        .carousel(carouselBuilder.build())
                        .build())
                .quickReplies(quickReplies)
                .build();
    }

    private BasicCardView createCard(UidModelMap.Item item) {
        return BasicCardView.builder()
                .basicCard(BasicCard.builder()
                        .thumbnail(Thumbnail.builder()
                                .imageUrl(getImageUrl(item))
                                .fixedRatio(true)
                                .width(192).height(192)
                                .build())
                        .title(getTitle(item))
                        .description(getDescription(item))
                        .build())
                .build();
    }

    private FallbackId[] getFallbacks() {
        return new FallbackId[] {ErrorFallbacks.Home};
    }

    private String getDescription(UidModelMap.Item item) {
        return String.format("%s %s", item.getServer(), item.getUid());
    }

    private String getTitle(UidModelMap.Item item) {
        return String.format("%s%s %s", PREFIX_LEVEL, item.getLevel(), item.getName());
    }

    private String getImageUrl(UidModelMap.Item item) {
        if (item.isLumine())
            return lumineImageUrl();
        else
            return aetherImageUrl();
    }

    private String aetherImageUrl() {
        return imageRegistry.findById(IMAGEKEY_AETHER);
    }

    private String lumineImageUrl() {
        return imageRegistry.findById(IMAGEKEY_LUMINE);
    }
}
