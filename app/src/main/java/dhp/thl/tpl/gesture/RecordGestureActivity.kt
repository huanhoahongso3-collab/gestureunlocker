package dhp.thl.tpl.gesture

import android.app.Activity
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.widget.Toast
import android.graphics.Color
import android.widget.Button

class RecordGestureActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.layoutParams = android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT
        )

        val gestureOverlay = GestureOverlayView(this)
        gestureOverlay.layoutParams = android.widget.LinearLayout.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            0, 1f
        )
        gestureOverlay.gestureColor = Color.BLUE
        gestureOverlay.isEventsInterceptionEnabled = true
        
        var currentGesture: android.gesture.Gesture? = null

        gestureOverlay.addOnGesturePerformedListener { _, gesture ->
            currentGesture = gesture
            Toast.makeText(this, "Gesture drawn! Press save.", Toast.LENGTH_SHORT).show()
        }

        val saveBtn = Button(this)
        saveBtn.text = "Save Gesture"
        saveBtn.setOnClickListener {
            if (currentGesture != null) {
                GestureManager.saveGesture(this, currentGesture!!)
                Toast.makeText(this, "Gesture saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Draw a gesture first", Toast.LENGTH_SHORT).show()
            }
        }

        layout.addView(gestureOverlay)
        layout.addView(saveBtn)
        setContentView(layout)
    }
}
