package ru.msm.memoizer.reflection;

import ru.msm.memoizer.cache.CacheKey;

import java.util.List;

/**
 * Key for memoization with overloaded hashCode and equals by reflection
 */
public class ReflectiveCacheKey implements CacheKey {

    private static final long serialVersionUID = 3675455378328463567L;

    private List<Object> objects;

    public ReflectiveCacheKey(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public int hashCode() {
        return DeepHashCodeBuilder.reflectionHashCode(objects);
    }

    @Override
    public boolean equals(Object obj) {
        List<Object> object2 = null;
        if (obj != null && obj instanceof ReflectiveCacheKey) {
            object2 = ((ReflectiveCacheKey) obj).getObjects();
        }
        return DeepEqualsBuilder.reflectionEquals(objects, object2);
    }

    public List<Object> getObjects() {
        return objects;
    }
}
