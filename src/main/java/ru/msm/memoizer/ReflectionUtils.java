package ru.msm.memoizer;

import org.apache.commons.lang.StringUtils;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReflectionUtils {

    private static final ConcurrentMap<Class<?>, Field[]> fieldsCache = new ConcurrentHashMap<>();

    public static Field[] getAllFields(Class<?> clazz) {
        Field[] fields = fieldsCache.get(clazz);
        if (fields == null) {
            List<Field> resultList = new ArrayList<>();

            putAllDeclaredFields(resultList, clazz);
            Class<?> searchClass = clazz;
            while (searchClass.getSuperclass() != null) {
                searchClass = searchClass.getSuperclass();
                putAllDeclaredFields(resultList, searchClass);
            }
            Field[] result = new Field[resultList.size()];
            fields = resultList.toArray(result);
            Field[] oldFields = fieldsCache.putIfAbsent(clazz, fields);
            if (oldFields != null) {
                fields = oldFields;
            }
        }
        return fields;
    }

    public static Object getValueForField(Field field, Object object) {
        if (object == null) {
            return null;
        }
        Object result;
        if (field.isAccessible()) {
            result = safeGetValue(field, object);
        } else {
            field.setAccessible(true);
            result = safeGetValue(field, object);
            //field.setAccessible(false);
        }
        return result;
    }

    public static Object getValueForField(String fieldName, Object object) {
        return getValueForField(fieldName, object, false);
    }

    /**
     * Возваращет значение поля объекта по его имени. Возвращает null в случае, если такого поля нету,
     * и в случае, если значение поля null.
     *
     * @param object    объект для исследования
     * @param fieldName название поля
     * @return
     */
    public static Object getValueForField(String fieldName, Object object, Boolean ignoreCase) {
        Field field = getFieldByName(object, fieldName, ignoreCase);
        if (field == null) {
            return null;
        }
        return getValueForField(field, object);
    }

    public static boolean setValueForField(Field field, Object object, Object value) {
        if (object == null) {
            return false;
        }
        if (field.isAccessible()) {
            return safeSetValue(field, object, value);
        } else {
            field.setAccessible(true);
            return safeSetValue(field, object, value);
            //field.setAccessible(false);
        }
    }

    public static boolean setValueForField(String fieldName, Object object, Object value) {
        return setValueForField(fieldName, object, value, false);
    }

    /**
     * Изменняет значение поля объекта по его имени. Возвращает false в случае, если такого поля нету,
     *
     * @param object    объект для исследования
     * @param fieldName название поля
     * @param value     новое значение
     * @return true если поле существует
     */
    public static boolean setValueForField(String fieldName, Object object, Object value, Boolean ignoreCase) {
        Field field = getFieldByName(object, fieldName, ignoreCase);
        if (field == null) {
            return false;
        }
        return setValueForField(field, object, value);
    }

    public static boolean isPrimitiveType(Object value) {
        return value instanceof String
                || value instanceof Number
                || value instanceof Date
                || value instanceof Boolean
                || value instanceof long[]
                || value instanceof int[]
                || value instanceof short[]
                || value instanceof char[]
                || value instanceof byte[]
                || value instanceof double[]
                || value instanceof float[]
                || value instanceof boolean[]
                ;
    }

    public static boolean isCollectionType(Object value) {
        return value instanceof Collection;
    }

    public static boolean isMapType(Object value) {
        return value instanceof Map;
    }

    public static boolean isArray(Object value) {
        return value != null
                && value.getClass().isArray();
    }

//PRIVATE-----------------------------------------------------------------

    private static Field getFieldByName(Object object, String name, Boolean ignoreCase) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (Field field : getAllFields(object.getClass())) {
            if (ignoreCase && name.equalsIgnoreCase(field.getName())) {
                return field;
            } else if (name.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    private static void putAllDeclaredFields(List<Field> fieldsList, Class clazz) {
        Field[] allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            if (!Modifier.isStatic(field.getModifiers()) && !isTransient(field)) {
                fieldsList.add(field);
            }
        }
    }

    private static boolean isTransient(Field field) {
        Annotation[] allAnnotations = field.getDeclaredAnnotations();
        for (Annotation annotation : allAnnotations) {
            if (annotation instanceof Transient) {
                return true;
            }
        }
        return field.isSynthetic() || Modifier.isTransient(field.getModifiers());
    }

    private static Object safeGetValue(Field field, Object object) {
        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static boolean safeSetValue(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
