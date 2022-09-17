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

    private static final String ACTION_BLOCK = "block";

    private final Map<String, QuickReply> quickReplies;

    /**
     * @param blocks 블록 이름 대 블록 정보 매핑
     */
    public QuickReplies(Map<String, BlockSpec> blocks) {
        this.quickReplies = new HashMap<>();
        blocks.forEach((name, block)->
                this.add(name, new QuickReply(block.getLabel(), ACTION_BLOCK, null, block.getId(), null)));
    }

    private void add(String blockName, QuickReply quickReply) {
        quickReplies.put(blockName, Objects.requireNonNull(quickReply));
    }

    /**
     * @param blockName 블록 이름
     * @return 이 블록과의 연결을 수립한 바로 연결 응답 객체
     */
    public QuickReply findByName(String blockName) {
        return this.quickReplies.get(blockName);
    }

    /**
     * @param blockNames 블록 이름들
     * @return 주어진 각 블록과의 연결을 수립한 바로 연결 응답 객체들
     */
    public Collection<QuickReply> findByName(String... blockNames) {
        return Arrays.stream(blockNames).map(this::findByName)
                .collect(Collectors.toList());
    }

    /**
     * @param fallbackMethod 대체 수단 객체
     * @return 대체 수단이 지칭하는 블록과의 연결을 수립한 바로 연결 응답 객체
     */
    public QuickReply findByFallbackMethod(FallbackMethod fallbackMethod) {
        return this.findByName(fallbackMethod.getKey());
    }

    /**
     * @param fallbackMethods 대체 수단 객체들
     * @return 각 대체 수단이 지칭하는 블록과의 연결을 수립한 바로 연결 응답 객체들
     */
    public Collection<QuickReply> findByFallbackMethod(FallbackMethod... fallbackMethods) {
        String[] blockNames = new String[fallbackMethods.length];
        for (int i = 0; i < fallbackMethods.length; i++)
            blockNames[i] = fallbackMethods[i].getKey();
        return findByName(blockNames);
    }
}
