package com.example.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.data.AppDatabase
import com.example.network.WebhookSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BankNotificationListenerService : NotificationListenerService() {
    private val TAG = "BankNotificationListener"
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Notification Listener Connected!")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return

        val packageName = sbn.packageName ?: return
        
        // Skip our own app notifications to avoid loops
        if (packageName == applicationContext.packageName) return

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        
        if (text.isBlank() && title.isBlank()) {
            return
        }

        serviceScope.launch {
            try {
                val db = AppDatabase.getDatabase(applicationContext)
                val config = db.configDao().getConfigDirect() ?: com.example.data.Configuration()

                // Check if notification forwarding is enabled globally
                if (!config.isNotificationForwardEnabled) {
                    return@launch
                }

                // Check if the source package is one of the selected bank packages
                val selectedList = config.selectedBankPackages.split(",").map { it.trim() }
                val isSelectedBank = selectedList.any { it.equals(packageName, ignoreCase = true) }

                if (!isSelectedBank) {
                    Log.d(TAG, "Skipping notification from $packageName (not in selected bank packages list)")
                    return@launch
                }

                Log.d(TAG, "Captured bank notification from $packageName. Title: $title, Text: $text")

                // Resolve a human-friendly sender name (e.g., K PLUS, SCB EASY, etc. or fallback to Title/Package)
                val appLabel = try {
                    val pm = packageManager
                    val appInfo = pm.getApplicationInfo(packageName, 0)
                    pm.getApplicationLabel(appInfo).toString()
                } catch (e: Exception) {
                    null
                }

                // Map packages to standard SMS-style sender names expected by backend matching scripts
                val resolvedSender = when (packageName) {
                    "com.kasikorn.kplus" -> "KBank"
                    "com.scb.phone" -> "SCB"
                    "th.co.krungthaibank.next" -> "Krungthai"
                    "com.bualuang.mbanking" -> "Bualuang"
                    "com.krungsri.kma" -> "Krungsri"
                    "com.ttbbank.oneapp" -> "ttb"
                    "gsb.or.th.mymo" -> "MyMo"
                    "com.tdg.truemoneywallet" -> "TrueMoney"
                    "com.garena.android.koalapay" -> "ShopeePay"
                    "th.co.lhbank.mobilebanking" -> "LHB"
                    "com.uob.mightyth" -> "UOB"
                    "th.co.cimbthai.clicks" -> "CIMB"
                    else -> when {
                        !appLabel.isNullOrBlank() -> appLabel
                        title.isNotBlank() -> title
                        else -> packageName
                    }
                }

                // Combine Title and Text to represent the notification exactly as it is shown on screen
                val combinedMessage = when {
                    title.isNotBlank() && text.isNotBlank() -> {
                        if (text.contains(title, ignoreCase = true)) {
                            text
                        } else {
                            "$title\n$text"
                        }
                    }
                    text.isNotBlank() -> text
                    else -> title
                }

                // Forward notification
                WebhookSender.sendForward(
                    context = applicationContext,
                    type = "NOTIFICATION",
                    sender = resolvedSender,
                    message = combinedMessage
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error handling notification post", e)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}
