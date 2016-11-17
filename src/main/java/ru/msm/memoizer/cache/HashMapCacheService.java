package ru.msm.memoizer.cache;

import java.io.Serializable;
import java.util.HashMap;

public class HashMapCacheService<T extends Serializable> implements CacheService<T> {
    HashMap<CacheKey, T> map = new HashMap<>();
    @Override
    public T writeToCache(CacheKey cacheKey, T object) {
        map.put(cacheKey, object);
        return object;
    }

    @Override
    public T readFromCache(CacheKey cacheKey) {
        return map.get(cacheKey);
    }
}
