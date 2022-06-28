package org.binchoo.paimonganyu.ikakao.type.subtype;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Link {

    private String mobile;
    private String ios;
    private String android;
    private String pc;
    private String mac;
    private String win;
    private String web;
}
