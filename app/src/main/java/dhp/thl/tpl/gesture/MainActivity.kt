package dhp.thl.tpl.gesture

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsManager = SettingsManager(this)

        // Request Shizuku Permission
        Shizuku.addRequestPermissionResultListener(this::onRequestPermissionResult)
        if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            try {
                Shizuku.requestPermission(100)
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to request Shizuku permission. Please open Shizuku app to enable it manually.", Toast.LENGTH_LONG).show()
            }
        }

        setContent {
            MaterialTheme(
                colorScheme = if (androidx.compose.foundation.isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen(
                        settingsManager = settingsManager,
                        onRecordGestureClick = {
                            startActivity(Intent(this@MainActivity, RecordGestureActivity::class.java))
                        },
                        onRequestShizuku = {
                            if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                                try {
                                    Shizuku.requestPermission(100)
                                } catch (e: Exception) {
                                    Toast.makeText(this@MainActivity, "Failed to request Shizuku permission. Please open Shizuku app to enable it manually.", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "Shizuku is running and permission granted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        val msg = if (grantResult == PackageManager.PERMISSION_GRANTED) {
            "Shizuku permission granted!"
        } else {
            "Shizuku permission denied. You may need to open Shizuku app to enable it manually."
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(this::onRequestPermissionResult)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    onRecordGestureClick: () -> Unit,
    onRequestShizuku: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var pin by remember { mutableStateOf(settingsManager.pin) }
    var showGesturePath by remember { mutableStateOf(settingsManager.showGesturePath) }
    var autoOpenGesture by remember { mutableStateOf(settingsManager.autoOpenGesture) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gesture Unlock Settings") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = pin,
                onValueChange = { 
                    pin = it
                    settingsManager.pin = it
                },
                label = { Text("Unlock PIN") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Show gesture path")
                Switch(
                    checked = showGesturePath,
                    onCheckedChange = { 
                        showGesturePath = it
                        settingsManager.showGesturePath = it
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Auto open when gesture is correct")
                Switch(
                    checked = autoOpenGesture,
                    onCheckedChange = { 
                        autoOpenGesture = it
                        settingsManager.autoOpenGesture = it
                    }
                )
            }

            Button(
                onClick = onRecordGestureClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Record Unlock Gesture")
            }

            Button(
                onClick = {
                    GestureManager.removeGesture(context)
                    Toast.makeText(context, "Gesture removed", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remove Saved Gesture")
            }
            
            Button(
                onClick = onRequestShizuku,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Request Shizuku Permission")
            }
        }
    }
}
