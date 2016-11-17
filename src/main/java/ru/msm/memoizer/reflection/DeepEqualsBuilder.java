package ru.msm.memoizer.reflection;

import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains equals implementation. It goes recursively throw all domain tree, and returns true only if all
 * primitive types are equals. This implementation do not require to override equals and hash code
 * methods for all domain entities. It uses only equals for primitive types
 */
public class DeepEqualsBuilder {

    public static boolean reflectionEquals(Object object1, Object object2) {
        EqualsBuilder builder = new EqualsBuilder();
        builder = processValues(builder, object1, object2);
        return builder.isEquals();
    }

//PRIVATE ----------------------------------------------------------------

    private static EqualsBuilder buildEquals(Object object1, Object object2, EqualsBuilder builder) {
        if (!builder.isEquals()) {
            return builder;
        }
        if (object1 == null || object2 == null) {
            return builder.append(object1, object2);
        }
        if (ReflectionUtils.isPrimitiveType(object1)) {
            return builder.append(object1, object2);
        }
        if (ReflectionUtils.isCollectionType(object1)) {
            return processCollection(object1, object2, builder);
        }
        Field[] allFields = ReflectionUtils.getAllFields(object1.getClass());

        for (Field field : allFields) {
            Object value1 = ReflectionUtils.getValueForField(field, object1);
            Object value2 = ReflectionUtils.getValueForField(field, object2);
            builder = processValues(builder, value1, value2);
        }
        return builder;
    }

//PRIVATE-----------------------------------------------------------------

    private static EqualsBuilder processValues(EqualsBuilder builder, Object value1, Object value2) {
        if (ReflectionUtils.isPrimitiveType(value1)) {
            builder = builder.append(value1, value2);
        } else if (ReflectionUtils.isCollectionType(value1)) {
            builder = processCollection(value1, value2, builder);
        } else if (ReflectionUtils.isArray(value1)) {
            builder = processArray(value1, value2, builder);
        } else if (ReflectionUtils.isMapType(value1)) {
            builder = processMap(value1, value2, builder);
        } else {
            builder = buildEquals(value1, value2, builder);
        }
        return builder;
    }

    private static EqualsBuilder processCollection(Object value1, Object value2, EqualsBuilder builder) {
        if (!ReflectionUtils.isCollectionType(value2)) {
            builder = builder.append(value1, value2);
            if (!builder.isEquals()) {
                return builder;
            }
        }
        Collection collection1 = (Collection) value1;
        Collection collection2 = (Collection) value2;

        if (collection1.size() != collection2.size()) {
            builder = builder.append(value1, value2);
            if (!builder.isEquals()) {
                return builder;
            }
        }

        Iterator it1 = collection1.iterator();
        Iterator it2 = collection2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Object child1 = it1.next();
            Object child2 = it2.next();
            builder = buildEquals(child1, child2, builder);
        }
        return builder;
    }

    private static EqualsBuilder processArray(Object value1, Object value2, EqualsBuilder builder) {
        if (!ReflectionUtils.isArray(value2)) {
            builder = builder.append(value1, value2);
            if (!builder.isEquals()) {
                return builder;
            }
        }
        Object[] array1 = (Object[]) value1;
        Object[] array2 = (Object[]) value2;

        if (array1.length != array2.length) {
            builder = builder.append(value1, value2);
            if (!builder.isEquals()) {
                return builder;
            }
        }
        for (int i = 0; i < array1.length; ++i) {
            builder = buildEquals(array1[i], array2[i], builder);
        }
        return builder;
    }

    private static EqualsBuilder processMap(Object value1, Object value2, EqualsBuilder builder) {
        if (!ReflectionUtils.isMapType(value2)) {
            builder = builder.append(value1, value2);
            if (!builder.isEquals()) {
                return builder;
            }
        }
        Map map1 = (Map) value1;
        Map map2 = (Map) value2;

        if (map1.size() != map2.size()) {
            builder = builder.append(value1, value2);
            if (!builder.isEquals()) {
                return builder;
            }
        }

        Iterator it1 = map1.entrySet().iterator();
        Iterator it2 = map2.entrySet().iterator();

        while (it1.hasNext() && it2.hasNext()) {
            Map.Entry child1 = (Map.Entry) it1.next();
            Map.Entry child2 = (Map.Entry) it2.next();

            builder = buildEquals(child1.getKey(), child2.getKey(), builder);
            builder = buildEquals(child1.getValue(), child2.getValue(), builder);
        }
        return builder;
    }
}
