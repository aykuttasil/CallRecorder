package com.aykuttasil.callrecord.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by aykutasil on 8.12.2016.
 */

public class PrefsHelper {

    private static final String DEFAULT_STRING_VALUE = null;
    private static final int DEFAULT_INT_VALUE = 0;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    ////
    private Context mContext;
    private SharedPreferences mPref;

    public PrefsHelper(Context context, String pref_name) {
        mPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    }

    public PrefsHelper(Context context) {
        mPref = getDefaultPreference(context);
    }

    public static SharedPreferences getDefaultPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void writePrefString(Context context, String key, String value) {
        new PrefsHelper(context).getPrefEditor().putString(key, value).commit();
    }

    public static String readPrefString(Context context, String key) {
        return new PrefsHelper(context).getPreference().getString(key, DEFAULT_STRING_VALUE);
    }

    //
    public static void writePrefInt(Context context, String key, int value) {
        new PrefsHelper(context).getPrefEditor().putInt(key, value).commit();
    }

    public static int readPrefInt(Context context, String key) {
        return new PrefsHelper(context).getPreference().getInt(key, DEFAULT_INT_VALUE);
    }
    //

    //
    public static void writePrefBool(Context context, String key, boolean value) {
        new PrefsHelper(context).getPrefEditor().putBoolean(key, value).commit();
    }

    public static boolean readPrefBool(Context context, String key) {
        return new PrefsHelper(context).getPreference().getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }
    //

    public static void clearPreference(Context context) {
        new PrefsHelper(context).getPreference().edit().clear().apply();

    }

    public SharedPreferences getPreference() {
        return mPref;
    }
    //

    public SharedPreferences.Editor getPrefEditor() {
        return getPreference().edit();
    }


    /*
    public class DateTimePreference {
        private final SharedPreferences preferences;
        private final String key;
        private final DateTime defaultValue;

        public DateTimePreference(@NonNull SharedPreferences preferences, @NonNull String key) {
            this(preferences, key, null);
        }

        public DateTimePreference(@NonNull SharedPreferences preferences, @NonNull String key, @Nullable DateTime defaultValue) {
            this.preferences = preferences;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        @Nullable
        public DateTime get() {
            final long millis = preferences.getLong(key, defaultValue != null ? defaultValue.getMillis() : -1);
            return millis == -1 ? null : new DateTime(millis);
        }

        public boolean isSet() {
            return preferences.contains(key);
        }

        public void set(@Nullable DateTime value) {
            preferences.edit().putLong(key, value != null ? value.getMillis() : -1).apply();
        }

        public void delete() {
            preferences.edit().remove(key).apply();
        }
    }
    */


    public class EnumPreference<E extends Enum<E>> {
        private final SharedPreferences preferences;
        private final Class<E> clazz;
        private final String key;
        private final E defaultValue;


        public EnumPreference(@NonNull SharedPreferences preferences,
                              @NonNull Class<E> clazz,
                              @NonNull String key,
                              @NonNull E defaultValue) {
            this.preferences = preferences;
            this.clazz = clazz;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        @NonNull
        public E get() {
            final String string = preferences.getString(key, null);
            return string != null ? E.valueOf(clazz, string) : defaultValue;
        }

        @SuppressWarnings("UnusedDeclaration")
        public boolean isSet() {
            return preferences.contains(key);
        }

        public void set(@Nullable E value) {
            preferences.edit().putString(key, value != null ? value.name() : null).apply();
        }

        @SuppressWarnings("UnusedDeclaration")
        public void delete() {
            preferences.edit().remove(key).apply();
        }
    }

    public class StringPreference {
        private final SharedPreferences preferences;
        private final String key;
        private final String defaultValue;

        public StringPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
            this(preferences, key, null);
        }

        public StringPreference(@NonNull SharedPreferences preferences, @NonNull String key, @Nullable String defaultValue) {
            this.preferences = preferences;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        @Nullable
        public String get() {
            return preferences.getString(key, defaultValue);
        }

        public boolean isSet() {
            return preferences.contains(key);
        }

        public void set(@Nullable String value) {
            preferences.edit().putString(key, value).apply();
        }

        public void delete() {
            preferences.edit().remove(key).apply();
        }
    }


    public class IntPreference {
        private final SharedPreferences preferences;
        private final String key;
        private final int defaultValue;

        public IntPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
            this(preferences, key, 0);
        }

        public IntPreference(@NonNull SharedPreferences preferences, @NonNull String key, int defaultValue) {
            this.preferences = preferences;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public int get() {
            return preferences.getInt(key, defaultValue);
        }

        public boolean isSet() {
            return preferences.contains(key);
        }

        public void set(int value) {
            preferences.edit().putInt(key, value).apply();
        }

        public void delete() {
            preferences.edit().remove(key).apply();
        }
    }

    public class BooleanPreference {
        private final SharedPreferences preferences;
        private final String key;
        private final boolean defaultValue;

        public BooleanPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
            this(preferences, key, false);
        }

        public BooleanPreference(@NonNull SharedPreferences preferences, @NonNull String key, boolean defaultValue) {
            this.preferences = preferences;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public boolean get() {
            return preferences.getBoolean(key, defaultValue);
        }

        public boolean isSet() {
            return preferences.contains(key);
        }

        public void set(boolean value) {
            preferences.edit().putBoolean(key, value).apply();
        }

        public void delete() {
            preferences.edit().remove(key).apply();
        }
    }

}
