package ru.cod331n.coloriot.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class ReflectionUtils {
    /**
     * Возвращает значение приватного поля для заданного экземпляра.
     * 
     * @param instance Экземпляр объекта
     * @param fieldName Имя поля
     * @return Значение поля
     * @throws NoSuchFieldException если поле не найдено
     * @throws IllegalAccessException если доступ к полю невозможен
     */
    public static Object getPrivateField(@NotNull Object instance, @NotNull String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}
