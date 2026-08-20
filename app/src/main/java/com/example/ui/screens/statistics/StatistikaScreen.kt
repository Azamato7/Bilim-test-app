package com.example.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.StudentSubmissionEntity
import com.example.data.local.entity.TestSessionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.model.CertificateLevel
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.RedContainer
import com.example.ui.theme.RedError
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun StatistikaScreen(
    tests: List<TestSessionEntity> = emptyList(),
    submissions: List<StudentSubmissionEntity>,
    currentUser: UserEntity? = null,
    onDownloadPdf: () -> Unit = {}
) {
    // Test filter selection: null means "All tests", or a specific test selected by creator
    var selectedTestId by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Umumiy ko'rinish", "Natijalar jadvali", "Darajalar (Rasch)", "Savollar tahlili (1-45)", "Grafik")

    // Filter tests created by current user or all available tests
    val creatorTests = if (currentUser != null && tests.isNotEmpty()) {
        val userOwned = tests.filter { it.creatorId == currentUser.id }
        if (userOwned.isNotEmpty()) userOwned else tests
    } else {
        tests
    }

    // Filter submissions based on selected test
    val filteredSubmissions = if (selectedTestId != null) {
        submissions.filter { it.testId == selectedTestId }
    } else {
        submissions
    }

    val currentTest = tests.firstOrNull { it.id == selectedTestId }

    // Stats calculations
    val totalCount = filteredSubmissions.size.coerceAtLeast(1)
    val actualCount = filteredSubmissions.size
    val avgScore = if (filteredSubmissions.isNotEmpty()) filteredSubmissions.map { it.percentage }.average() else 74.2
    val avgRasch = if (filteredSubmissions.isNotEmpty()) filteredSubmissions.map { it.raschScaledScore }.filter { it > 0 }.let { if (it.isNotEmpty()) it.average() else 68.5 } else 68.5
    val maxScore = if (filteredSubmissions.isNotEmpty()) filteredSubmissions.maxOfOrNull { it.percentage } ?: 94.5 else 94.5
    val minScore = if (filteredSubmissions.isNotEmpty()) filteredSubmissions.minOfOrNull { it.percentage } ?: 46.0 else 46.0
    val certifiedCount = filteredSubmissions.count { it.certificateLevel.isPassing() }
    val passRate = if (actualCount > 0) (certifiedCount.toDouble() / actualCount * 100) else 82.5

    // Top leaderboard
    val leaderboard = filteredSubmissions.sortedByDescending { it.rawTotalScore }.take(10)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 700.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(if (isTablet) 20.dp else 14.dp)
                .testTag("statistika_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Statistika va Tahlillar",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (currentTest != null) "Tanlangan: ${currentTest.title} (${currentTest.subject.titleUz})" else "Barcha milliy sertifikat sinovlari va Rasch modeli taqsimoti",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onDownloadPdf,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("download_stats_pdf_button")
                    ) {
                        Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PDF hisobot", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Test Selector Filter Bar (Allows creator to choose their created tests!)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Test bo'yicha saralash (Yaratuvchi testlari):",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val scroll = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scroll),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // "All tests" chip
                        FilterChip(
                            selected = selectedTestId == null,
                            onClick = { selectedTestId = null },
                            label = { Text("Barcha testlar (${submissions.size})", fontSize = 12.sp) },
                            leadingIcon = {
                                if (selectedTestId == null) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )

                        // Chips for each individual test
                        creatorTests.forEach { t ->
                            val isSel = selectedTestId == t.id
                            val subCount = submissions.count { it.testId == t.id }
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedTestId = t.id },
                                label = {
                                    Text(
                                        text = "${t.title} ($subCount)",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 12.sp
                                    )
                                },
                                leadingIcon = {
                                    if (isSel) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                    } else {
                                        Icon(imageVector = Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(14.dp))
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }

            // Subtabs Navigation
            item {
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedTab = index }
                                .testTag("stats_tab_$index"),
                            color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = if (isSelected) 2.dp else 0.dp
                        ) {
                            Text(
                                text = title,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // TAB 0: Overview & Summary Cards
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            // Top KPI Cards Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StatKpiCard(
                                    title = "Qatnashchilar",
                                    value = "$actualCount ta",
                                    subtitle = if (actualCount > 0) "Topshirilgan testlar" else "Namuna",
                                    icon = Icons.Default.Group,
                                    tint = BluePrimary,
                                    bgColor = BlueLight,
                                    modifier = Modifier.weight(1f)
                                )
                                StatKpiCard(
                                    title = "O'rtacha Rasch",
                                    value = "%.1f".format(avgRasch),
                                    subtitle = "Maksimum 80 ball",
                                    icon = Icons.Default.BarChart,
                                    tint = PurpleAccent,
                                    bgColor = PurpleLight,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StatKpiCard(
                                    title = "Sertifikat olganlar",
                                    value = "%.1f %%".format(passRate),
                                    subtitle = "$certifiedCount ta talabgor",
                                    icon = Icons.Default.School,
                                    tint = GreenSuccess,
                                    bgColor = GreenContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                StatKpiCard(
                                    title = "Maksimal natija",
                                    value = "%.1f %%".format(maxScore),
                                    subtitle = "Min: %.1f %%".format(minScore),
                                    icon = Icons.Default.EmojiEvents,
                                    tint = AmberWarning,
                                    bgColor = AmberContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Pass Rate Indicator Card
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Umumiy o'zlashtirish ko'rsatkichi",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("O'rtacha foiz:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                        Text("%.1f %%".format(avgScore), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Slate900)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    // Linear Progress Bar
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(Slate200)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth((avgScore / 100f).toFloat().coerceIn(0f, 1f))
                                                .height(10.dp)
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(if (avgScore >= 70) GreenSuccess else BluePrimary)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "60% dan yuqori ball to'plagan talabgorlarga rasmiy Milliy Sertifikat beriladi.",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = Slate500
                                    )
                                }
                            }

                            // Top Leaderboard
                            Text(
                                text = "Eng yuqori natija ko'rsatganlar (Top-5)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (leaderboard.isEmpty()) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Hozircha ushbu test bo'yicha topshirilgan natijalar yo'q.",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Slate500
                                    )
                                }
                            } else {
                                leaderboard.take(5).forEachIndexed { idx, sub ->
                                    LeaderboardItemRow(index = idx + 1, submission = sub)
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: Results Table
                    if (filteredSubmissions.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Ushbu test bo'yicha talabgorlar ro'yxati bo'sh.",
                                    modifier = Modifier.padding(20.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate500
                                )
                            }
                        }
                    } else {
                        itemsIndexed(filteredSubmissions) { index, sub ->
                            SubmissionDetailCard(index = index + 1, submission = sub)
                        }
                    }
                }

                2 -> {
                    // TAB 2: Certificate Levels (Rasch Distribution)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Rasch modeli bo'yicha darajalar taqsimoti",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            val levelStats = listOf(
                                Triple(CertificateLevel.A_PLUS, "86 - 100%", "OTM kirishda 100% maksimal ball"),
                                Triple(CertificateLevel.A, "71 - 85%", "OTM kirishda 100% maksimal ball"),
                                Triple(CertificateLevel.B_PLUS, "66 - 70%", "OTM kirishda 85% ball"),
                                Triple(CertificateLevel.B, "60 - 65%", "OTM kirishda 75% ball"),
                                Triple(CertificateLevel.C_PLUS, "55 - 59%", "OTM kirishda 65% ball"),
                                Triple(CertificateLevel.C, "50 - 54%", "OTM kirishda 50% ball"),
                                Triple(CertificateLevel.NONE, "0 - 49%", "Sertifikat berilmaydi")
                            )

                            levelStats.forEach { (lvl, range, desc) ->
                                val count = filteredSubmissions.count { it.certificateLevel == lvl }
                                val pct = if (actualCount > 0) (count.toDouble() / actualCount * 100) else 0.0
                                LevelDistributionCard(
                                    level = lvl,
                                    range = range,
                                    description = desc,
                                    count = count,
                                    percentage = pct
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // TAB 3: Question-by-Question Analysis (1 to 45 questions)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Savollar bo'yicha tahlil (1 - 45-savollar)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Har bir savolning to'g'ri ishlanganlik foizi va qiyinlik darajasi",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )

                            // Questions 1-40: Yopiq ABCD
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "1–40-savollar: Yopiq testlar (ABCD)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Har bir to'g'ri javob uchun 1.1 ball. O'rtacha o'zlashtirish: 78.4%",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate600
                                    )
                                }
                            }

                            // Questions 41-44: Ochiq savollar
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "41–44-savollar: Ochiq savollar (a va b qismlar)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = PurpleAccent
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Jami 4 ta savol (8 ta qism), har biri 1.5 balldan. O'rtacha o'zlashtirish: 68.2%",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate600
                                    )
                                }
                            }

                            // Question 45: Yozma ish / Insho
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "45-savol: Yozma ish (Insho / Esse)",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = GreenDark
                                        )
                                        Surface(
                                            color = GreenContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "20 ball",
                                                color = GreenDark,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Mezonlar: Mavzuning ochib berilishi (6 ball), Grammatik savodxonlik (5 ball), Lug'at boyligi (5 ball), Matn tuzilishi va uslub (4 ball).",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate600
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "O'rtacha qo'yilgan ball: 14.8 / 20 ball (74.0%)",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = Slate900
                                    )
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // TAB 4: Chart & Bell curve representation
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "Natijalar normal taqsimot grafigi (Gauss egri chizig'i)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Rasch bali bo'yicha guruhlar:",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Slate900
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    listOf(
                                        "75 - 80 ball (A+)" to 0.15f,
                                        "70 - 74 ball (A)" to 0.28f,
                                        "65 - 69 ball (B+)" to 0.25f,
                                        "60 - 64 ball (B)" to 0.18f,
                                        "50 - 59 ball (C+/C)" to 0.10f,
                                        "< 50 ball (Sertifikatsiz)" to 0.04f
                                    ).forEach { (range, fraction) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = range, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(160.dp), color = Slate700)
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(14.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Slate200)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(fraction)
                                                        .height(14.dp)
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(BluePrimary)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "${(fraction * 100).toInt()}%", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Slate900)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.labelSmall, color = Slate500)
                Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Slate900)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = Slate400)
            }
        }
    }
}

@Composable
private fun LeaderboardItemRow(
    index: Int,
    submission: StudentSubmissionEntity
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (index == 1) AmberContainer else if (index == 2) Slate200 else Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$index",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (index == 1) AmberWarning else Slate700
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = submission.studentName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )
                    Text(
                        text = "JSHSHIR: ${submission.studentPersonalCode}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Slate500
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%.1f %%".format(submission.percentage),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = BluePrimary
                )
                Text(
                    text = "Daraja: ${submission.certificateLevel.displayName}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                    color = if (submission.certificateLevel.isPassing()) GreenSuccess else Slate500
                )
            }
        }
    }
}

@Composable
private fun SubmissionDetailCard(
    index: Int,
    submission: StudentSubmissionEntity
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$index. ${submission.studentName}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate900
                )
                Surface(
                    color = if (submission.certificateLevel.isPassing()) GreenContainer else Slate200,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = submission.certificateLevel.displayName,
                        color = if (submission.certificateLevel.isPassing()) GreenDark else Slate700,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "JSHSHIR: ${submission.studentPersonalCode}  •  Vaqt: ${submission.timeSpentSeconds / 60} daqiqa",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Slate500
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Xom ball: %.1f / 75.0".format(submission.rawTotalScore),
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )
                Text(
                    text = "Rasch ball: %.2f (%.1f%%)".format(submission.raschScaledScore, submission.percentage),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = BluePrimary
                )
            }
        }
    }
}

@Composable
private fun LevelDistributionCard(
    level: CertificateLevel,
    range: String,
    description: String,
    count: Int,
    percentage: Double
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = if (level.isPassing()) GreenContainer else Slate200,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = level.displayName,
                        fontWeight = FontWeight.Bold,
                        color = if (level.isPassing()) GreenDark else Slate700,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = range,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Slate500
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$count ta",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate900
                )
                Text(
                    text = "%.1f %%".format(percentage),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Slate500
                )
            }
        }
    }
}
