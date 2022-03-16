package org.binchoo.paimonganyu.hoyoapi.response;

import lombok.Getter;

@Getter
public final class HoyoResponse<T> {

    private int retcode;
    private String message;
    private T data;
}
