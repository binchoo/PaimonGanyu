package org.binchoo.paimonganyu.lambda.hoyopass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LtuidLtoken {

    private String ltuid;
    private String ltoken;
}