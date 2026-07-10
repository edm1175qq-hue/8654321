package com.example.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Link
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ForwardLog
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.DarkYellowText
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LightYellowBg
import com.example.ui.theme.LimeGreen
import com.example.ui.theme.LimeGreenDim
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningYellow
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassBgDarker
import com.example.ui.theme.GlassAccentGreen
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Popular Thai banks mapping
data class BankAppInfo(
    val name: String,
    val packageName: String,
    val color: Color,
    val initial: String,
    val fullName: String
)

val THAI_BANKS = listOf(
    BankAppInfo("K PLUS", "com.kasikorn.kplus", Color(0xFF138F47), "K", "ธนาคารกสิกรไทย (K PLUS)"),
    BankAppInfo("SCB EASY", "com.scb.phone", Color(0xFF4E2A84), "S", "ธนาคารไทยพาณิชย์ (SCB EASY)"),
    BankAppInfo("Krungthai NEXT", "th.co.krungthaibank.next", Color(0xFF00A4E4), "KT", "ธนาคารกรุงไทย (Krungthai NEXT)"),
    BankAppInfo("Bualuang mB", "com.bualuang.mbanking", Color(0xFF003399), "B", "ธนาคารกรุงเทพ (Bualuang mBanking)"),
    BankAppInfo("KMA krungsri", "com.krungsri.kma", Color(0xFFFCBA12), "Kr", "ธนาคารกรุงศรีอยุธยา (KMA)"),
    BankAppInfo("ttb touch", "com.ttbbank.oneapp", Color(0xFF0056FF), "ttb", "ธนาคารทหารไทยธนชาต (ttb touch)"),
    BankAppInfo("MyMo", "gsb.or.th.mymo", Color(0xFFED008C), "M", "ธนาคารออมสิน (MyMo)"),
    BankAppInfo("TrueMoney", "com.tdg.truemoneywallet", Color(0xFFFF5F00), "TM", "ทรูมันนี่ วอลเล็ท (TrueMoney Wallet)"),
    BankAppInfo("ShopeePay", "com.garena.android.koalapay", Color(0xFFFF5722), "SP", "ช้อปปี้เพย์ (ShopeePay)"),
    BankAppInfo("LHB mChoice", "th.co.lhbank.mobilebanking", Color(0xFF53565A), "LH", "ธนาคารแลนด์ แอนด์ เฮ้าส์"),
    BankAppInfo("UOB TMRW", "com.uob.mightyth", Color(0xFF012D74), "U", "ธนาคารยูโอบี (UOB TMRW)"),
    BankAppInfo("CIMB Clicks", "th.co.cimbthai.clicks", Color(0xFFC7082C), "C", "ธนาคารซีไอเอ็มบี ไทย")
)

fun isNotificationServiceEnabled(context: Context): Boolean {
    val pkgName = context.packageName
    val flat = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    if (!flat.isNullOrEmpty()) {
        val names = flat.split(":")
        for (name in names) {
            val cn = android.content.ComponentName.unflattenFromString(name)
            if (cn != null && cn.packageName == pkgName) {
                return true
            }
        }
    }
    return false
}

@Composable
fun MainDashboardApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(0) }
    var hasNotificationPermission by remember { mutableStateOf(isNotificationServiceEnabled(context)) }

    // Periodically refresh notification permission check when activity is resumed
    LaunchedEffect(Unit) {
        while (true) {
            hasNotificationPermission = isNotificationServiceEnabled(context)
            kotlinx.coroutines.delay(2000)
        }
    }

    val config by viewModel.configState.collectAsState()
    val logs by viewModel.logsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDark)
            .drawBehind {
                // Top-left soft blue gradient blur (mesh decorative element)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x262563EB), Color.Transparent),
                        center = Offset(size.width * -0.1f, size.height * -0.05f),
                        radius = size.minDimension * 0.8f
                    ),
                    radius = size.minDimension * 0.8f,
                    center = Offset(size.width * -0.1f, size.height * -0.05f)
                )
                // Bottom-right soft indigo gradient blur (mesh decorative element)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x266366F1), Color.Transparent),
                        center = Offset(size.width * 1.1f, size.height * 1.05f),
                        radius = size.minDimension * 0.9f
                    ),
                    radius = size.minDimension * 0.9f,
                    center = Offset(size.width * 1.1f, size.height * 1.05f)
                )
            }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0x0F1E293B), // Translucent dark
                    modifier = Modifier
                        .testTag("bottom_nav_bar")
                        .drawBehind {
                            // Top border representing glass refraction
                            drawLine(
                                color = GlassBorder,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(imageVector = if (currentTab == 0) Icons.Filled.CheckCircle else Icons.Outlined.Home, contentDescription = "หน้าหลัก") },
                        label = { Text("หน้าหลัก", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LimeGreen,
                            selectedTextColor = LimeGreen,
                            indicatorColor = Color(0x19FFFFFF),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(imageVector = Icons.Outlined.Link, contentDescription = "ตั้งค่าที่อยู่") },
                        label = { Text("เปลี่ยนที่อยู่", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LimeGreen,
                            selectedTextColor = LimeGreen,
                            indicatorColor = Color(0x19FFFFFF),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        icon = { Icon(imageVector = Icons.Outlined.Devices, contentDescription = "เลือกธนาคาร") },
                        label = { Text("เลือกธนาคาร", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LimeGreen,
                            selectedTextColor = LimeGreen,
                            indicatorColor = Color(0x19FFFFFF),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Brand Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, GlassBorder), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "O",
                                color = LimeGreen,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SYSTEM SECURE",
                                color = AccentBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "EasyO Forwarder",
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Monitoring status badge
                    val liveMonitoring = config.isSmsForwardEnabled || config.isNotificationForwardEnabled
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (liveMonitoring) Color(0x1A10B981) else Color(0x1AEF4444))
                            .border(BorderStroke(1.dp, if (liveMonitoring) Color(0x3310B981) else Color(0x33EF4444)), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (liveMonitoring) GlassAccentGreen else ErrorRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (liveMonitoring) "LIVE MONITORING" else "PAUSED",
                            color = if (liveMonitoring) GlassAccentGreen else ErrorRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = GlassBorder, thickness = 1.dp)

            // Dynamic Content Tabs
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentTab) {
                    0 -> DashboardTab(
                        viewModel = viewModel,
                        hasNotificationPermission = hasNotificationPermission,
                        logs = logs,
                        smsEnabled = config.isSmsForwardEnabled,
                        notificationEnabled = config.isNotificationForwardEnabled,
                        webhookUrl = config.webhookUrl,
                        token = config.token,
                        onSmsToggle = { viewModel.updateSmsEnabled(it) },
                        onNotificationToggle = { viewModel.updateNotificationEnabled(it) },
                        onGrantPermissionClick = {
                            try {
                                context.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "ไม่สามารถเปิดหน้าตั้งค่าได้", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                    1 -> SettingsTab(
                        viewModel = viewModel,
                        currentUrl = config.webhookUrl,
                        currentToken = config.token,
                        onSave = { url, token ->
                            viewModel.saveConfig(url, token)
                            Toast.makeText(context, "บันทึกที่อยู่เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
                        }
                    )
                    2 -> AppSelectionTab(
                        selectedBanksString = config.selectedBankPackages,
                        onBanksChanged = { selectedList ->
                            viewModel.updateSelectedBanks(selectedList)
                        }
                    )
                }
            }
        }
    }
}
}

@Composable
fun DashboardTab(
    viewModel: MainViewModel,
    hasNotificationPermission: Boolean,
    logs: List<ForwardLog>,
    smsEnabled: Boolean,
    notificationEnabled: Boolean,
    webhookUrl: String,
    token: String,
    onSmsToggle: (Boolean) -> Unit,
    onNotificationToggle: (Boolean) -> Unit,
    onGrantPermissionClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // 1. Permission Warning Card
        if (!hasNotificationPermission) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("permission_warning_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = LightYellowBg),
                    border = BorderStroke(1.dp, WarningYellow.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Notification alert",
                            tint = WarningYellow,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ต้องการสิทธิ์เข้าถึงการแจ้งเตือน",
                                color = DarkYellowText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "กรุณาเปิดสิทธิ์เพื่อให้แอปดักจับการแจ้งเตือนจากแอปธนาคารโดยตรงและส่งออกอัตโนมัติ",
                                color = DarkYellowText.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = onGrantPermissionClick,
                            colors = ButtonDefaults.buttonColors(containerColor = WarningYellow),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = RowPaddingCompact()
                        ) {
                            Text("เปิดสิทธิ์", color = SlateDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. Overview Counters Card
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // SMS stats Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SlateCard),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ระบบดัก SMS", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Switch(
                                checked = smsEnabled,
                                onCheckedChange = onSmsToggle,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = LimeGreen,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = Color(0x26000000)
                                ),
                                modifier = Modifier.testTag("sms_forward_switch")
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = logs.filter { it.type == "SMS" && it.status == "SUCCESS" }.size.toString(),
                            color = TextPrimary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ส่งออกสำเร็จวันนี้",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Notification stats Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SlateCard),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ดักการแจ้งเตือน", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Switch(
                                checked = notificationEnabled,
                                onCheckedChange = onNotificationToggle,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = LimeGreen,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = Color(0x26000000)
                                ),
                                modifier = Modifier.testTag("notification_forward_switch")
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = logs.filter { it.type == "NOTIFICATION" && it.status == "SUCCESS" }.size.toString(),
                            color = TextPrimary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ส่งออกสำเร็จวันนี้",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 3. Webhook Summary Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "เป้าหมายการส่งออก Webhook",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Webhook URL
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Link, contentDescription = "Webhook Url", tint = AccentBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (webhookUrl.isBlank()) "ไม่ได้ตั้งค่าที่อยู่" else webhookUrl,
                            color = if (webhookUrl.isBlank()) ErrorRed else TextPrimary,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (webhookUrl.isNotBlank()) {
                                    clipboardManager.setText(AnnotatedString(webhookUrl))
                                    Toast.makeText(context, "คัดลอก URL แล้ว", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Url", tint = TextSecondary, modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Access Token
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Key, contentDescription = "Access Token", tint = LimeGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        val displayToken = if (token.length > 8) {
                            token.take(4) + "..." + token.takeLast(4)
                        } else if (token.isBlank()) {
                            "ไม่มีโทเคน"
                        } else {
                            token
                        }
                        Text(
                            text = displayToken,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (token.isNotBlank()) {
                                    clipboardManager.setText(AnnotatedString(token))
                                    Toast.makeText(context, "คัดลอก Token แล้ว", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Token", tint = TextSecondary, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // 4. Log History Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LimeGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RECENT ACTIVITY",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                if (logs.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.clearAllLogs() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = "ลบประวัติ", tint = ErrorRed.copy(alpha = 0.8f))
                    }
                }
            }
        }

        // 5. Activity Log list or empty state
        if (logs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x0AFFFFFF)),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "No logs yet",
                            tint = TextSecondary,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "ไม่มีประวัติการส่งต่อ SMS / การแจ้งเตือน",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "SMS ที่ได้รับและผ่านตัวกรองจะถูกแสดงขึ้นเมื่อส่งออกสำเร็จ",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }
        } else {
            items(logs.take(50)) { log ->
                LogItemCard(log)
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun LogItemCard(log: ForwardLog) {
    val dateString = remember(log.timestamp) {
        val formatter = SimpleDateFormat("HH:mm:ss · dd/MM/yy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("Asia/Bangkok")
        }
        formatter.format(Date(log.timestamp))
    }

    val isSuccess = log.status == "SUCCESS"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sender and type
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (log.type == "SMS") AccentBlue.copy(alpha = 0.15f) else LimeGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (log.type == "SMS") Icons.Default.Sms else Icons.Default.Notifications,
                            contentDescription = log.type,
                            tint = if (log.type == "SMS") AccentBlue else LimeGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = log.sender,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${log.type} · $dateString",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                // Status Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSuccess) Color(0x2610B981) else Color(0x26EF4444))
                        .border(BorderStroke(1.dp, if (isSuccess) Color(0x3310B981) else Color(0x33EF4444)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = log.status,
                        tint = if (isSuccess) GlassAccentGreen else ErrorRed,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSuccess) "ส่งสำเร็จ" else "ล้มเหลว",
                        color = if (isSuccess) GlassAccentGreen else ErrorRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            
            // Log message body
            Text(
                text = log.message,
                color = TextPrimary.copy(alpha = 0.9f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            // If failed, show response or reason
            if (log.responseMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GlassBgDarker)
                        .border(BorderStroke(0.5.dp, GlassBorder), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = "Status detail",
                        tint = if (isSuccess) GlassAccentGreen else ErrorRed,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = log.responseMessage,
                        color = if (isSuccess) TextSecondary else Color(0xFFFCA5A5),
                        fontSize = 10.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsTab(
    viewModel: MainViewModel,
    currentUrl: String,
    currentToken: String,
    onSave: (String, String) -> Unit
) {
    val context = LocalContext.current
    var url by remember(currentUrl) { mutableStateOf(currentUrl) }
    var token by remember(currentToken) { mutableStateOf(currentToken) }

    val isTesting by viewModel.isTestingWebhook.collectAsState()
    val testResult by viewModel.testResult.collectAsState()

    // Clear test results on screen load or dismiss
    LaunchedEffect(Unit) {
        viewModel.clearTestResult()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Setup guide
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "แก้ไขเป้าหมาย Webhook",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ระบบจะยิง HTTP POST ไปยัง URL นี้ทันทีที่ได้รับ SMS หรือการแจ้งเตือนจากธนาคาร พร้อมแนบ Token ใน Headers และ Body เพื่อความปลอดภัย 100%",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Form Fields
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Webhook URL field
                    Text(
                        text = "Webhook URL (ที่อยู่เซิร์ฟเวอร์ callback)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("webhook_url_input"),
                        placeholder = { Text("https://example.com/callback", color = TextSecondary.copy(alpha = 0.5f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LimeGreen,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = GlassBgDarker,
                            unfocusedContainerColor = GlassBgDarker
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Access Token field
                    Text(
                        text = "Access Token / Bearer Token",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = token,
                        onValueChange = { token = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("webhook_token_input"),
                        placeholder = { Text("กรอก Token สำหรับการยืนยันตัวตน", color = TextSecondary.copy(alpha = 0.5f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LimeGreen,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = GlassBgDarker,
                            unfocusedContainerColor = GlassBgDarker
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Test Webhook Button
                        Button(
                            onClick = {
                                if (url.isBlank()) {
                                    Toast.makeText(context, "กรุณากรอก Webhook URL ก่อนทดสอบ", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.sendTestWebhook(context)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("test_webhook_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GlassBorder)
                        ) {
                            if (isTesting) {
                                CircularProgressIndicator(color = LimeGreen, modifier = Modifier.size(18.dp))
                            } else {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Test", tint = LimeGreen)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ทดสอบส่ง", color = LimeGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        // Save Button
                        Button(
                            onClick = { onSave(url, token) },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .testTag("save_webhook_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = "Save", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("บันทึกที่อยู่", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Test outcome display
        if (testResult != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (testResult!!.contains("สำเร็จ")) Color(0x2610B981) else Color(0x26EF4444)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, if (testResult!!.contains("สำเร็จ")) Color(0x4010B981) else Color(0x40EF4444))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (testResult!!.contains("สำเร็จ")) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = "Test result icon",
                            tint = if (testResult!!.contains("สำเร็จ")) GlassAccentGreen else ErrorRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = testResult!!,
                            color = if (testResult!!.contains("สำเร็จ")) TextPrimary else Color(0xFFFECACA),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AppSelectionTab(
    selectedBanksString: String,
    onBanksChanged: (List<String>) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedPackages = remember(selectedBanksString) {
        selectedBanksString.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    val filteredBanks = THAI_BANKS.filter {
        it.fullName.contains(searchQuery, ignoreCase = true) ||
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.packageName.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Card header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateCard),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, GlassBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "คัดกรองแอปธนาคารที่ต้องการดักจับ",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "เลือกเฉพาะแอปธนาคารที่คุณติดตั้งและต้องการใช้งานเท่านั้น เพื่อป้องกันความสับสนและรับประกันความถูกต้องแม่นยำในการนำไปประมวลผล 100%",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bank_search_input"),
            placeholder = { Text("ค้นหาชื่อธนาคาร...", color = TextSecondary.copy(alpha = 0.5f)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LimeGreen,
                unfocusedBorderColor = GlassBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = GlassBgDarker,
                unfocusedContainerColor = GlassBgDarker
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // List of banks
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredBanks) { bank ->
                val isSelected = selectedPackages.contains(bank.packageName)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val newList = if (isSelected) {
                                selectedPackages - bank.packageName
                            } else {
                                selectedPackages + bank.packageName
                            }
                            onBanksChanged(newList)
                        }
                        .testTag("bank_card_${bank.packageName}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0x24FFFFFF) else Color(0x12FFFFFF)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, if (isSelected) GlassBorderLight else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Circular Initial Badge for Bank
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(bank.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bank.initial,
                                    color = bank.color,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = bank.name,
                                    color = if (isSelected) TextPrimary else TextPrimary.copy(alpha = 0.8f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = bank.packageName,
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Switch(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                val newList = if (checked) {
                                    selectedPackages + bank.packageName
                                } else {
                                    selectedPackages - bank.packageName
                                }
                                onBanksChanged(newList)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = LimeGreen,
                                uncheckedThumbColor = TextSecondary,
                                uncheckedTrackColor = Color(0x1F000000)
                            ),
                            modifier = Modifier.testTag("bank_switch_${bank.packageName}")
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

private fun RowPaddingCompact() = androidx.compose.foundation.layout.PaddingValues(
    horizontal = 12.dp,
    vertical = 6.dp
)
