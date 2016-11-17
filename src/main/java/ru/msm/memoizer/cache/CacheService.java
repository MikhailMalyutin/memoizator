package ru.msm.memoizer.cache;

import java.io.Serializable;

public interface CacheService<T extends Serializable> {
    T writeToCache(CacheKey cacheKey, T object);

    T readFromCache(CacheKey cacheKey);
}
