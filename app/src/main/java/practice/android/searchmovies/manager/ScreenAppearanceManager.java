package practice.android.searchmovies.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Stella on 7/1/17.
 */

public class ScreenAppearanceManager {
    public static final String TAG = "ScreenAppearanceManager";
    public static final String SHARED_PREFERENCE = "shared preference";
    public static final String WIDTH = "screen width";
    public static final String LENGTH = "screen length";

    private SharedPreferences mPrefs;

    public ScreenAppearanceManager(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void setScreenPreference(Display display) {
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenLength = size.y;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(WIDTH, screenWidth);
        editor.putInt(LENGTH, screenLength);
        editor.apply();
    }

    public int getScreenLength() {
        return mPrefs.getInt(LENGTH, -1);
    }

    public int getScreenWidth() {
        return mPrefs.getInt(WIDTH, -1);
    }
}
