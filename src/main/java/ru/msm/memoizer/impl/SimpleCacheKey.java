package ru.msm.memoizer.impl;

import java.util.List;

public class SimpleCacheKey implements CacheKey {
    private List<Object> objects;

    public SimpleCacheKey(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleCacheKey)) return false;

        SimpleCacheKey that = (SimpleCacheKey) o;

        if (objects != null ? !objects.equals(that.objects) : that.objects != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return objects != null ? objects.hashCode() : 0;
    }

    public List<Object> getObjects() {
        return objects;
    }
}
