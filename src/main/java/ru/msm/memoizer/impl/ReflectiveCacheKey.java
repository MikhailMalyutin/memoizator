package ru.msm.memoizer.impl;

import java.util.List;

/**
 * Ключ кеширования, у которого переопределены hashCode, equals таким образом, что пробегают
 * по всем нетранзиентным полям и всем вложенным объектам
 */
public class ReflectiveCacheKey implements CacheKey {

    private static final long serialVersionUID = 3675455378328463794L;

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
