package org.binchoo.paimonganyu.hoyopass.api.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class HoyoResponse<T> {

    private int retcode;
    private String message;
    private T data;
}
