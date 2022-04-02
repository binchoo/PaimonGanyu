package org.binchoo.paimonganyu.hoyoapi.error;

import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;

import java.util.Optional;

public class RetcodeException extends RuntimeException {

    private String message = "";

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    static {
        RetcodeExceptionBootstrapper.start();
    }

    public static Optional<RetcodeException> findMapping(HoyoResponse<?> response) {
        return RetcodeExceptionMappings.getInstance()
                .createException(response.getRetcode(), response.getMessage());
    }
}
