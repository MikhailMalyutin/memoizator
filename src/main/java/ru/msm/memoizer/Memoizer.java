package ru.msm.memoizer;

import ru.msm.memoizer.cache.CacheKey;
import ru.msm.memoizer.cache.CacheService;
import ru.msm.memoizer.cache.NullValue;
import ru.msm.memoizer.cache.SimpleCacheKey;
import ru.msm.memoizer.reflection.ReflectiveCacheKey;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Memoizer {
    private CacheService cacheService;

    public Memoizer(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public Object memoize(Function f, Object arg, boolean cacheNull, boolean overrideHashCode) throws Throwable {
        String signatureString    = f.toString();
        Serializable result;
        Object[] args = new Object[1];
        args[0] = arg;
        CacheKey cacheKey = constructCacheKey(args, signatureString, overrideHashCode);
        synchronized (signatureString.intern()) {
            result = cacheService.readFromCache(cacheKey);
            if (result != null) {
                if (result instanceof NullValue) {
                    return null;
                }
                return result;
            }
            result = (Serializable) f.apply(arg);
            if (result == null && cacheNull) {
                cacheService.writeToCache(cacheKey, new NullValue());
            } else if (result != null) {
                cacheService.writeToCache(cacheKey, result);
            }
        }
        return result;
    }

    private CacheKey constructCacheKey(Object[] point, String cacheKey, boolean overrideHashCode) {
        List<Object> args = Arrays.stream(point).collect(Collectors.toList());
        if (overrideHashCode) {
            return buildReflectiveCacheKey(cacheKey, args);
        }
        return buildSimpleCacheKey(cacheKey, args);
    }

    private CacheKey buildSimpleCacheKey(String cacheKey, List<Object> args) {
        List<Object> key = new ArrayList<>();
        key.add(cacheKey);
        key.addAll(args);
        return new ReflectiveCacheKey(key);
    }

    private CacheKey buildReflectiveCacheKey(String cacheKey, List<Object> args) {
        List<Object> key = new ArrayList<>();
        key.add(cacheKey);
        key.addAll(args);
        return new SimpleCacheKey(key);
    }
}
