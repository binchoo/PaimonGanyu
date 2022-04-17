package org.binchoo.paimonganyu.algorithm;

/**
 * @param <T> 블룸필터에 삽입하는 아이템 타입
 */
public class BloomFilter<T extends MultiHashable> {

    private final int filterSize;
    private boolean[] bloomFilter;

    public BloomFilter(int filterSize) {
        this.filterSize = filterSize;
        this.bloomFilter = new boolean[filterSize];
    }

    public void insert(T item) {
        if (!assumeExists(item)) {
            for (int h : item.getHashes()) {
                int i = h % filterSize;
                bloomFilter[Math.abs(i)] = true;
            }
        }
    }

    public boolean assumeExists(T item) {
        for (int h : item.getHashes()) {
            int i = h % filterSize;
            if (!bloomFilter[Math.abs(i)])
                return false;
        }
        return true;
    }
}

