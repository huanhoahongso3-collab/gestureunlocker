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
        if (Shizuku.pingBpf() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(100)
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
                            if (Shizuku.pingBpf() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                                Shizuku.requestPermission(100)
                            } else {
                                Toast.makeText(this@MainActivity, "Shizuku is running and permission granted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    onRecordGestureClick: () -> Unit,
    onRequestShizuku: () -> Unit
) {
    var pin by remember { mutableStateOf(settingsManager.pin) }
    var showGesturePath by remember { mutableStateOf(settingsManager.showGesturePath) }

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

            Button(
                onClick = onRecordGestureClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Record Unlock Gesture")
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
