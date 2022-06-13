package org.binchoo.paimonganyu.chatbot.resources;

import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
public final class QuickReplies {

    private final Map<String, QuickReply> registry = new HashMap<>();

    public void add(FallbackMethod fallbackMethod, QuickReply quickReply) {
        this.add(fallbackMethod.getId(), quickReply);
    }

    private void add(String id, QuickReply quickReply) {
        if (quickReply != null)
            registry.put(id, quickReply);
    }

    public QuickReply findById(FallbackMethod fallbackMethod) {
        return findById(fallbackMethod.getId());
    }

    public QuickReply findById(String id) {
        return registry.get(id);
    }
}
