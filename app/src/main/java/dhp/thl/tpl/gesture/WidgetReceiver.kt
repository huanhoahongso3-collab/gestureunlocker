package dhp.thl.tpl.gesture

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "dhp.thl.tpl.gesture.WIDGET_CLICKED") {
            val activityIntent = Intent(context, GestureUnlockActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
            context.startActivity(activityIntent)
        }
    }
}
