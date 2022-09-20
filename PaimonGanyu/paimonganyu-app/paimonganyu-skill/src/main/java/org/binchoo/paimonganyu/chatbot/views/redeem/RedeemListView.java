package org.binchoo.paimonganyu.chatbot.views.redeem;

import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.CarouselView;
import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import org.binchoo.paimonganyu.ikakao.component.componentType.ItemCard;
import org.binchoo.paimonganyu.ikakao.type.*;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022-09-17
 */
public class RedeemListView extends SkillResponseView {

    private static final String THUMB_FAIL = "redeem_fail";
    private static final String THUMB_COMPL = "redeem_complete";
    private static final String THUMB_LUMINE = "lumine_ic_front";
    private static final String THUMB_AETHER = "aether_ic_front";

    private static final String FORMAT_PROFILE_TITLE = "최신 %s건을 보여줄게!";
    private static final String FORMAT_CHARACTER_INFO = "%s Lv.%d %s";
    private static final String FORMAT_UID = "UID: %s";
    private static final String FORMAT_CODE_REASON = "%s %s";
    private static final String FORMAT_DATE = "M월d일";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private static final String CAROUSEL_TYPE = "itemCard";
    private static final String ITEMS_ALIGN = "right";

    public RedeemListView(Images images, QuickReplies quickReplies) {
        super(images, quickReplies, null);
    }

    @Override
    protected SkillResponse render(Object content) {
        return renderSkillResponse((List<UidRedeem>) content);
    }

    public SkillResponse renderSkillResponse(List<UidRedeem> uidRedeems) {
        assert !uidRedeems.isEmpty();
        return SkillResponse.builder()
                .template(renderSkillTemplate(uidRedeems))
                .build();
    }

    private SkillTemplate renderSkillTemplate(List<UidRedeem> uidRedeems) {
        return SkillTemplate.builder()
                .addOutput(CarouselView.builder()
                        .carousel(Carousel.builder()
                                .type(CAROUSEL_TYPE)
                                .items(renderItemCards(uidRedeems))
                                .build())
                        .build())
                .quickReplies(getQuickReplies())
                .build();
    }

    private Collection<? extends CanCarousel> renderItemCards(List<UidRedeem> uidRedeems) {
        return uidRedeems.stream().filter(uidRedeem -> !uidRedeem.isEmpty())
                .map(this::renderItemCard)
                .collect(Collectors.toList());
    }

    private ItemCard renderItemCard(UidRedeem uidRedeem) {
        Profile profile = renderProfile(uidRedeem);
        ImageTitle imageTitle = renderImageTitle(uidRedeem);
        List<ItemList> itemLists = renderItemLists(uidRedeem);
        return ItemCard.builder()
                .profile(profile)
                .imageTitle(imageTitle)
                .itemList(itemLists)
                .itemListAlignment(ITEMS_ALIGN)
                .build();
    }

    private Profile renderProfile(UidRedeem uidRedeem) {
        String imgUrl = profileImageStrategy(uidRedeem);
        String title = String.format(FORMAT_PROFILE_TITLE, uidRedeem.getTotalCount());
        return Profile.builder()
                .title(title)
                .imageUrl(imgUrl)
                .build();
    }

    private List<ItemList> renderItemLists(UidRedeem uidRedeem) {
        return uidRedeem.getRedeems().stream()
                .map(this::renderItemList)
                .collect(Collectors.toList());
    }

    private ItemList renderItemList(UserRedeem userRedeem) {
        String date = userRedeem.getDate().format(dateFormatter);
        String code = userRedeem.getRedeemCode().getCode();
        String successful = userRedeem.isDone()? "성공" : "실패";
        String reason = userRedeem.getReason();

        return new ItemList(date,
                String.format("%s%n%s 사유:%s", code, successful, reason));
    }

    private ImageTitle renderImageTitle(UidRedeem uidRedeem) {
        return ImageTitle.builder()
                .title(String.format(FORMAT_CHARACTER_INFO, uidRedeem.getRegion(), uidRedeem.getCharacterLevel(), uidRedeem.getCharacterName()))
                .description(String.format(FORMAT_UID, uidRedeem.getUidString()))
                .imageUrl(profileImageStrategy(uidRedeem.isLumine()))
                .build();
    }

    private String profileImageStrategy(UidRedeem uidRedeem) {
        return images().findByName(uidRedeem.isSuccessful()? THUMB_COMPL : THUMB_FAIL);
    }

    private String profileImageStrategy(boolean isLumine) {
        return images().findByName(isLumine? THUMB_LUMINE : THUMB_AETHER);
    }

    private Collection<? extends QuickReply> getQuickReplies() {
        return quickReplies().findByFallbackMethod(
                FallbackMethods.Home, FallbackMethods.ListUserRedeemText);
    }
}
