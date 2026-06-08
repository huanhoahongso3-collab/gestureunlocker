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
            gestureLibrary?.sequenceType = android.gesture.GestureStore.SEQUENCE_INVARIANT
            gestureLibrary?.orientationStyle = android.gesture.GestureStore.ORIENTATION_INVARIANT
            
            if (!storeFile.exists() || !gestureLibrary!!.load()) {
                // Initialize if needed
            }
        }
        return gestureLibrary!!
    }

    fun removeGesture(context: Context) {
        val lib = getLibrary(context)
        lib.removeEntry(GESTURE_NAME)
        lib.save()
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
            // Lowered threshold to 1.0 and since we use SEQUENCE_INVARIANT and ORIENTATION_INVARIANT, 
            // the match will focus more on the general shape than the exact drawing speed or size.
            if (bestPrediction.name == GESTURE_NAME && bestPrediction.score >= 1.0) {
                return true
            }
        }
        return false
    }
}
