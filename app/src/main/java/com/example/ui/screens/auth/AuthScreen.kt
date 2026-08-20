package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.components.AvatarHelper
import com.example.ui.components.AvatarPickerRow
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RedError
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import kotlinx.coroutines.delay

enum class AuthTabMode {
    PHONE_SMS,
    EMAIL,
    FULL_REGISTER
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLogin: (email: String, pass: String, role: UserRole) -> Unit,
    onPhoneLogin: (phone: String, smsCode: String, role: UserRole) -> Unit,
    onRegister: (
        firstName: String,
        lastName: String,
        fatherName: String,
        birthDay: Int,
        birthMonth: Int,
        birthYear: Int,
        interests: String,
        avatarUrl: String,
        phone: String,
        email: String,
        role: UserRole,
        personalCode: String
    ) -> Unit,
    onDemoLogin: (UserRole) -> Unit
) {
    var activeTab by remember { mutableStateOf(AuthTabMode.PHONE_SMS) }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    // Phone / SMS state
    var phoneNumber by remember { mutableStateOf("+998 90 123 45 67") }
    var smsCodeInput by remember { mutableStateOf("") }
    var isSmsSent by remember { mutableStateOf(false) }
    var generatedSmsCode by remember { mutableStateOf("") }
    var smsTimerSeconds by remember { mutableIntStateOf(0) }
    var showSmsBanner by remember { mutableStateOf(false) }

    // Email state
    var emailInput by remember { mutableStateOf("example@gmail.com") }
    var passwordInput by remember { mutableStateOf("12345678") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showGoogleChooser by remember { mutableStateOf(false) }

    // Full Registration state
    var regFirstName by remember { mutableStateOf("Azizbek") }
    var regLastName by remember { mutableStateOf("Abduqodirov") }
    var regFatherName by remember { mutableStateOf("Alisher o'g'li") }
    var regBirthDay by remember { mutableIntStateOf(15) }
    var regBirthMonth by remember { mutableIntStateOf(8) }
    var regBirthYear by remember { mutableIntStateOf(2004) }
    var regPhone by remember { mutableStateOf("+998 90 123 45 67") }
    var regEmail by remember { mutableStateOf("azizbek@gmail.com") }
    var regAvatar by remember { mutableStateOf("avatar_boy_1") }

    val allInterests = listOf(
        "Ona tili va adabiyot", "Ingliz tili", "Matematika",
        "Fizika", "Kimyo", "Biologiya", "Tarix", "Geografiya", "Dasturlash"
    )
    val selectedInterests = remember {
        mutableStateListOf("Ona tili va adabiyot", "Ingliz tili", "Matematika")
    }

    // Timer effect for SMS
    LaunchedEffect(smsTimerSeconds) {
        if (smsTimerSeconds > 0) {
            delay(1000)
            smsTimerSeconds--
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .testTag("auth_screen")
    ) {
        val isWide = maxWidth >= 840.dp

        // SMS notification banner simulation at top
        AnimatedVisibility(
            visible = showSmsBanner,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Slate900,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .clickable {
                        smsCodeInput = generatedSmsCode
                        showSmsBanner = false
                    }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GreenSuccess),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sms,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SMS: Milliy Sertifikat Test Markazi",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF93C5FD)
                        )
                        Text(
                            text = "Tasdiqlash kodi: $generatedSmsCode (Nusxalash uchun bosing)",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Kiritish",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = GreenSuccess
                    )
                }
            }
        }

        if (isWide) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Hero Banner
                AuthHeroBanner(modifier = Modifier.weight(0.9f))

                // Right Form Container
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AuthMainCard(
                        activeTab = activeTab,
                        onTabChange = { activeTab = it },
                        selectedRole = selectedRole,
                        onRoleChange = { selectedRole = it },
                        phoneNumber = phoneNumber,
                        onPhoneNumberChange = { phoneNumber = it },
                        smsCodeInput = smsCodeInput,
                        onSmsCodeChange = { smsCodeInput = it },
                        isSmsSent = isSmsSent,
                        smsTimerSeconds = smsTimerSeconds,
                        onSendSms = {
                            val code = (100000..999999).random().toString()
                            generatedSmsCode = code
                            isSmsSent = true
                            smsTimerSeconds = 60
                            showSmsBanner = true
                        },
                        onPhoneSubmit = {
                            onPhoneLogin(phoneNumber, smsCodeInput, selectedRole)
                        },
                        emailInput = emailInput,
                        onEmailChange = { emailInput = it },
                        passwordInput = passwordInput,
                        onPasswordChange = { passwordInput = it },
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible },
                        onEmailSubmit = {
                            onLogin(emailInput, passwordInput, selectedRole)
                        },
                        onGoogleClick = { showGoogleChooser = true },
                        regFirstName = regFirstName,
                        onRegFirstNameChange = { regFirstName = it },
                        regLastName = regLastName,
                        onRegLastNameChange = { regLastName = it },
                        regFatherName = regFatherName,
                        onRegFatherNameChange = { regFatherName = it },
                        regBirthDay = regBirthDay,
                        onRegBirthDayChange = { regBirthDay = it },
                        regBirthMonth = regBirthMonth,
                        onRegBirthMonthChange = { regBirthMonth = it },
                        regBirthYear = regBirthYear,
                        onRegBirthYearChange = { regBirthYear = it },
                        regPhone = regPhone,
                        onRegPhoneChange = { regPhone = it },
                        regEmail = regEmail,
                        onRegEmailChange = { regEmail = it },
                        regAvatar = regAvatar,
                        onRegAvatarChange = { regAvatar = it },
                        allInterests = allInterests,
                        selectedInterests = selectedInterests,
                        onToggleInterest = { interest ->
                            if (selectedInterests.contains(interest)) {
                                selectedInterests.remove(interest)
                            } else {
                                selectedInterests.add(interest)
                            }
                        },
                        onRegisterSubmit = {
                            onRegister(
                                regFirstName,
                                regLastName,
                                regFatherName,
                                regBirthDay,
                                regBirthMonth,
                                regBirthYear,
                                selectedInterests.joinToString(", "),
                                regAvatar,
                                regPhone,
                                regEmail,
                                selectedRole,
                                "4190993" + (1000000..9999999).random().toString()
                            )
                        },
                        onDemoLogin = onDemoLogin,
                        modifier = Modifier.widthIn(max = 520.dp)
                    )
                }
            }
        } else {
            // Mobile Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(28.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "MILLIY SERTIFIKAT",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = BlueDark
                        )
                        Text(
                            text = "45 talik Rasch test tizimi",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Slate600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AuthMainCard(
                    activeTab = activeTab,
                    onTabChange = { activeTab = it },
                    selectedRole = selectedRole,
                    onRoleChange = { selectedRole = it },
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    smsCodeInput = smsCodeInput,
                    onSmsCodeChange = { smsCodeInput = it },
                    isSmsSent = isSmsSent,
                    smsTimerSeconds = smsTimerSeconds,
                    onSendSms = {
                        val code = (100000..999999).random().toString()
                        generatedSmsCode = code
                        isSmsSent = true
                        smsTimerSeconds = 60
                        showSmsBanner = true
                    },
                    onPhoneSubmit = {
                        onPhoneLogin(phoneNumber, smsCodeInput, selectedRole)
                    },
                    emailInput = emailInput,
                    onEmailChange = { emailInput = it },
                    passwordInput = passwordInput,
                    onPasswordChange = { passwordInput = it },
                    passwordVisible = passwordVisible,
                    onTogglePassword = { passwordVisible = !passwordVisible },
                    onEmailSubmit = {
                        onLogin(emailInput, passwordInput, selectedRole)
                    },
                    onGoogleClick = { showGoogleChooser = true },
                    regFirstName = regFirstName,
                    onRegFirstNameChange = { regFirstName = it },
                    regLastName = regLastName,
                    onRegLastNameChange = { regLastName = it },
                    regFatherName = regFatherName,
                    onRegFatherNameChange = { regFatherName = it },
                    regBirthDay = regBirthDay,
                    onRegBirthDayChange = { regBirthDay = it },
                    regBirthMonth = regBirthMonth,
                    onRegBirthMonthChange = { regBirthMonth = it },
                    regBirthYear = regBirthYear,
                    onRegBirthYearChange = { regBirthYear = it },
                    regPhone = regPhone,
                    onRegPhoneChange = { regPhone = it },
                    regEmail = regEmail,
                    onRegEmailChange = { regEmail = it },
                    regAvatar = regAvatar,
                    onRegAvatarChange = { regAvatar = it },
                    allInterests = allInterests,
                    selectedInterests = selectedInterests,
                    onToggleInterest = { interest ->
                        if (selectedInterests.contains(interest)) {
                            selectedInterests.remove(interest)
                        } else {
                            selectedInterests.add(interest)
                        }
                    },
                    onRegisterSubmit = {
                        onRegister(
                            regFirstName,
                            regLastName,
                            regFatherName,
                            regBirthDay,
                            regBirthMonth,
                            regBirthYear,
                            selectedInterests.joinToString(", "),
                            regAvatar,
                            regPhone,
                            regEmail,
                            selectedRole,
                            "4190993" + (1000000..9999999).random().toString()
                        )
                    },
                    onDemoLogin = onDemoLogin,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Google Account Chooser Dialog
        if (showGoogleChooser) {
            GoogleChooserDialog(
                onDismiss = { showGoogleChooser = false },
                onSelectAccount = { accEmail ->
                    showGoogleChooser = false
                    onLogin(accEmail, "google_oauth_pass", selectedRole)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AuthMainCard(
    activeTab: AuthTabMode,
    onTabChange: (AuthTabMode) -> Unit,
    selectedRole: UserRole,
    onRoleChange: (UserRole) -> Unit,
    // Phone
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    smsCodeInput: String,
    onSmsCodeChange: (String) -> Unit,
    isSmsSent: Boolean,
    smsTimerSeconds: Int,
    onSendSms: () -> Unit,
    onPhoneSubmit: () -> Unit,
    // Email
    emailInput: String,
    onEmailChange: (String) -> Unit,
    passwordInput: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePassword: () -> Unit,
    onEmailSubmit: () -> Unit,
    onGoogleClick: () -> Unit,
    // Register
    regFirstName: String,
    onRegFirstNameChange: (String) -> Unit,
    regLastName: String,
    onRegLastNameChange: (String) -> Unit,
    regFatherName: String,
    onRegFatherNameChange: (String) -> Unit,
    regBirthDay: Int,
    onRegBirthDayChange: (Int) -> Unit,
    regBirthMonth: Int,
    onRegBirthMonthChange: (Int) -> Unit,
    regBirthYear: Int,
    onRegBirthYearChange: (Int) -> Unit,
    regPhone: String,
    onRegPhoneChange: (String) -> Unit,
    regEmail: String,
    onRegEmailChange: (String) -> Unit,
    regAvatar: String,
    onRegAvatarChange: (String) -> Unit,
    allInterests: List<String>,
    selectedInterests: List<String>,
    onToggleInterest: (String) -> Unit,
    onRegisterSubmit: () -> Unit,
    onDemoLogin: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Role Selector Chips
            Text(
                text = "Tizimdagi rolingizni tanlang:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Slate600
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isStudent = selectedRole == UserRole.STUDENT
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isStudent) BluePrimary else Color(0xFFF1F5F9),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onRoleChange(UserRole.STUDENT) }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isStudent) Color.White else Slate700,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Talabgor / O'quvchi",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isStudent) Color.White else Slate700
                        )
                    }
                }

                val isTeacher = selectedRole == UserRole.TEACHER_CREATOR
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isTeacher) BluePrimary else Color(0xFFF1F5F9),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onRoleChange(UserRole.TEACHER_CREATOR) }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = if (isTeacher) Color.White else Slate700,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ustoz / Yaratuvchi",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isTeacher) Color.White else Slate700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Tabs
            TabRow(
                selectedTabIndex = activeTab.ordinal,
                containerColor = Color(0xFFF8FAFC),
                contentColor = BluePrimary,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = activeTab == AuthTabMode.PHONE_SMS,
                    onClick = { onTabChange(AuthTabMode.PHONE_SMS) },
                    text = { Text("📱 Telefon & SMS", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == AuthTabMode.EMAIL,
                    onClick = { onTabChange(AuthTabMode.EMAIL) },
                    text = { Text("📧 Email / Parol", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == AuthTabMode.FULL_REGISTER,
                    onClick = { onTabChange(AuthTabMode.FULL_REGISTER) },
                    text = { Text("📝 Yangi Profil", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            when (activeTab) {
                AuthTabMode.PHONE_SMS -> {
                    // Phone & SMS Input Flow
                    Text(
                        text = "Telefon raqamingiz orqali tezkor kirish",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate800
                    )
                    Text(
                        text = "Raqamingizga 6 xonali SMS tasdiqlash kodi yuboriladi",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        label = { Text("Telefon raqami") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = BluePrimary)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_phone_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!isSmsSent) {
                        Button(
                            onClick = onSendSms,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_send_sms"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SMS kod olish", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // SMS code input box
                        OutlinedTextField(
                            value = smsCodeInput,
                            onValueChange = { if (it.length <= 6) onSmsCodeChange(it) },
                            label = { Text("6 xonali SMS kod") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = GreenDark)
                            },
                            trailingIcon = {
                                if (smsCodeInput.length == 6) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenSuccess)
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text("Masalan: 849201") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_sms_code_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (smsTimerSeconds > 0) {
                                Text(
                                    text = "Qayta yuborish: ${smsTimerSeconds}s",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            } else {
                                TextButton(onClick = onSendSms) {
                                    Text("Kodni qayta yuborish", color = BluePrimary, fontSize = 12.sp)
                                }
                            }

                            Text(
                                text = "SMS yuborildi ✅",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = GreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = onPhoneSubmit,
                            enabled = smsCodeInput.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_verify_sms_login"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                        ) {
                            Text("Tasdiqlash va Kirish", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                AuthTabMode.EMAIL -> {
                    // Email & Password Flow
                    Text(
                        text = "Email va parol orqali kirish",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = onEmailChange,
                        label = { Text("Email manzili") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = BluePrimary)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = onPasswordChange,
                        label = { Text("Parol") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = BluePrimary)
                        },
                        trailingIcon = {
                            IconButton(onClick = onTogglePassword) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Ko'rsatish"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onEmailSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_email_login"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Text("Kirish", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Google Login Button
                    OutlinedButton(
                        onClick = onGoogleClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("google_login_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, tint = Color(0xFFEA4335))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Google hisobi orqali kirish", color = Slate800, fontWeight = FontWeight.SemiBold)
                    }
                }

                AuthTabMode.FULL_REGISTER -> {
                    // Full Profile Registration
                    Text(
                        text = "Shaxsiy ma'lumotlar va profil sozlamalari",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate800
                    )
                    Text(
                        text = "Sertifikatda aynan shu ma'lumotlar va tanlangan rasm chiqadi",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Avatar Picker
                    AvatarPickerRow(
                        selectedAvatarId = regAvatar,
                        onSelectAvatar = onRegAvatarChange,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Names
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = regLastName,
                            onValueChange = onRegLastNameChange,
                            label = { Text("Familiya") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = regFirstName,
                            onValueChange = onRegFirstNameChange,
                            label = { Text("Ism") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = regFatherName,
                        onValueChange = onRegFatherNameChange,
                        label = { Text("Otasining ismi") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date of birth (Day, Month, Year)
                    Text(
                        text = "Tug'ilgan sana (Kun, Oy, Yil):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate700
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = regBirthDay.toString(),
                            onValueChange = { onRegBirthDayChange(it.toIntOrNull()?.coerceIn(1, 31) ?: 1) },
                            label = { Text("Kun") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Month
                        val months = listOf("Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun", "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr")
                        var monthExpanded by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = monthExpanded,
                            onExpandedChange = { monthExpanded = !monthExpanded },
                            modifier = Modifier.weight(1.5f)
                        ) {
                            OutlinedTextField(
                                value = months.getOrElse(regBirthMonth - 1) { "Avgust" },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Oy") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = monthExpanded,
                                onDismissRequest = { monthExpanded = false }
                            ) {
                                months.forEachIndexed { idx, mName ->
                                    DropdownMenuItem(
                                        text = { Text(mName) },
                                        onClick = {
                                            onRegBirthMonthChange(idx + 1)
                                            monthExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = regBirthYear.toString(),
                            onValueChange = { onRegBirthYearChange(it.toIntOrNull()?.coerceIn(1950, 2015) ?: 2004) },
                            label = { Text("Yil") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1.2f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Interests Multi-select Chips
                    Text(
                        text = "Qiziqishlaringiz va yo'nalishlar:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate700
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        allInterests.forEach { interest ->
                            val isSel = selectedInterests.contains(interest)
                            FilterChip(
                                selected = isSel,
                                onClick = { onToggleInterest(interest) },
                                label = { Text(interest, fontSize = 11.sp) },
                                leadingIcon = if (isSel) {
                                    { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BluePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phone & Email in register
                    OutlinedTextField(
                        value = regPhone,
                        onValueChange = onRegPhoneChange,
                        label = { Text("Telefon raqami") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = onRegEmailChange,
                        label = { Text("Email manzili") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRegisterSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_complete_registration"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Profilni saqlash va Kirish", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Slate200)
            Spacer(modifier = Modifier.height(12.dp))

            // Demo Quick Logins
            Text(
                text = "⚡ Sinab ko'rish uchun tezkor demo hisoblar:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Slate500
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onDemoLogin(UserRole.TEACHER_CREATOR) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("👨‍🏫 Ustoz (Azizbek)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onDemoLogin(UserRole.STUDENT) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("👩‍🎓 Talaba (Elmira)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AuthHeroBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF1E3A8A), Color(0xFF1E40AF))
                )
            )
            .padding(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Hero Logo",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "MILLIY SERTIFIKAT\nONLAYN IMTIHON TIZIMI",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Bilim va malakalarni baholash agentligi talablari asosidagi 45 ta savolli professional test, Rasch modeli va QR-kodli elektron sertifikat platformasi.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFBFDBFE),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            HeroFeatureBadge("45 ta namunaviy savollar (yopiq, ochiq va esse)")
            HeroFeatureBadge("Rasch xalqaro shkalali ball hisoblash tizimi")
            HeroFeatureBadge("3x4 fotosuratli va verifikatsiyali QR-kodli Sertifikat")
            HeroFeatureBadge("Ona tili, Ingliz tili (Audio), Matematika va barcha fanlar")
        }
    }
}

@Composable
private fun HeroFeatureBadge(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(GreenSuccess.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4ADE80),
                modifier = Modifier.size(13.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFFF1F5F9)
        )
    }
}

@Composable
private fun GoogleChooserDialog(
    onDismiss: () -> Unit,
    onSelectAccount: (String) -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color(0xFF4285F4),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Google hisobini tanlang",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Slate800
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                listOf(
                    Pair("Azizbek Abduqodirov", "azizbek.test@gmail.com"),
                    Pair("Elmiraxon Toirova", "elmiraxon.talaba@gmail.com"),
                    Pair("Sardor Pirmamatov", "sardor.math@gmail.com")
                ).forEach { (name, gEmail) ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectAccount(gEmail) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name.take(1),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(text = gEmail, style = MaterialTheme.typography.bodySmall, color = Slate500)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Bekor qilish", color = Slate600)
                }
            }
        }
    }
}
