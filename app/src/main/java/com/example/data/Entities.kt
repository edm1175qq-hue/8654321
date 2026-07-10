package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuration")
data class Configuration(
    @PrimaryKey val id: Int = 1,
    val webhookUrl: String = "https://khaki-lapwing-104409.hostingersite.com/api/v1/sms/callback",
    val token: String = "fd49e732c5f5ed78fe5fe38b5f8ac8c2",
    val isSmsForwardEnabled: Boolean = true,
    val isNotificationForwardEnabled: Boolean = true,
    val selectedBankPackages: String = "com.kasikorn.kplus,com.scb.phone,th.co.krungthaibank.next,com.bualuang.mbanking,com.krungsri.kma,com.ttbbank.oneapp,gsb.or.th.mymo,com.tdg.truemoneywallet,com.garena.android.koalapay,th.co.lhbank.mobilebanking,com.uob.mightyth,th.co.cimbthai.clicks"
)

@Entity(tableName = "forward_logs")
data class ForwardLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "SMS" or "NOTIFICATION"
    val sender: String,
    val message: String,
    val status: String, // "SUCCESS" or "FAILED"
    val responseMessage: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
