package ru.msm.memoizer;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Contains hashCode implementation. It goes recursively throw all domain tree, and return the same hashCode
 * only if all primitive types are equals. This implementation do not require to override equals and hash code
 * methods for all domain entities. It used only hashCode for primitive types.
 */
public class DeepHashCodeBuilder implements Serializable {

    public static int reflectionHashCode(Object object) {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = processValue(builder, object);
        return builder.toHashCode();
    }

//PRIVATE ----------------------------------------------------------------

    private static HashCodeBuilder buildHashCode(Object object, HashCodeBuilder builder) {
        if (object == null) {
            return builder;
        }
        if (ReflectionUtils.isPrimitiveType(object)) {
            return builder.append(object);
        }
        if (ReflectionUtils.isCollectionType(object)) {
            return processValue(builder, object);
        }
        Field[] allFields = ReflectionUtils.getAllFields(object.getClass());

        for (Field field : allFields) {
            Object value = ReflectionUtils.getValueForField(field, object);
            builder = processValue(builder, value);
        }
        return builder;
    }

    private static HashCodeBuilder processValue(HashCodeBuilder builder, Object value) {
        if (ReflectionUtils.isPrimitiveType(value)) {
            builder = builder.append(value);
        } else if (ReflectionUtils.isCollectionType(value)) {
            Collection collection = (Collection) value;
            for (Object child : collection) {
                builder = buildHashCode(child, builder);
            }
        } else if (ReflectionUtils.isArray(value)) {
                Object[] array = (Object[]) value;
                for (Object child : array) {
                    builder = buildHashCode(child, builder);
                }
        } else if (ReflectionUtils.isMapType(value)) {
            Map map = (Map) value;
            for (Object child : map.entrySet()) {
                Map.Entry entry = (Map.Entry) child;
                builder = buildHashCode(entry.getKey(), builder);
                builder = buildHashCode(entry.getValue(), builder);
            }
        } else {
            builder = buildHashCode(value, builder);
        }
        return builder;
    }
}
