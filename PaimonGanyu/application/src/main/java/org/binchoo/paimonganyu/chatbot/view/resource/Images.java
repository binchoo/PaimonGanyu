package org.binchoo.paimonganyu.chatbot.view.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class Images {

    private final Map<String, String> images;

    public Images(Map<String, String> images) {
        this.images = new HashMap<>(images);
    }

    public String findById(String id) {
        return images.get(id);
    }

    public Map<String, String> getImages() {
        return images;
    }
}
