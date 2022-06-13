package org.binchoo.paimonganyu.chatbot.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public final class BlockIds {

    private final Map<String, String> blockNameAndId = new HashMap<>();

    public BlockIds(Map<String, String> blockNameAndId) {
        this.blockNameAndId.putAll(blockNameAndId);
    }

    /**
     * @param blockName 블록 이름
     * @return 블록 아이디
     */
    public String findByName(String blockName) {
        return this.blockNameAndId.get(blockName);
    }
}
