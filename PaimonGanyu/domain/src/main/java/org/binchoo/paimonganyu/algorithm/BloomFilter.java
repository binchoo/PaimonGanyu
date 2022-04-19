package org.binchoo.paimonganyu.algorithm;

import java.util.Arrays;

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
        for (int h : item.getHashes()) {
            int i = h % filterSize;
            bloomFilter[Math.abs(i)] = true;
        }
    }

    /**
     * @param item 찾을 아이템
     * @return {@code true} 블룸필터에 {@code item}이 "존재하는 것 같을때"
     * <p> {@code false} 블룸필터에 {@code item}이 존재하지 않을 때
     */
    public boolean assumeExists(T item) {
        for (int h : item.getHashes()) {
            int i = h % filterSize;
            if (!bloomFilter[Math.abs(i)])
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(bloomFilter);
    }
}

