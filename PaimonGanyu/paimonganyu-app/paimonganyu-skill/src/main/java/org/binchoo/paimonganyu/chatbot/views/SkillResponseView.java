package org.binchoo.paimonganyu.chatbot.views;

import org.binchoo.paimonganyu.chatbot.resources.Blocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public abstract class SkillResponseView extends MappingJackson2JsonView {

    private static final String VIEW_KEY = "View";
    public static final String CONTENT_KEY = "Content";

    protected final Images images;
    protected final QuickReplies quickReplies;
    protected final Blocks blocks;

    public SkillResponseView() {
        this(null, null, null);
    }

    public SkillResponseView(Images images, QuickReplies quickReplies, Blocks blocks) {
        super(new ObjectMapper());
        this.images = images;
        this.blocks = blocks;
        this.quickReplies = quickReplies;
        this.setExtractValueFromSingleKeyModel(true);
    }

    @Override
    public void render(Map<String, ?> model,
                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model != null) {
            Object modelContent = model.get(CONTENT_KEY);
            if (modelContent != null) {
                SkillResponse skillResponse = render(modelContent);
                model = Map.of(VIEW_KEY, skillResponse);
            }
        }
        super.render(model, request, response);
    }

    /**
     * @param modelContent the content to be contained in the new {@link SkillResponse}, never null.
     * @return {@link SkillResponse} object that's rendered via attributes in {@code model} object.
     */
    protected abstract SkillResponse render(Object modelContent);

    public Images images() {
        return images;
    }

    public Blocks blocks() {
        return blocks;
    }

    public QuickReplies quickReplies() {
        return quickReplies;
    }
}
