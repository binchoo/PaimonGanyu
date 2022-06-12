package org.binchoo.paimonganyu.chatbot.view;

import org.binchoo.paimonganyu.error.FallbackId;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class Images {

    private final Map<String, String> registry;

    public Images() {
        this.registry = new HashMap<>();
    }

    public Images(Map<String, String> images) {
        this();
        registry.putAll(images);
    }

    public void add(String id, String imageUrl) {
        registry.put(id, imageUrl);
    }

    public String findById(String id) {
        return registry.get(id);
    }
}
