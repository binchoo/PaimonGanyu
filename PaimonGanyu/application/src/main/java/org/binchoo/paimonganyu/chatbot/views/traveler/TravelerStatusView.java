package org.binchoo.paimonganyu.chatbot.views.traveler;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.AbstractSkillResopnseView;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ItemCard;
import org.binchoo.paimonganyu.ikakao.type.*;
import org.binchoo.paimonganyu.traveler.TravelerStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/14
 */
public class TravelerStatusView extends AbstractSkillResopnseView {

    private static int thumbIndex = 0;
    private static int thumbRobin = 5;
    private static String THUMB_PREFIX = "generic_thumb_";
    private static int THUMB_FIXED_WIDTH = 800;
    private static int THUMB_FIXED_HEIGHT = 400;

    private static String THUMB_RESIN = "resin_ic";
    private static String THUMB_SEREN = "serenitea_ic";
    private static String THUMB_LUMINE = "lumine_ic_front";
    private static String THUMB_AETHER = "aether_ic_front";

    private static String TITLE_RESIN = "레진";
    private static String TITLE_RESIN_RECOV = "레진회복";
    private static String TITLE_SEREN = "선계보화";
    private static String TITLE_EXPED = "탐사중";

    private static String ITEMS_ALIGN = "right";
    private static String CAROUSEL_TYPE = "itemCard";

    public TravelerStatusView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse renderResponse(Map<String, ?> model) {
        return null;
    }

    public SkillResponse renderSkillResponse(Collection<TravelerStatus> status) {
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
        Thumbnail thumb = selectRandom(imageRepo());
        Profile profile = renderProfile(status);
        ImageTitle imageTitle = renderImageTitle(status);
        List<ItemList> itemLists = renderItemLists(status);
        return ItemCard.builder()
                .thumbnail(thumb)
                .profile(profile)
                .imageTitle(imageTitle)
                .itemList(itemLists)
                .itemListAlignment(ITEMS_ALIGN)
                .itemListSummary(new ItemListSummary("", "호요랩 참고 자료입니다."))
                .build();
    }

    private Thumbnail selectRandom(Images imageRepo) {
        String robin = getNextThumbnail();
        String imageUrl = imageRepo.findById(robin);
        return new Thumbnail(imageUrl, null, null,
                THUMB_FIXED_WIDTH, THUMB_FIXED_HEIGHT);
    }

    private Profile renderProfile(TravelerStatus status) {
        String imgUrl = profileImgStrat(status);
        String title = profileTitleStrat(status);
        return Profile.builder()
                .title(title)
                .imageUrl(imgUrl)
                .build();
    }

    private List<ItemList> renderItemLists(TravelerStatus status) {
        long resin = status.getCurrentResin();
        long m = TimeUnit.SECONDS.toMinutes(status.getCurrentResin());
        resin -= m*60;
        return List.of(new ItemList(TITLE_RESIN, status.ratioStringOfResin()),
                new ItemList(TITLE_RESIN_RECOV, String.format("%dmin %ds", m, resin)),
                new ItemList(TITLE_SEREN, status.ratioStringOfHomeCoin()),
                new ItemList(TITLE_EXPED, String.valueOf(status.getCurrentExpeditionNum())));
    }

    private ImageTitle renderImageTitle(TravelerStatus status) {
        return ImageTitle.builder()
                .title(status.nameFormat("%s Lv.%d %s"))
                .description(status.uidFormat("UID: %s"))
                .imageUrl(profileImgStrat(status.isLumine()))
                .build();
    }

    private String profileImgStrat(TravelerStatus status) {
        return imageRepo().findById((status.ratioOfResin() > status.ratioOfHomeCoin())?
                THUMB_RESIN : THUMB_SEREN);
    }

    private String profileImgStrat(boolean isLumine) {
        return imageRepo().findById(isLumine? THUMB_LUMINE : THUMB_AETHER);
    }

    private String profileTitleStrat(TravelerStatus status) {
        double resinRatio = status.ratioOfResin();
        if (resinRatio >= 95){
            return "얼른 들어가 봐야 돼!(✪Ω✪)ノ";
        }
        else if (resinRatio < 33) {
            return "오늘 여행은 괜찮았어?";
        }
        else {
            return "여행자의 상태를 보고왔어~";
        }
    }

    private Collection<? extends QuickReply> getQuickReplies() {
        return List.of(quickReplyRepo()
                .findById(FallbackMethods.Home));
    }

    private static String getNextThumbnail() {
        thumbIndex = ++thumbIndex % thumbRobin;
        return String.format("%s%d", THUMB_PREFIX, thumbIndex);
    }
}
