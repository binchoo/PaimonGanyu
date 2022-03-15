package org.binchoo.paimonganyu.hoyoapi.error.exceptions;

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
        bootstrapRetcodeExceptions();
    }

    private static void bootstrapRetcodeExceptions() {
        try {
            Class.forName(RetcodeExceptionBootstrap.className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static RetcodeException of(int retcode) {
        RetcodeExceptionMappings exceptionMappings = RetcodeExceptionMappings.getInstance();
        return exceptionMappings.getMappingClass(retcode);
    }
}
