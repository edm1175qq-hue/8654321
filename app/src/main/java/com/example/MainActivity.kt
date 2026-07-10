package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.ui.MainDashboardApp
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the local Room Database
        val database = AppDatabase.getDatabase(applicationContext)
        val viewModelFactory = MainViewModelFactory(database)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        setContent {
            MyApplicationTheme {
                // Request SMS runtime permission
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        Toast.makeText(this, "สิทธิ์ดักจับ SMS ได้รับการอนุญาตแล้ว", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "ต้องการสิทธิ์ RECEIVE_SMS เพื่อดักจับข้อความจากธนาคารโดยอัตโนมัติ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                LaunchedEffect(Unit) {
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.RECEIVE_SMS
                    )
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        permissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SlateDark
                ) {
                    MainDashboardApp(viewModel = viewModel)
                }
            }
        }
    }
}
