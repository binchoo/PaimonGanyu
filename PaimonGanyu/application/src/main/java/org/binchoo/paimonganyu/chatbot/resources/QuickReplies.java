package org.binchoo.paimonganyu.chatbot.resources;

import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;

import java.util.*;
import java.util.stream.Collectors;

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

    public QuickReply findByFallbackMethod(FallbackMethod fallbackMethod) {
        return findById(fallbackMethod.getId());
    }

    public Collection<QuickReply> findByFallbackMethod(FallbackMethod... fallbackMethods) {
        String[] ids = new String[fallbackMethods.length];
        for (int i = 0; i < fallbackMethods.length; i++)
            ids[i] = fallbackMethods[i].getId();
        return findById(ids);
    }

    public QuickReply findById(String id) {
        return registry.get(id);
    }

    public Collection<QuickReply> findById(String... ids) {
        return Arrays.stream(ids).map(this::findById)
                .collect(Collectors.toList());
    }
}
