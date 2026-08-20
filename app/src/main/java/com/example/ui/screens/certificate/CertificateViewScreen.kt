package com.example.ui.screens.certificate

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.StudentSubmissionEntity
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun CertificateViewScreen(
    submission: StudentSubmissionEntity,
    subjectTitle: String = "Ona tili va adabiyot",
    onBack: () -> Unit,
    onDownloadPdf: () -> Unit,
    onSharePdf: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("certificate_view_screen")
    ) {
        // Top Action Toolbar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Orqaga")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Milliy Sertifikat",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = submission.certificateId ?: "UZ26 641200",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onSharePdf,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("share_cert_button")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ulashish", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDownloadPdf,
                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("download_cert_pdf_button")
                    ) {
                        Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("PDF yuklash", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Certificate Parchment Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            OfficialCertificateParchment(
                submission = submission,
                subjectTitle = subjectTitle
            )
        }
    }
}

@Composable
private fun OfficialCertificateParchment(
    submission: StudentSubmissionEntity,
    subjectTitle: String
) {
    // Official Certificate Card with Double Gold/Green Security Border
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 680.dp)
            .shadow(10.dp, RoundedCornerShape(8.dp))
            .border(4.dp, GoldPrimary, RoundedCornerShape(8.dp))
            .border(8.dp, Color(0xFF0F766E).copy(alpha = 0.15f), RoundedCornerShape(6.dp)),
        color = Color(0xFFFCFBF7), // Warm authentic parchment texture
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background security guilloche fine lines
            Canvas(modifier = Modifier.matchParentSize()) {
                val step = 28f
                for (x in 0..(size.width.toInt()) step step.toInt()) {
                    drawLine(
                        color = Color(0xFF0D9488).copy(alpha = 0.025f),
                        start = Offset(x.toFloat(), 0f),
                        end = Offset(0f, x.toFloat()),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color(0xFFD97706).copy(alpha = 0.02f),
                        start = Offset(x.toFloat(), size.height),
                        end = Offset(size.width, size.height - x.toFloat()),
                        strokeWidth = 1f
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Official Republic Header & Coat of Arms Emblem
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFF0284C7))
                                )
                            )
                            .border(2.5.dp, GoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("★", fontSize = 10.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                            Text("UZB", fontSize = 11.sp, color = Color(0xFF0369A1), fontWeight = FontWeight.ExtraBold)
                            Text("★", fontSize = 8.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "O'ZBEKISTON RESPUBLIKASI OLIY TA'LIM, FAN VA INNOVATSIYALAR VAZIRLIGI\nHUZURIDAGI BILIM VA MALAKALARNI BAHOLASH AGENTLIGI",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.6.sp,
                        lineHeight = 16.sp
                    ),
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Decorative Gold Ribbon Divider
                Canvas(modifier = Modifier.fillMaxWidth(0.9f).height(4.dp)) {
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, GoldPrimary, Color(0xFF0D9488), GoldPrimary, Color.Transparent)
                        ),
                        start = Offset(0f, 2f),
                        end = Offset(size.width, 2f),
                        strokeWidth = 3f
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "UMUMTA'LIM FANINI BILISH DARAJASI\nTO'G'RISIDA SERTIFIKAT",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp,
                        lineHeight = 22.sp
                    ),
                    color = Color(0xFF0F766E),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Certificate Serial & Barcode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seriya: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600
                    )
                    Text(
                        text = submission.certificateId ?: "UZ26 № 641200",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp),
                        color = Color(0xFFB91C1C)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Student Identity Row with 3x4 Photo and Details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.8f))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CertFieldLine(label = "Shaxsiy kod (JSHSHIR):", value = submission.studentPersonalCode.ifBlank { "41909931330028" }, isBold = true)
                        CertFieldLine(label = "Familiyasi:", value = submission.studentLastName.ifBlank { "KARIMOV" }.uppercase(), isBold = true)
                        CertFieldLine(label = "Ismi:", value = submission.studentFirstName.ifBlank { "ANVAR" }.uppercase(), isBold = true)
                        CertFieldLine(label = "Otasining ismi:", value = submission.studentFatherName.ifBlank { "RUSTAM O'G'LI" }.uppercase(), isBold = true)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Photo box with official agency seal stamp and user selected avatar
                    Box(
                        modifier = Modifier
                            .size(width = 90.dp, height = 118.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.5.dp, Color(0xFF0F766E), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        com.example.ui.components.UserAvatarView(
                            avatarId = submission.studentAvatarUrl,
                            fullName = "${submission.studentLastName} ${submission.studentFirstName}",
                            size = 80.dp,
                            is3x4PassportStyle = true,
                            modifier = Modifier.padding(4.dp)
                        )

                        // 3x4 label overlay at top
                        Surface(
                            shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
                            color = Color(0xFF0F766E).copy(alpha = 0.85f),
                            modifier = Modifier.align(Alignment.TopCenter)
                        ) {
                            Text(
                                text = "3×4 FOTO",
                                fontSize = 8.sp,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }

                        // Round watermark stamp at bottom-right corner of photo
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color(0xFF0284C7).copy(alpha = 0.22f))
                                .border(1.5.dp, Color(0xFF0284C7).copy(alpha = 0.7f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "BMBA",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0369A1)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Subject, Rasch Score & Level Performance Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CertFieldLine(label = "Umumta'lim fani:", value = subjectTitle, isBold = true, highlightValue = true)

                        val raschScore = if (submission.raschScaledScore > 0) "%.2f ball".format(submission.raschScaledScore) else "70.32 ball"
                        CertFieldLine(label = "Rasch shkalasi bo'yicha umumiy ball:", value = raschScore, isBold = true)

                        val percentDisplay = if (submission.percentage > 0) "%.1f %%".format(submission.percentage) else "100.0 %"
                        CertFieldLine(label = "Umumiy ballga nisbatan foiz ko'rsatkichi:", value = percentDisplay, isBold = true)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Sertifikat darajasi:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Slate800
                            )
                            Surface(
                                color = if (submission.certificateLevel.isPassing()) GreenContainer else Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (submission.certificateLevel.isPassing()) GreenDark else Color(0xFFD97706))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = if (submission.certificateLevel.isPassing()) GreenDark else Color(0xFFD97706),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Daraja ${submission.certificateLevel.displayName}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (submission.certificateLevel.isPassing()) GreenDark else Color(0xFF92400E)
                                    )
                                }
                            }
                        }

                        Divider(color = Slate200, modifier = Modifier.padding(vertical = 2.dp))

                        // Score breakdown: Test sinovi (1-44) va Yozma ish (45)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val testScore = if (submission.testScorePart > 0) "%.2f ball".format(submission.testScorePart) else "69.64 ball"
                            Text(text = "• Test sinovi (1-44): $testScore", fontSize = 11.sp, color = Slate600)

                            val essayScore = if (submission.writtenScorePart > 0) "%.2f ball".format(submission.writtenScorePart) else "71.00 ball"
                            Text(text = "• Yozma ish (45): $essayScore", fontSize = 11.sp, color = Slate600)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Footer: Verification QR Code, Director Signature & Seal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Berilgan sana: ${submission.certificateIssueDate ?: "10.03.2026"}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Slate700
                        )
                        Text(
                            text = "Amal qilish muddati: 3 yil (2029-yilgacha)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                            color = Slate700
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Agentlik direktori:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )
                    }

                    // Authentic QR Code Box
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier
                                .size(64.dp)
                                .border(1.dp, Color(0xFF0F172A), RoundedCornerShape(2.dp)),
                            color = Color.White
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                                drawRect(Color.Black, Offset(0f, 0f), Size(16f, 16f))
                                drawRect(Color.White, Offset(3f, 3f), Size(10f, 10f))
                                drawRect(Color.Black, Offset(5f, 5f), Size(6f, 6f))

                                drawRect(Color.Black, Offset(size.width - 16f, 0f), Size(16f, 16f))
                                drawRect(Color.White, Offset(size.width - 13f, 3f), Size(10f, 10f))
                                drawRect(Color.Black, Offset(size.width - 11f, 5f), Size(6f, 6f))

                                drawRect(Color.Black, Offset(0f, size.height - 16f), Size(16f, 16f))
                                drawRect(Color.White, Offset(3f, size.height - 13f), Size(10f, 10f))
                                drawRect(Color.Black, Offset(5f, size.height - 11f), Size(6f, 6f))

                                drawCircle(Color.Black, 3f, Offset(size.width / 2f, size.height / 2f))
                                drawCircle(Color.Black, 2f, Offset(size.width / 2f + 8f, size.height / 2f + 8f))
                            }
                        }
                        Text(
                            text = "uzbmb.uz/verify",
                            fontSize = 8.sp,
                            color = Slate500,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Official Round Blue Seal & Signature
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "M.M. KARIMOV",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )

                        // Handwritten blue signature flourish line
                        Canvas(modifier = Modifier.size(width = 80.dp, height = 18.dp)) {
                            val path = Path().apply {
                                moveTo(0f, 12f)
                                quadraticTo(20f, 2f, 40f, 10f)
                                quadraticTo(60f, 18f, 80f, 6f)
                            }
                            drawPath(
                                path = path,
                                color = Color(0xFF1D4ED8),
                                style = Stroke(width = 2.5f)
                            )
                        }

                        Text(
                            text = "M.O'. (Rasmiy muhr)",
                            fontSize = 9.sp,
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Microsecurity text footer
                Text(
                    text = "• BILIM VA MALAKALARNI BAHOLASH AGENTLIGI • RASMIY DAVLAT SERTIFIKATI • RASCH MODELI •",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp, letterSpacing = 1.sp),
                    color = Slate400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CertFieldLine(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlightValue: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Slate600
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (highlightValue) 14.sp else 13.sp
            ),
            color = if (highlightValue) Color(0xFF0F766E) else Slate900
        )
    }
}
