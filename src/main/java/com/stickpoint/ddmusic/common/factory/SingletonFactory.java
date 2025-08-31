package com.stickpoint.ddmusic.common.factory;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 单例工厂 - 提供线程安全的单例实例管理
 * @author fntp
 * @since 2023/6/27
 */
@SuppressWarnings("unused")
public final class SingletonFactory {

    /**
     * 私有化构造单例
     */
    private SingletonFactory() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Object> INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    private static final Map<Class, WeakReference<Object>> WEAK_REFERENCE_INSTANCES = new ConcurrentHashMap<>();

    /**
     * 获取强引用单例实例（线程安全，不会被GC回收）
     * @param clazz 类对象
     * @return 单例实例
     * @throws IllegalStateException 当实例创建失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <E> E getInstance(Class<E> clazz) {
        Object instance = INSTANCES.get(clazz);
        if (instance == null) {
            synchronized (SingletonFactory.class) {
                instance = INSTANCES.get(clazz);
                if (instance == null) {
                    instance = createInstance(clazz);
                    INSTANCES.put(clazz, instance);
                }
            }
        }
        return (E) instance;
    }

    /**
     * 获取弱引用单例实例（内存敏感时可能被GC回收）
     * 注意：这可能导致同一类有多个实例，违背单例模式原则，请谨慎使用
     * @param clazz 类对象
     * @return 单例实例
     * @throws IllegalStateException 当实例创建失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <E> E getWeakInstance(Class<E> clazz) {
        WeakReference<Object> reference = WEAK_REFERENCE_INSTANCES.get(clazz);
        Object instance = (reference != null) ? reference.get() : null;

        if (instance == null) {
            synchronized (SingletonFactory.class) {
                reference = WEAK_REFERENCE_INSTANCES.get(clazz);
                instance = (reference != null) ? reference.get() : null;
                if (instance == null) {
                    instance = createInstance(clazz);
                    WEAK_REFERENCE_INSTANCES.put(clazz, new WeakReference<>(instance));
                }
            }
        }
        return (E) instance;
    }

    /**
     * 创建类实例（集中化实例创建逻辑）
     * @param clazz 要实例化的类
     * @return 新创建的实例
     * @throws IllegalStateException 当实例创建失败时抛出
     */
    private static <E> Object createInstance(Class<E> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException e) {
            String errorMsg = String.format("Failed to create instance for class %s: %s",
                    clazz.getName(), e.getMessage());
            throw new IllegalStateException(errorMsg, e);
        }
    }

    /**
     * 清除指定类的单例实例（主要用于测试）
     * @param clazz 要清除的类
     */
    public static void clearInstance(Class<?> clazz) {
        synchronized (SingletonFactory.class) {
            INSTANCES.remove(clazz);
            WEAK_REFERENCE_INSTANCES.remove(clazz);
        }
    }

    /**
     * 清除所有单例实例（主要用于测试）
     */
    public static void clearAll() {
        synchronized (SingletonFactory.class) {
            INSTANCES.clear();
            WEAK_REFERENCE_INSTANCES.clear();
        }
    }
}