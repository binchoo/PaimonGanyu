package org.binchoo.paimonganyu.hoyoapi.error;

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

    public static RetcodeException of(int retcode) {
        RetcodeExceptionMappings exceptionMappings = RetcodeExceptionMappings.getInstance();
        return exceptionMappings.newExceptionInstance(retcode);
    }
}
