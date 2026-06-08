package dhp.thl.tpl.gesture

import android.util.Log
import rikka.shizuku.Shizuku

object ShizukuUtil {

    fun unlockDevice(pin: String) {
        if (!Shizuku.pingBinder()) {
            Log.e("ShizukuUtil", "Shizuku is not running")
            return
        }

        if (Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Log.e("ShizukuUtil", "Shizuku permission not granted")
            return
        }

        Thread {
            try {
                // Wait for GestureUnlockActivity to finish and close
                Thread.sleep(500)
                
                // Wake up and dismiss bouncer (swipe up)
                executeShellCommand("input", "keyevent", "224") // WAKEUP
                Thread.sleep(100)
                executeShellCommand("input", "keyevent", "82") // MENU/UNLOCK
                Thread.sleep(500)
                
                // Input the PIN
                executeShellCommand("input", "text", pin)
                Thread.sleep(200)
                
                // Press Enter (Key code 66)
                executeShellCommand("input", "keyevent", "66")
            } catch (e: Exception) {
                Log.e("ShizukuUtil", "Error executing Shizuku command", e)
            }
        }.start()
    }

    private fun executeShellCommand(vararg command: String) {
        try {
            val process = Shizuku.newProcess(command as Array<String>, null, null)
            process.waitFor()
        } catch (e: Exception) {
            Log.e("ShizukuUtil", "Failed to run command", e)
        }
    }
}
