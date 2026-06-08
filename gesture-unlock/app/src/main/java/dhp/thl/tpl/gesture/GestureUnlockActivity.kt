package dhp.thl.tpl.gesture

import android.app.Activity
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.widget.Toast
import android.graphics.Color

class GestureUnlockActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Show over lockscreen
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        setContentView(R.layout.activity_gesture_unlock)

        val settingsManager = SettingsManager(this)
        val showGesture = settingsManager.showGesturePath
        
        val gestureOverlay = findViewById<GestureOverlayView>(R.id.gesture_overlay)
        
        if (!showGesture) {
            gestureOverlay.gestureColor = Color.TRANSPARENT
            gestureOverlay.uncertainGestureColor = Color.TRANSPARENT
        } else {
            gestureOverlay.gestureColor = Color.GREEN
            gestureOverlay.uncertainGestureColor = Color.YELLOW
        }

        gestureOverlay.addOnGesturePerformedListener { overlay, gesture ->
            if (GestureManager.recognizeGesture(this, gesture)) {
                // Success! Unlock via Shizuku
                val pin = settingsManager.pin
                if (pin.isNotEmpty()) {
                    ShizukuUtil.unlockDevice(pin)
                } else {
                    Toast.makeText(this, "PIN not set in app", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Incorrect Gesture", Toast.LENGTH_SHORT).show()
                overlay.clear(true)
            }
        }
    }
}
