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

        val btnSend = findViewById<android.widget.Button>(R.id.btn_send)

        if (!settingsManager.autoOpenGesture) {
            btnSend.visibility = android.view.View.VISIBLE
            btnSend.setOnClickListener {
                val currentGesture = gestureOverlay.gesture
                if (currentGesture != null && currentGesture.strokesCount > 0) {
                    if (GestureManager.recognizeGesture(this, currentGesture)) {
                        val pin = settingsManager.pin
                        if (pin.isNotEmpty()) {
                            ShizukuUtil.unlockDevice(pin)
                        } else {
                            Toast.makeText(this, "PIN not set in app", Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect Gesture", Toast.LENGTH_SHORT).show()
                        gestureOverlay.clear(true)
                    }
                } else {
                    Toast.makeText(this, "Draw a gesture first", Toast.LENGTH_SHORT).show()
                }
            }
        }

        gestureOverlay.addOnGesturePerformedListener { overlay, gesture ->
            if (settingsManager.autoOpenGesture) {
                if (GestureManager.recognizeGesture(this, gesture)) {
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
}
