package org.binchoo.paimonganyu.error;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
public class FallbackMethod {

    private final String id;

    public FallbackMethod(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return id.equals(o);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
