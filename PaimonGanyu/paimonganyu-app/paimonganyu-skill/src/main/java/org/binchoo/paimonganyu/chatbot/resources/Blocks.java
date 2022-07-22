package org.binchoo.paimonganyu.chatbot.resources;

import org.binchoo.paimonganyu.error.FallbackMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jbinchoo
 * @since 2022-06-13
 */
public final class Blocks {

    private final Map<String, String> blocks;

    /**
     * @param blocks 블록의 논리적 이름 대 블록 아이디 매핑 테이블
     */
    public Blocks(Map<String, BlockSpec> blocks) {
        this.blocks = new HashMap<>();
        blocks.forEach((name, blockSpec)-> this.blocks.put(name, blockSpec.getId()));
    }

    /**
     * @param blockName 챗봇 블록 이름
     * @return 챗봇 블록 아이디
     */
    public String findByName(String blockName) {
        return this.blocks.get(blockName);
    }

    /**
     * @param fallbackMethod 대체 수단 객체
     * @return 대체 수단 객체가 지칭한 블록의 아이디
     */
    public String findByFallbackMethod(FallbackMethod fallbackMethod) {
        String blockName = fallbackMethod.getKey();
        return this.blocks.get(blockName);
    }
}
