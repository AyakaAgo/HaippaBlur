package com.agyer.windmill.core.miui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.agyer.windmill.core.os.SystemPropertiesCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @apiNote from miuix.core.util.MiuiBlurUtils.
 * some methods not public due to unknown arg/usages
 */
@SuppressWarnings("PrivateApi")
public class MiuiViewBlurUtil {
    /**
     * disable blur
     */
    public static final int BLUR_MODE_NONE = 0;
    /**
     * blur underlying views
     */
    public static final int BLUR_MODE_BACKGROUND = 1;
    /**
     * blur in view content's shape
     */
    public static final int BLUR_MODE_CONTENT_SHAPE = 2;
    /**
     * blur in view content's shape but ignores view content's color,
     * <p>
     * ignores color doesn't mean don't need color
     * <p>
     * should call {@link #addBackgroundBlendColor(View, int, int)} to
     * ensure readability
     */
    public static final int BLUR_MODE_CONTENT_SHAPE_IGNORES_COLOR = 3;

    public static final int BACKGROUND_BLUR_MODE_NONE = 0;
    public static final int BACKGROUND_BLUR_MODE_BACKGROUND = 1;
    public static final int BACKGROUND_BLUR_MODE_FOREGROUND = 2;

    /**
     * @see android.graphics.BlendMode#CLEAR
     */
    public static final int BLEND_MODE_CLEAR = 0;
    /**
     * @see android.graphics.BlendMode#SRC
     */
    public static final int BLEND_MODE_SRC = 1;
    /**
     * @see android.graphics.BlendMode#DST
     */
    public static final int BLEND_MODE_DST = 2;
    /**
     * @see android.graphics.BlendMode#SRC_OVER
     */
    public static final int BLEND_MODE_SRC_OVER = 3;
    /**
     * @see android.graphics.BlendMode#DST_OVER
     */
    public static final int BLEND_MODE_DST_OVER = 4;
    /**
     * @see android.graphics.BlendMode#SRC_IN
     */
    public static final int BLEND_MODE_SRC_IN = 5;
    /**
     * @see android.graphics.BlendMode#DST_IN
     */
    public static final int BLEND_MODE_DST_IN = 6;
    /**
     * @see android.graphics.BlendMode#SRC_OUT
     */
    public static final int BLEND_MODE_SRC_OUT = 7;
    /**
     * @see android.graphics.BlendMode#DST_OUT
     */
    public static final int BLEND_MODE_DST_OUT = 8;
    /**
     * @see android.graphics.BlendMode#SRC_ATOP
     */
    public static final int BLEND_MODE_SRC_ATOP = 9;
    /**
     * @see android.graphics.BlendMode#DST_ATOP
     */
    public static final int BLEND_MODE_DST_ATOP = 10;
    /**
     * @see android.graphics.BlendMode#XOR
     */
    public static final int BLEND_MODE_XOR = 11;
    /**
     * @see android.graphics.BlendMode#PLUS
     */
    public static final int BLEND_MODE_PLUS = 12;
    /**
     * same as "plus" mode
     *
     * @see #BLEND_MODE_PLUS
     * @see android.graphics.BlendMode#PLUS
     * @see android.graphics.PorterDuff.Mode#ADD
     */
    @BlendMode
    public static final int BLEND_MODE_ADD = BLEND_MODE_PLUS;
    /**
     * @see android.graphics.BlendMode#MODULATE
     */
    public static final int BLEND_MODE_MODULATE = 13;
    /**
     * @see android.graphics.BlendMode#SCREEN
     */
    public static final int BLEND_MODE_SCREEN = 14;
    /**
     * @see android.graphics.BlendMode#OVERLAY
     */
    public static final int BLEND_MODE_OVERLAY = 15;
    /**
     * @see android.graphics.BlendMode#DARKEN
     */
    public static final int BLEND_MODE_DARKEN = 16;
    /**
     * @see android.graphics.BlendMode#LIGHTEN
     */
    public static final int BLEND_MODE_LIGHTEN = 17;
    /**
     * system will adjust transparency
     *
     * @see android.graphics.BlendMode#COLOR_DODGE
     */
    public static final int BLEND_MODE_COLOR_DODGE /*BLEND_MODE_COLOR_MIXING_LIGHTEN*/ = 18;
    /**
     * system will adjust transparency
     *
     * @see android.graphics.BlendMode#COLOR_BURN
     */
    public static final int BLEND_MODE_COLOR_BURN /*BLEND_MODE_COLOR_MIXING*/ = 19;
    /**
     * @see android.graphics.BlendMode#HARD_LIGHT
     */
    public static final int BLEND_MODE_HARD_LIGHT = 20;
    /**
     * @see android.graphics.BlendMode#SOFT_LIGHT
     */
    public static final int BLEND_MODE_SOFT_LIGHT = 21;
    /**
     * @see android.graphics.BlendMode#DIFFERENCE
     */
    public static final int BLEND_MODE_DIFFERENCE = 22;
    /**
     * @see android.graphics.BlendMode#EXCLUSION
     */
    public static final int BLEND_MODE_EXCLUSION = 23;
    /**
     * @see android.graphics.BlendMode#MULTIPLY
     * @apiNote this different with {@link #BLEND_MODE_MODULATE}, {@link PorterDuff.Mode#MULTIPLY}
     */
    public static final int BLEND_MODE_MULTIPLY = 24;
    /**
     * @see android.graphics.BlendMode#HUE
     */
    public static final int BLEND_MODE_HUE = 25;
    /**
     * @see android.graphics.BlendMode#SATURATION
     */
    public static final int BLEND_MODE_SATURATION = 26;
    /**
     * @see android.graphics.BlendMode#COLOR
     */
    public static final int BLEND_MODE_COLOR = 27;
    /**
     * @see android.graphics.BlendMode#LUMINOSITY
     */
    public static final int BLEND_MODE_LUMINOSITY = 28;
    /**
     * unknown behaviour
     * <p>
     * known: preserve transparency
     */
    public static final int BLEND_MODE_COLOR_MIXING_PRESERVE_TRANSPARENCY = 100;
    /**
     * unknown behaviour
     * <p>
     * known: in some case shows grayscale colors or transparent
     */
    public static final int BLEND_MODE_COLOR_MIXING = 106;

    private static final String TAG = MiuiViewBlurUtil.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final boolean BACKGROUND_BLUR_SUPPORTED = SystemPropertiesCompat.getBoolean("persist.sys.background_blur_supported");

    private static final Map<BackgroundBlurSettingsObserver, Context> backgroundBlurListeners = new HashMap<>();
    private static final String BACKGROUND_BLUR_ENABLED_CONFIG = "background_blur_enable";

    private MiuiViewBlurUtil() {
    }

    /**
     * check is background blur supported
     *
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    public static boolean isBackgroundBlurSupported() {
        return BACKGROUND_BLUR_SUPPORTED;
    }

    private static boolean hasSystemSetting(@NonNull Context context) {
        try {
            Settings.Secure.getInt(context.getContentResolver(), BACKGROUND_BLUR_ENABLED_CONFIG);
            return true;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    /**
     * check contains setting in system
     *
     * @return if true, you can use {@link OnBackgroundBlurChangedListener}
     * @see #addOnBackgroundBlurChangedListener(Context, OnBackgroundBlurChangedListener)
     * @see #addOnBackgroundBlurChangedListener(Context, Handler, OnBackgroundBlurChangedListener)
     * @see #removeOnBackgroundBlurChangedListener(OnBackgroundBlurChangedListener)
     */
    @CheckResult
    public static boolean isBackgroundBlurListenerSupported(@NonNull Context context) {
        return BACKGROUND_BLUR_SUPPORTED && hasSystemSetting(context);
    }

    /**
     * to follow system settings, you should listen for changes to remove or add blur effect
     * <p>
     * disable "enhanced textures" in system settings will not auto remove blur effect
     */
    public static void addOnBackgroundBlurChangedListener(@NonNull Context context, @NonNull Handler handler, @NonNull OnBackgroundBlurChangedListener listener) {
        for (BackgroundBlurSettingsObserver observer : backgroundBlurListeners.keySet()) {
            if (observer.listener.equals(listener)) {
                return;
            }
        }

        new BackgroundBlurSettingsObserver(handler, listener).register(context);
    }

    public static void addOnBackgroundBlurChangedListener(@NonNull Context context, @NonNull OnBackgroundBlurChangedListener listener) {
        addOnBackgroundBlurChangedListener(context, new Handler(context.getMainLooper()), listener);
    }

    /**
     * remove listener to avoid context leaks
     */
    public static void removeOnBackgroundBlurChangedListener(@NonNull OnBackgroundBlurChangedListener listener) {
        for (BackgroundBlurSettingsObserver observer : backgroundBlurListeners.keySet()) {
            if (observer.listener.equals(listener)) {
                observer.unregister();
                break;
            }
        }
    }

    /**
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    public static boolean setBackgroundBlur(@NonNull View view, @IntRange(from = 0, to = 400) int radius) {
        return setBackgroundBlur(view, radius, false);
    }

    /**
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    public static boolean setBackgroundBlur(@NonNull View view, @IntRange(from = 0, to = 400) int radius, boolean blurOnContent) {
        return setBackgroundBlur(view, radius, blurOnContent ? MiuiViewBlurUtil.BLUR_MODE_CONTENT_SHAPE : MiuiViewBlurUtil.BLUR_MODE_BACKGROUND);
    }

    /**
     * enable/disable view blur
     * <p>
     * this method does the following:
     * <ol>
     * <li><strong>set background blur mode to {@link #BACKGROUND_BLUR_MODE_BACKGROUND}</strong></li>
     * <li>set background blur radius to {@code radius}</li>
     * <li>set view blur mode to {@code blurMode}</li>
     * </ol>
     *
     * @apiNote from miuix.core.util.MiuiBlurUtils
     * @see #setBlurMode(View, int)
     * @see #setBackgroundBlurRadius(View, int)
     * @see #setBackgroundBlurMode(View, int)
     */
    public static boolean setBackgroundBlur(@NonNull View view, @IntRange(from = 0, to = 400) int radius, @BlurMode int blurMode) {
        Context viewContext = view.getContext();

        if (DEBUG) {
            Log.i(TAG, "blur supported " + BACKGROUND_BLUR_SUPPORTED + " enabled " + isBackgroundBlurEnabled(viewContext) + " has setting " + hasSystemSetting(viewContext));
        }

        if (!isBackgroundBlurEnabled(viewContext)) {
            return false;
        }

        try {
            tryCall("setMiBackgroundBlurMode", view, new Class[]{int.class}, new Object[]{BACKGROUND_BLUR_MODE_BACKGROUND});
            tryCall("setMiBackgroundBlurRadius", view, new Class[]{int.class}, new Object[]{radius});
            return setBlurMode(view, blurMode);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            Log.e(TAG, "failed to setBackgroundBlur " + e);
            return false;
        }
    }

    /**
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    @CheckResult
    public static boolean isBackgroundBlurEnabled(@NonNull Context context) {
        return BACKGROUND_BLUR_SUPPORTED && Settings.Secure.getInt(context.getContentResolver(), BACKGROUND_BLUR_ENABLED_CONFIG, 0) == 1;
    }

    /**
     * set view background blur mode
     *
     * @apiNote from miuix.core.util.MiuiBlurUtils
     * @see #setBlurMode(View, int)
     */
    public static boolean setBackgroundBlurMode(@NonNull View view, @BackgroundBlurMode int mode) {
        return BACKGROUND_BLUR_SUPPORTED && call("setMiBackgroundBlurMode", view, new Class[]{int.class}, new Object[]{mode});
    }

    static boolean setBackgroundBlurEnhanceFlag(@NonNull View view, int flag, int mask) {
        return BACKGROUND_BLUR_SUPPORTED && call("setMiBackgroundBlurEnhanceFlag", view, new Class[]{int.class, int.class}, new Object[]{flag, mask});
    }

    public static boolean setBackgroundBlurRadius(@NonNull View view, @IntRange(from = 0, to = 400) int radius) {
        return BACKGROUND_BLUR_SUPPORTED && call("setMiBackgroundBlurRadius", view, new Class[]{int.class}, new Object[]{radius});
    }

    static boolean setBackgroundBlurScaleRatio(@NonNull View view, float scaleRatio) {
        return BACKGROUND_BLUR_SUPPORTED && call("setMiBackgroundBlurScaleRatio", view, new Class[]{float.class}, new Object[]{scaleRatio});
    }

    static boolean setBackgroundBlurEnhanceFlag(@NonNull View view, float flag) {
        return BACKGROUND_BLUR_SUPPORTED && call("setMiBackgroundBlurEnhanceFlag", view, new Class[]{float.class}, new Object[]{flag});
    }

    private static void tryCall(@NonNull String methodName, @NonNull View view, @Nullable Class<?>[] paramTypes, @Nullable Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = View.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        method.invoke(view, args);
    }

    @CheckResult
    @SuppressLint("PrivateApi")
    private static boolean call(@NonNull String methodName, @NonNull View view, @Nullable Class<?>[] paramTypes, @Nullable Object[] args) {
        try {
            tryCall(methodName, view, paramTypes, args);
            return true;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "failed to " + methodName + " " + e);
            return false;
        }
    }

    /**
     * disable view blur
     *
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    public static boolean clearBackgroundBlur(@NonNull View view) {
        return setBackgroundBlurMode(view, BACKGROUND_BLUR_MODE_NONE) && setBlurMode(view, BLUR_MODE_NONE);
    }

    /**
     * set view blur mode
     *
     * @apiNote from miuix.core.util.MiuiBlurUtils
     * @see #setBackgroundBlurMode(View, int)
     */
    public static boolean setBlurMode(@NonNull View view, @BlurMode int mode) {
        return BACKGROUND_BLUR_SUPPORTED && call("setMiViewBlurMode", view, new Class[]{int.class}, new Object[]{mode});
    }

    /**
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    public static boolean addBackgroundBlendColor(@NonNull View view, @ColorInt int color, @BlendMode int mode) {
        return isBackgroundBlurEnabled(view.getContext()) && call("addMiBackgroundBlendColor", view, new Class[]{int.class, int.class}, new Object[]{color, mode});
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public static boolean addBackgroundBlendColor(@NonNull View view, @ColorInt int color, @NonNull android.graphics.BlendMode mode) {
        return addBackgroundBlendColor(view, color, mode.ordinal());
    }

    /**
     * a convenient way to add multiple blend colors
     *
     * @throws IllegalArgumentException if length of colors and modes are not the same
     */
    public static boolean addBackgroundBlendColors(@NonNull View view, @NonNull @ColorInt int[] colors, @NonNull @BlendMode int[] modes) {
        int length = colors.length;
        if (modes.length != length) {
            throw new IllegalArgumentException("colors and modes size mismatch");
        }
        for (int i = 0; i < length; i ++) {
            if (!addBackgroundBlendColor(view, colors[i], modes[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see #addBackgroundBlendColors(View, int[], int[])
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    public static boolean addBackgroundBlendColors(@NonNull View view, @NonNull @ColorInt int[] colors, @NonNull android.graphics.BlendMode[] modes) {
        int length = modes.length;
        @BlendMode
        int[] ordinalModes = new int[length];
        for (int i = 0; i < length; i ++) {
            ordinalModes[i] = modes[i].ordinal();
        }
        return addBackgroundBlendColors(view, colors, ordinalModes);
    }

    /**
     * @apiNote from miuix.core.util.MiuiBlurUtils
     */
    public static boolean clearBackgroundBlendColor(@NonNull View view) {
        return BACKGROUND_BLUR_SUPPORTED && call("clearMiBackgroundBlendColor", view, null, null);
    }

    /**
     * view blur modes
     * <p>
     * we don't know the exact values but they does take effect.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BLUR_MODE_NONE, BLUR_MODE_BACKGROUND, BLUR_MODE_CONTENT_SHAPE, BLUR_MODE_CONTENT_SHAPE_IGNORES_COLOR})
    public @interface BlurMode {
    }

    /**
     * view background blur modes
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BACKGROUND_BLUR_MODE_NONE, BACKGROUND_BLUR_MODE_BACKGROUND, BACKGROUND_BLUR_MODE_FOREGROUND})
    public @interface BackgroundBlurMode {
    }

    /**
     * color blend modes
     * <p>
     * we don't know the exact values but they does take effect.
     * in miui apps we found 100, 106, 66
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(/*open = true,*/ value = {BLEND_MODE_CLEAR, BLEND_MODE_SRC, BLEND_MODE_DST, BLEND_MODE_SRC_OVER, BLEND_MODE_DST_OVER,
            BLEND_MODE_SRC_IN, BLEND_MODE_DST_IN, BLEND_MODE_SRC_OUT, BLEND_MODE_DST_OUT, BLEND_MODE_SRC_ATOP, BLEND_MODE_DST_ATOP,
            BLEND_MODE_XOR, BLEND_MODE_PLUS, BLEND_MODE_MODULATE, BLEND_MODE_SCREEN, BLEND_MODE_OVERLAY, BLEND_MODE_DARKEN,
            BLEND_MODE_LIGHTEN, BLEND_MODE_COLOR_DODGE, BLEND_MODE_COLOR_BURN, BLEND_MODE_HARD_LIGHT, BLEND_MODE_SOFT_LIGHT,
            BLEND_MODE_DIFFERENCE, BLEND_MODE_EXCLUSION, BLEND_MODE_MULTIPLY, BLEND_MODE_HUE, BLEND_MODE_SATURATION, BLEND_MODE_COLOR,
            BLEND_MODE_LUMINOSITY, BLEND_MODE_COLOR_MIXING_PRESERVE_TRANSPARENCY, BLEND_MODE_COLOR_MIXING})
    public @interface BlendMode {
    }

    public interface OnBackgroundBlurChangedListener {
        void onBackgroundBlurChanged(boolean enabled);
    }

    private static class BackgroundBlurSettingsObserver extends ContentObserver {
        @NonNull
        private final OnBackgroundBlurChangedListener listener;

        public BackgroundBlurSettingsObserver(@NonNull Handler handler, @NonNull OnBackgroundBlurChangedListener listener) {
            super(handler);

            this.listener = listener;
        }

        @Override
        public void onChange(boolean selfChange) {
            //noinspection DataFlowIssue
            listener.onBackgroundBlurChanged(isBackgroundBlurEnabled(backgroundBlurListeners.get(this)));
        }

        void register(@NonNull Context context) {
            if (backgroundBlurListeners.get(this) != null) {
                throw new IllegalStateException("observer already registered.");
            }
            backgroundBlurListeners.put(this, context);
            context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(BACKGROUND_BLUR_ENABLED_CONFIG), false, this);
        }

        void unregister() {
            Context context = backgroundBlurListeners.get(this);
            if (context != null) {
                context.getContentResolver().unregisterContentObserver(this);
                backgroundBlurListeners.remove(this);
            }
        }

    }

}
