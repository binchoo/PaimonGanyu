package org.binchoo.paimonganyu.error;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
public class FallbackId {

    private final String id;

    public FallbackId(String id) {
        this.id = id;
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
