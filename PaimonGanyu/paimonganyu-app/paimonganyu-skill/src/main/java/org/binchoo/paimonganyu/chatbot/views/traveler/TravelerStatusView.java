package org.binchoo.paimonganyu.chatbot.views.traveler;

import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ItemCard;
import org.binchoo.paimonganyu.ikakao.type.*;
import org.binchoo.paimonganyu.traveler.TravelerStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/14
 */
public class TravelerStatusView extends SkillResponseView {

    private static int thumbIndex = 0;
    private static final int thumbRobin = 5;
    private static final String THUMB_PREFIX = "generic_thumb_";
    private static final int THUMB_FIXED_WIDTH = 800;
    private static final int THUMB_FIXED_HEIGHT = 800;

    private static final String THUMB_RESIN = "resin_ic";
    private static final String THUMB_SEREN = "serentea_ic";
    private static final String THUMB_LUMINE = "lumine_ic_front";
    private static final String THUMB_AETHER = "aether_ic_front";

    private static final String TITLE_RESIN = "레진";
    private static final String TITLE_RESIN_RECOV = "레진회복";
    private static final String TITLE_SEREN = "선계보화";
    private static final String TITLE_EXPED = "탐사중";

    private static final String ITEMS_ALIGN = "right";
    private static final String CAROUSEL_TYPE = "itemCard";

    public TravelerStatusView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse renderSkillResponse(Object modelContent) {
        return this.renderSkillResponse((Collection<TravelerStatus>) modelContent);
    }

    public SkillResponse renderSkillResponse(Collection<TravelerStatus> status) {
        assert !status.isEmpty();
        return SkillResponse.builder()
                .template(renderSkillTemplate(status))
                .build();
    }

    private SkillTemplate renderSkillTemplate(Collection<TravelerStatus> status) {
        return SkillTemplate.builder()
                .addOutput(CarouselView.builder()
                        .carousel(Carousel.builder()
                                .type(CAROUSEL_TYPE)
                                .items(renderItemCards(status))
                                .build())
                        .build())
                .quickReplies(getQuickReplies())
                .build();
    }

    private Collection<? extends CanCarousel> renderItemCards(Collection<TravelerStatus> status) {
        return status.stream().map(this::renderItemCard)
                .collect(Collectors.toList());
    }

    private ItemCard renderItemCard(TravelerStatus status) {
        ItemCard.Thumbnail thumb = selectRandom(images());
        Profile profile = renderProfile(status);
        ImageTitle imageTitle = renderImageTitle(status);
        List<ItemList> itemLists = renderItemLists(status);
        return ItemCard.builder()
                .thumbnail(thumb)
                .profile(profile)
                .imageTitle(imageTitle)
                .itemList(itemLists)
                .itemListAlignment(ITEMS_ALIGN)
                .itemListSummary(new ItemListSummary("호요랩참고", ""))
                .build();
    }

    private ItemCard.Thumbnail selectRandom(Images imageRepo) {
        String robin = nextThumbnail();
        String imageUrl = imageRepo.findByName(robin);
        return new ItemCard.Thumbnail(imageUrl, THUMB_FIXED_WIDTH, THUMB_FIXED_HEIGHT);
    }

    private Profile renderProfile(TravelerStatus status) {
        String imgUrl = profileImageStrategy(status);
        String title = profileTitleStrategy(status);
        return Profile.builder()
                .title(title)
                .imageUrl(imgUrl)
                .build();
    }

    private List<ItemList> renderItemLists(TravelerStatus status) {
        long resinRecovery = status.getResinRecoverySeconds();
        long hour = resinRecovery / 3600;
        long min = resinRecovery % 3600 / 60;
        long sec = resinRecovery % 3600 % 60;
        return List.of(new ItemList(TITLE_RESIN, status.ratioStringOfResin()),
                new ItemList(TITLE_RESIN_RECOV, String.format("%dh %dm %ds", hour, min, sec)),
                new ItemList(TITLE_SEREN, status.ratioStringOfHomeCoin()),
                new ItemList(TITLE_EXPED, String.valueOf(status.getCurrentExpeditionNum())));
    }

    private ImageTitle renderImageTitle(TravelerStatus status) {
        return ImageTitle.builder()
                .title(status.nameFormat("%s Lv.%d %s"))
                .description(status.uidFormat("UID: %s"))
                .imageUrl(profileImageStrategy(status.isLumine()))
                .build();
    }

    private String profileImageStrategy(TravelerStatus status) {
        return images().findByName((status.ratioOfResin() > status.ratioOfHomeCoin())?
                THUMB_RESIN : THUMB_SEREN);
    }

    private String profileImageStrategy(boolean isLumine) {
        return images().findByName(isLumine? THUMB_LUMINE : THUMB_AETHER);
    }

    private String profileTitleStrategy(TravelerStatus status) {
        double resinRatio = status.ratioOfResin();
        if (resinRatio >= 95){
            return "들어가야 돼!(✪Ω✪)ノ";
        }
        else if (resinRatio < 33) {
            return "오늘 여행은 괜찮았어?";
        }
        else {
            return "여행자의 상태를 보고왔어~";
        }
    }

    private Collection<? extends QuickReply> getQuickReplies() {
        return List.of(quickReplies()
                .findByFallbackMethod(FallbackMethods.Home));
    }

    private static String nextThumbnail() {
        thumbIndex = ++thumbIndex % thumbRobin;
        return String.format("%s%d", THUMB_PREFIX, thumbIndex);
    }
}
