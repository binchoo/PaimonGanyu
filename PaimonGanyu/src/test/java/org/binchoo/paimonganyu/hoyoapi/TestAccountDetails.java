package org.binchoo.paimonganyu.hoyoapi;

import lombok.Builder;
import lombok.Getter;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;

@Getter
@Builder
public class TestAccountDetails {

    private LtuidLtoken ltuidLtoken;
    private String uid;
    private String region;
}
