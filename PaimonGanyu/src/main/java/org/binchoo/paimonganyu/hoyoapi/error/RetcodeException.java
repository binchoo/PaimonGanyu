package org.binchoo.paimonganyu.hoyoapi.error;

import org.binchoo.paimonganyu.hoyoapi.error.annotation.Retcode;

import java.util.HashMap;
import java.util.Map;

public class RetcodeException extends RuntimeException {

    private static final Map<Integer, Class<RetcodeException>> exceptionMapping;

    static {
        exceptionMapping = new HashMap<>();

        for (Class<?> clazz : RetcodeException.class.getClasses()) {
            Retcode retcodeAnnot = clazz.getAnnotation(Retcode.class);

            if (Integer.MIN_VALUE != retcodeAnnot.value())
                exceptionMapping.put(retcodeAnnot.value(), (Class<RetcodeException>) clazz);

            for (int code : retcodeAnnot.codes())
                exceptionMapping.put(code, (Class<RetcodeException>) clazz);
        }
    }

    public static RetcodeException of(int retcode) {
        RetcodeException retcodeException = null;

        if (exceptionMapping.containsKey(retcode)) {
            Class<RetcodeException> clazz = exceptionMapping.get(retcode);
            try {
                retcodeException = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return retcodeException;
    }

    private String message = "";

    public RetcodeException() { }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Retcode(-1)
    public static class ParamsError extends RetcodeException {
    }

    @Retcode(-10001)
    public static class InvalidRequestError extends RetcodeException {
    }

    @Retcode(10101)
    public static class TooManyRequestsError extends RetcodeException {
    }

    @Retcode(codes = {-100, -1071, 10001, 10103})
    public static class NotLoggedInError extends RetcodeException {
    }

    @Retcode(codes = {-10002, 1009})
    public static class AccountNotFoundError extends RetcodeException {
    }

    @Retcode(10102)
    public static class DataNotPublicError extends RetcodeException {
    }

    @Retcode(codes = {-2021, -2018, -2017, -2003, -2001, -1073})
    public static class CodeRedeemException extends RetcodeException {
    }

    @Retcode(codes = {-5003, 2001})
    public static class SignInException extends RetcodeException {
    }
}
