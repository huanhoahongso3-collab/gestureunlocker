package dhp.thl.tpl.gesture

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gesture_prefs", Context.MODE_PRIVATE)

    var pin: String
        get() = prefs.getString("pin", "") ?: ""
        set(value) = prefs.edit().putString("pin", value).apply()

    var showGesturePath: Boolean
        get() = prefs.getBoolean("show_gesture_path", true)
        set(value) = prefs.edit().putBoolean("show_gesture_path", value).apply()

    var autoOpenGesture: Boolean
        get() = prefs.getBoolean("auto_open_gesture", true)
        set(value) = prefs.edit().putBoolean("auto_open_gesture", value).apply()
}
