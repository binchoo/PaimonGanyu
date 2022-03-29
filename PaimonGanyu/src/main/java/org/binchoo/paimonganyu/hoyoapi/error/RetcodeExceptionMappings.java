package org.binchoo.paimonganyu.hoyoapi.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RetcodeExceptionMappings {

    private static RetcodeExceptionMappings instance = null;

    private final Map<Integer, Class<RetcodeException>> exceptionMappings = new HashMap<>();

    public RetcodeException newExceptionInstance(int retcode) {
        RetcodeException retcodeException = null;
        if (this.contains(retcode)) {
            Class<RetcodeException> clazz = this.getMapping(retcode);
            try {
                retcodeException = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return retcodeException;
    }

    public Class<RetcodeException> getMapping(int retcode) {
        return exceptionMappings.get(retcode);
    }

    public void addMapping(int retcode, Class<RetcodeException> retcodeExceptionClass) {
        exceptionMappings.put(retcode, retcodeExceptionClass);
    }

    public void deleteMapping(int retcode) {
        exceptionMappings.remove(retcode);
    }

    public boolean contains(int retcode) {
        return exceptionMappings.containsKey(retcode);
    }

    public Set<Map.Entry<Integer, Class<RetcodeException>>> entrySet() {
        return this.exceptionMappings.entrySet();
    }

    @Override
    public String toString() {
        return this.exceptionMappings.toString();
    }

    public static RetcodeExceptionMappings getInstance() {
        if (instance == null)
            instance = new RetcodeExceptionMappings();
        return instance;
    }
}
