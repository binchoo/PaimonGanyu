package org.binchoo.paimonganyu.hoyoapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountIdCookieToken {

    private String accountId;
    private String cookieToken;
}
