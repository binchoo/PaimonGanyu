package org.binchoo.paimonganyu.chatbot.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.resources.BlockIds;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
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
public abstract class AbstractSkillResopnseView extends MappingJackson2JsonView {

    protected final Images images;
    protected final QuickReplies quickReplies;
    protected final BlockIds blockIds;

    public AbstractSkillResopnseView(Images images, QuickReplies quickReplies, BlockIds blockIds) {
        super();
        this.images = images;
        this.quickReplies = quickReplies;
        this.blockIds = blockIds;
        this.setExtractValueFromSingleKeyModel(true);
    }

    public AbstractSkillResopnseView(ObjectMapper objectMapper,
                                     Images images, QuickReplies quickReplies, BlockIds blockIds) {
        super(objectMapper);
        this.images = images;
        this.quickReplies = quickReplies;
        this.blockIds = blockIds;
        this.setExtractValueFromSingleKeyModel(true);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model == null)
            throw new NullPointerException("Model is null.");

        Map<String, SkillResponse> newModel = new HashMap<>();
        newModel.put("View", renderResponse(model));

        super.render(model, request, response);
    }

    /**
     * @param model model, which is never {@code null}
     * @return {@link SkillResponse} object that's rendered via attributes in {@code model} object.
     */
    protected abstract SkillResponse renderResponse(Map<String,?> model);

    public Images imageRepo() {
        return images;
    }

    public QuickReplies quickReplyRepo() {
        return quickReplies;
    }

    public BlockIds blockIdRepo() {
        return blockIds;
    }
}
