package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;

@Getter
@Builder
@ToString
public class QuickReply {

    private String label;
    @Builder.Default
    private String action = "message";
    private String messageText;
    private String blockId;
    private Object extra;
}
