package org.binchoo.paimonganyu.chatbot.view;

import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Component
public class QuickReplies {

    private final Map<String, QuickReply> registry = new HashMap<>();

    public void add(FallbackId fallbackId, QuickReply quickReply) {
        this.add(fallbackId.getId(), quickReply);
    }

    private void add(String id, QuickReply quickReply) {
        if (quickReply != null)
            registry.put(id, quickReply);
    }

    public QuickReply findById(FallbackId fallbackId) {
        return findById(fallbackId.getId());
    }

    public QuickReply findById(String id) {
        return registry.get(id);
    }
}
