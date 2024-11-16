package com.agyer.windmill.core.os;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@SuppressLint("PrivateApi")
public final class SystemPropertiesCompat {
    private static final String TAG = SystemPropertiesCompat.class.getSimpleName();
    private static final boolean DEBUG = false;

    @Nullable
    private static final Class<?> propertyClass;

    static {
        Class<?> aClass;
        try {
            aClass = Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            aClass = null;
        }
        propertyClass = aClass;
    }

    private SystemPropertiesCompat() {

    }

    @CheckResult
    public static boolean getBoolean(@NonNull String propertyName, boolean defaultValue) {
        return get(propertyName, defaultValue, "getBoolean", boolean.class);
    }

    @CheckResult
    public static boolean getBoolean(@NonNull String propertyName) {
        return getBoolean(propertyName, false);
    }

    @CheckResult
    public static int getInt(@NonNull String propertyName, int defaultValue) {
        return get(propertyName, defaultValue, "getInt", int.class);
    }

    @CheckResult
    public static int getInt(@NonNull String propertyName) {
        return getInt(propertyName, 0);
    }

    @CheckResult
    public static long getLong(@NonNull String propertyName, long defaultValue) {
        return get(propertyName, defaultValue, "getLong", long.class);
    }

    @CheckResult
    public static long getLong(@NonNull String propertyName) {
        return getLong(propertyName, 0L);
    }

    @NonNull
    @CheckResult
    public static String get(@NonNull String propertyName, @NonNull String defaultValue) {
        return get(propertyName, defaultValue, "get", String.class);
    }

    @NonNull
    @CheckResult
    public static String get(@NonNull String propertyName) {
        return get(propertyName, "");
    }

    @NonNull
    @CheckResult
    private static <T> T get(@NonNull String propertyName, @NonNull T defaultValue, @NonNull String methodName, @NonNull Class<T> type) {
        try {
            return tryGet(propertyName, defaultValue, methodName, type);
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "unable to get property \"" + propertyName + "\", default value " + defaultValue + " returned. " + e);
        }
        return defaultValue;
    }

    /**
     * @throws UnsupportedOperationException reflection failed
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @CheckResult
    public static <T> T tryGet(@NonNull String propertyName, @NonNull T defaultValue, @NonNull String methodName, @NonNull Class<T> type) throws UnsupportedOperationException {
        Throwable throwable = null;
        if (propertyClass != null) {
            try {
                Method method = propertyClass.getDeclaredMethod(methodName, String.class, type);
                method.setAccessible(true);
                return (T) Objects.requireNonNull(method.invoke(null, propertyName, defaultValue));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                     ClassCastException | NullPointerException /*npe of unboxing*/ e) {
                throwable = e;
            }
        }
        throw new UnsupportedOperationException("unable to get property \"" + propertyName + "\"", throwable);
    }

}
