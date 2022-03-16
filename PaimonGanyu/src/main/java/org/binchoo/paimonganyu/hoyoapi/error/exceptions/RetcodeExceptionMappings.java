package org.binchoo.paimonganyu.hoyoapi.error.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RetcodeExceptionMappings {

    private static RetcodeExceptionMappings instance = null;

    private final Map<Integer, Class<RetcodeException>> exceptionMappings = new HashMap<>();

    public static RetcodeExceptionMappings getInstance() {
        if (instance == null)
            instance = new RetcodeExceptionMappings();
        return instance;
    }

    public void addMapping(int retcode, Class<RetcodeException> retcodeExceptionClass) {
        exceptionMappings.put(retcode, retcodeExceptionClass);
    }

    public Class<RetcodeException> getMapping(int retcode) {
        return exceptionMappings.get(retcode);
    }

    public void deleteMapping(int retcode) {
        exceptionMappings.remove(retcode);
    }

    public boolean contains(int retcode) {
        return exceptionMappings.containsKey(retcode);
    }

    public RetcodeException getMappingClass(int retcode) {
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

    @Override
    public String toString() {
        return this.exceptionMappings.toString();
    }

    public Set<Map.Entry<Integer, Class<RetcodeException>>> entrySet() {
        return this.exceptionMappings.entrySet();
    }
}
