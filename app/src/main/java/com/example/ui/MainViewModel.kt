package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Configuration
import com.example.data.ForwardLog
import com.example.network.WebhookSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val database: AppDatabase) : ViewModel() {

    private val configDao = database.configDao()
    private val logDao = database.forwardLogDao()

    // Configuration flow
    val configState: StateFlow<Configuration> = configDao.getConfig()
        .map { it ?: Configuration() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Configuration() // Fallback/Initial default config
        )

    // Logs flow
    val logsState: StateFlow<List<ForwardLog>> = logDao.getAllLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isTestingWebhook = MutableStateFlow(false)
    val isTestingWebhook: StateFlow<Boolean> = _isTestingWebhook.asStateFlow()

    private val _testResult = MutableStateFlow<String?>(null)
    val testResult: StateFlow<String?> = _testResult.asStateFlow()

    fun updateSmsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = configState.value
            configDao.insertConfig(current.copy(isSmsForwardEnabled = enabled))
        }
    }

    fun updateNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = configState.value
            configDao.insertConfig(current.copy(isNotificationForwardEnabled = enabled))
        }
    }

    fun saveConfig(webhookUrl: String, token: String) {
        viewModelScope.launch {
            val current = configState.value
            configDao.insertConfig(current.copy(webhookUrl = webhookUrl, token = token))
        }
    }

    fun updateSelectedBanks(selectedPackages: List<String>) {
        viewModelScope.launch {
            val current = configState.value
            val packagesString = selectedPackages.joinToString(",")
            configDao.insertConfig(current.copy(selectedBankPackages = packagesString))
        }
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            logDao.clearLogs()
        }
    }

    fun sendTestWebhook(context: Context) {
        viewModelScope.launch {
            _isTestingWebhook.value = true
            _testResult.value = null
            
            try {
                // Get current time formatted in Asia/Bangkok timezone
                val tz = java.util.TimeZone.getTimeZone("Asia/Bangkok")
                val dfDate = java.text.SimpleDateFormat("dd/MM/yy", java.util.Locale.US).apply { timeZone = tz }
                val dfTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.US).apply { timeZone = tz }
                
                val currentTimestamp = System.currentTimeMillis()
                val thaiDate = dfDate.format(java.util.Date(currentTimestamp))
                val thaiTime = dfTime.format(java.util.Date(currentTimestamp))
                
                // Format the test message to look exactly like a real KBank / K PLUS transaction notification with current time.
                // This ensures correct regex parsing on the backend so that it successfully matches the transaction!
                val testMessage = "คุณได้รับโอนเงินจำนวน 3,500.00 บาท จาก นายสมชาย เมื่อ $thaiDate $thaiTime น."

                // Simulate a bank transfer notification for testing
                val success = WebhookSender.sendForward(
                    context = context,
                    type = "NOTIFICATION",
                    sender = "KBank",
                    message = testMessage,
                    ignoreEnabledCheck = true
                )
                if (success) {
                    _testResult.value = "ส่งข้อความทดสอบสำเร็จ! กรุณาตรวจสอบที่ Webhook ของคุณ"
                } else {
                    // Fetch the latest log to see why it failed
                    val latestLog = logDao.getLatestLog()
                    val errorDetail = latestLog?.responseMessage ?: "เซิร์ฟเวอร์ปฏิเสธการเชื่อมต่อหรือ URL ผิดพลาด"
                    _testResult.value = "การส่งข้อความทดสอบล้มเหลว: $errorDetail"
                }
            } catch (e: Exception) {
                _testResult.value = "การทดสอบล้มเหลว: ${e.localizedMessage}"
            } finally {
                _isTestingWebhook.value = false
            }
        }
    }

    fun clearTestResult() {
        _testResult.value = null
    }
}

// ViewModel Factory
class MainViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
