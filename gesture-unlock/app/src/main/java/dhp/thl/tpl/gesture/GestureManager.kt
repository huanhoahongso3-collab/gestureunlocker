package dhp.thl.tpl.gesture

import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.content.Context
import java.io.File

object GestureManager {
    private const val GESTURE_NAME = "unlock_gesture"
    private var gestureLibrary: GestureLibrary? = null

    fun getLibrary(context: Context): GestureLibrary {
        if (gestureLibrary == null) {
            val storeFile = File(context.filesDir, "gestures")
            gestureLibrary = GestureLibraries.fromFile(storeFile)
            if (!storeFile.exists() || !gestureLibrary!!.load()) {
                // Initialize if needed
            }
        }
        return gestureLibrary!!
    }

    fun saveGesture(context: Context, gesture: Gesture) {
        val lib = getLibrary(context)
        lib.removeEntry(GESTURE_NAME)
        lib.addGesture(GESTURE_NAME, gesture)
        lib.save()
    }

    fun recognizeGesture(context: Context, gesture: Gesture): Boolean {
        val lib = getLibrary(context)
        val predictions = lib.recognize(gesture)
        if (predictions != null && predictions.isNotEmpty()) {
            val bestPrediction = predictions[0]
            if (bestPrediction.name == GESTURE_NAME && bestPrediction.score > 2.0) {
                return true
            }
        }
        return false
    }
}
