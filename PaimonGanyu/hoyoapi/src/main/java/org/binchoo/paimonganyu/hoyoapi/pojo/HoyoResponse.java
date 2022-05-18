package org.binchoo.paimonganyu.hoyoapi.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class HoyoResponse<T> {

    private int retcode;
    private String message;
    private T data;
}
