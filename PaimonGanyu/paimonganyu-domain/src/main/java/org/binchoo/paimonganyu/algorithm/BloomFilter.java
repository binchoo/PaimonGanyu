package org.binchoo.paimonganyu.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @param <T> 블룸필터에 삽입하는 아이템 타입
 */
@Slf4j
public class BloomFilter<T extends MultiHashable> {

    private final int filterSize;
    private final boolean[] flags;

    public BloomFilter(int filterSize) {
        this.filterSize = filterSize;
        this.flags = new boolean[filterSize];
    }

    public void insert(T item) {
        for (int h : item.getHashes()) {
            int i = Math.abs(h % filterSize);
            flags[i] = true;
        }
    }

    /**
     * @param item 찾을 아이템
     * @return {@code true} 블룸필터에 {@code item}이 "존재하는 것 같을때"
     * <p> {@code false} 블룸필터에 {@code item}이 존재하지 않을 때
     */
    public boolean probablyContains(T item) {
        for (int h : item.getHashes()) {
            int i = Math.abs(h % filterSize);
            if (!flags[i]) {
                log.debug("Item {} does not exist.", item);
                return false;
            }
        }
        log.debug("Item {} seems to exist.", item);
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(flags);
    }
}

