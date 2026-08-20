package com.example.ui.screens.help

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun HelpScreen() {
    val context = LocalContext.current

    fun openTelegram() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Bilimtest_n1"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Telegram kanal: https://t.me/Bilimtest_n1", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .testTag("help_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Yordam va Yo'riqnoma",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Milliy sertifikat sinovi, AI generator va Rasch modeli baholash tartibi",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500
                )
            }
        }

        // Section 0: Telegram Guruhi & Aloqa (Highlighted Card)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { openTelegram() },
                shape = RoundedCornerShape(16.dp),
                color = BluePrimary,
                shadowElevation = 3.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Rasmiy Telegram Guruhimiz",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "Savol-javob, savollar bazasi va doimiy aloqa",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Ochish",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "https://t.me/Bilimtest_n1 (@Bilimtest_n1)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )

                            Button(
                                onClick = { openTelegram() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = BluePrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Guruhga qo'shilish", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Section 1: Ustamalar jadvali
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.School, contentDescription = null, tint = BluePrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Milliy sertifikat darajalari va ustama miqdorlari",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val levelsInfo = listOf(
                        Triple("A+ daraja", "86% dan 100% gacha", "50% oylik ustama  •  OTMga 100% maksimal ball"),
                        Triple("A daraja", "80% dan 85.9% gacha", "50% oylik ustama  •  OTMga 100% maksimal ball"),
                        Triple("B+ daraja", "75% dan 79.9% gacha", "20% oylik ustama"),
                        Triple("B daraja", "70% dan 74.9% gacha", "20% oylik ustama"),
                        Triple("C+ daraja", "65% dan 69.9% gacha", "Sertifikat (ustamasiz)"),
                        Triple("C daraja", "60% dan 64.9% gacha", "Sertifikat (ustamasiz)")
                    )

                    levelsInfo.forEach { (level, range, bonus) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = GreenContainer,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.width(100.dp)
                            ) {
                                Text(
                                    text = level,
                                    color = GreenDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = range, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(180.dp))
                            Text(text = bonus, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Divider(color = Slate200.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Section 2: 45 ta savol strukturasi, PDF kitobcha & AI
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = BluePrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "45 ta standart savol tuzilishi va PDF kitobcha",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tizimda har qanday fan bo'yicha yaratilgan test qat'iy 45 ta savoldan iborat bo'ladi:\n\n" +
                                "1. 1–35-savollar: Yopiq turdagi test savollari (ABCD variantli bitta to'g'ri javob);\n" +
                                "2. 36–39-savollar: Moslashtirish yoki tanlash yopiq testlari;\n" +
                                "3. 40–44-savollar: Ochiq turdagi savollar (a va b bandli yozma javoblar);\n" +
                                "4. 45-savol: Insho (Esse) — kamida 120 so'zdan iborat erkin ijodiy yozma ish.\n\n" +
                                "• Gemini AI yordamida testdagi barcha 45 ta savolni to'liq matni, chizma rasmlari va to'g'ri kalitlari bilan 1 tugma orqali to'ldirish mumkin.\n" +
                                "• Test uchun yagona PDF Savollar Kitobchasi generatsiya qilinadi va o'quvchilar PDF ko'rinishida yechishlari mumkin.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Section 3: Rasch modeli
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PurpleAccent)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Rasch modeli (Item Response Theory) haqida",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Rasch modeli — bu zamonaviy psixometrik baholash modeli bo'lib, unda talabgorning yakuniy bali faqat to'g'ri javoblar soniga emas, balki yechilgan savollarning qiyinlik darajasiga (b_i) ham bog'liq bo'ladi. " +
                                "Kamchilik to'g'ri topgan qiyin savollarga javob berish talabgorning qobiliyat logitini (theta) yuqoriroq qiladi va shunga asosan milliy shkalaga (200-800 ball) o'tkaziladi.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

