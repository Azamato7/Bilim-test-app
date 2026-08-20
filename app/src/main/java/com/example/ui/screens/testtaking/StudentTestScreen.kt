package com.example.ui.screens.testtaking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.TestSessionEntity
import com.example.data.model.QuestionType
import com.example.data.model.StudentAnswer
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
import com.example.ui.theme.RedError
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700

@Composable
fun StudentTestScreen(
    test: TestSessionEntity,
    questions: List<QuestionEntity>,
    currentIndex: Int,
    answers: Map<Int, StudentAnswer>,
    remainingSeconds: Long,
    onSelectQuestion: (Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onAnswerClosed: (qNum: Int, option: String) -> Unit,
    onAnswerOpen: (qNum: Int, partA: String, partB: String) -> Unit,
    onAnswerEssay: (qNum: Int, text: String) -> Unit,
    onToggleBookmark: (qNum: Int) -> Unit,
    onSubmitExam: () -> Unit,
    onDownloadPdf: () -> Unit
) {
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }

    // Format remaining time hh:mm:ss
    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = "%02d:%02d:%02d".format(hours, minutes, seconds)

    val currentQ = questions.getOrNull(currentIndex)
    val currentAns = currentQ?.let { answers[it.questionNumber] } ?: StudentAnswer(questionNumber = currentIndex + 1)

    // Calculate progress stats
    val answeredCount = questions.count { q ->
        val a = answers[q.questionNumber]
        a != null && a.isAnswered(q.type)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("student_exam_screen")
    ) {
        val isWide = maxWidth >= 840.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Header Bar
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = test.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                        Text(
                            text = "Javob berilgan: $answeredCount / 45 ta savol",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Countdown Timer Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (remainingSeconds < 300) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (remainingSeconds < 300) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = timeFormatted,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = if (remainingSeconds < 300) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Submit Button
                        Button(
                            onClick = { showSubmitConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("submit_exam_top_button")
                        ) {
                            Text("Topshirish", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Body: Split or Stack
            if (isWide) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Question Box
                    Box(modifier = Modifier.weight(2.5f)) {
                        if (currentQ != null) {
                            QuestionTakingCard(
                                question = currentQ,
                                answer = currentAns,
                                isFirst = currentIndex == 0,
                                isLast = currentIndex == questions.size - 1,
                                onNext = onNext,
                                onPrev = onPrev,
                                onAnswerClosed = { opt -> onAnswerClosed(currentQ.questionNumber, opt) },
                                onAnswerOpen = { a, b -> onAnswerOpen(currentQ.questionNumber, a, b) },
                                onAnswerEssay = { text -> onAnswerEssay(currentQ.questionNumber, text) },
                                onToggleBookmark = { onToggleBookmark(currentQ.questionNumber) },
                                onSubmitClick = { showSubmitConfirmDialog = true }
                            )
                        }
                    }

                    // Navigator Box
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 1.dp
                    ) {
                        QuestionGridNavigator(
                            questions = questions,
                            currentIndex = currentIndex,
                            answers = answers,
                            onSelectIndex = onSelectQuestion
                        )
                    }
                }
            } else {
                // Mobile layout
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (currentQ != null) {
                        QuestionTakingCard(
                            question = currentQ,
                            answer = currentAns,
                            isFirst = currentIndex == 0,
                            isLast = currentIndex == questions.size - 1,
                            onNext = onNext,
                            onPrev = onPrev,
                            onAnswerClosed = { opt -> onAnswerClosed(currentQ.questionNumber, opt) },
                            onAnswerOpen = { a, b -> onAnswerOpen(currentQ.questionNumber, a, b) },
                            onAnswerEssay = { text -> onAnswerEssay(currentQ.questionNumber, text) },
                            onToggleBookmark = { onToggleBookmark(currentQ.questionNumber) },
                            onSubmitClick = { showSubmitConfirmDialog = true }
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 1.dp
                    ) {
                        QuestionGridNavigator(
                            questions = questions,
                            currentIndex = currentIndex,
                            answers = answers,
                            onSelectIndex = onSelectQuestion
                        )
                    }
                }
            }
        }
    }

    if (showSubmitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            title = { Text("Testni yakunlashni tasdiqlaysizmi?", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Jami savollar: 45 ta")
                    Text("Javob berilgan: $answeredCount ta")
                    Text(
                        "Javob berilmagan: ${45 - answeredCount} ta",
                        color = if (45 - answeredCount > 0) RedError else GreenDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Testni topshirganingizdan so'ng uni qayta tahrirlay olmaysiz. Natijalar o'qituvchi testni yakunlab, sertifikatlarni chiqargandan so'ng Rasch modeli orqali hisoblanadi.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        onSubmitExam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                    modifier = Modifier.testTag("confirm_submit_exam_dialog")
                ) {
                    Text("Ha, topshirish")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmDialog = false }) {
                    Text("Davom etish")
                }
            }
        )
    }
}

@Composable
private fun QuestionTakingCard(
    question: QuestionEntity,
    answer: StudentAnswer,
    isFirst: Boolean,
    isLast: Boolean,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onAnswerClosed: (String) -> Unit,
    onAnswerOpen: (String, String) -> Unit,
    onAnswerEssay: (String) -> Unit,
    onToggleBookmark: () -> Unit,
    onSubmitClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Question Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Savol ${question.questionNumber} / 45",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val typeLabel = when (question.type) {
                            QuestionType.CLOSED_ABCD -> "Yopiq"
                            QuestionType.OPEN_TWO_PARTS -> "Ochiq"
                            QuestionType.ESSAY -> "Insho"
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = typeLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Bookmark flag button
                    OutlinedButton(
                        onClick = onToggleBookmark,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (answer.isMarkedForReview) AmberContainer else Color.Transparent
                        ),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("bookmark_button_${question.questionNumber}")
                    ) {
                        Icon(
                            imageVector = if (answer.isMarkedForReview) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Belgilash",
                            tint = if (answer.isMarkedForReview) AmberWarning else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (answer.isMarkedForReview) "Belgilangan" else "Belgilash",
                            color = if (answer.isMarkedForReview) AmberWarning else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Audio Player (If English listening or audio attached)
                if (!question.audioUrl.isNullOrBlank()) {
                    com.example.ui.components.QuestionAudioPlayer(
                        audioUrl = question.audioUrl,
                        audioTitle = question.audioTitle,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Image / Diagram (If graphic or diagram attached)
                if (!question.imageUrl.isNullOrBlank()) {
                    com.example.ui.components.QuestionImageView(
                        imageUrl = question.imageUrl,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Question Text
                Text(
                    text = question.questionText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Question Inputs
                when (question.type) {
                    QuestionType.CLOSED_ABCD -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(
                                "A" to (question.optionA ?: "A varianti"),
                                "B" to (question.optionB ?: "B varianti"),
                                "C" to (question.optionC ?: "C varianti"),
                                "D" to (question.optionD ?: "D varianti")
                            ).forEach { (optKey, optText) ->
                                val isSelected = answer.selectedOption.equals(optKey, ignoreCase = true)
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { onAnswerClosed(optKey) }
                                        .testTag("option_${optKey}_q_${question.questionNumber}"),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { onAnswerClosed(optKey) },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "$optKey) $optText",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    QuestionType.OPEN_TWO_PARTS -> {
                        var partA by remember(question.questionNumber, answer.openAnswerA) { mutableStateOf(answer.openAnswerA) }
                        var partB by remember(question.questionNumber, answer.openAnswerB) { mutableStateOf(answer.openAnswerB) }

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "a) ${question.openPartAPrompt ?: "1-qism javobini kiriting:"}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = partA,
                                onValueChange = {
                                    partA = it
                                    onAnswerOpen(it, partB)
                                },
                                placeholder = { Text("Javobingizni bu yerga yozing...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("open_input_a_q_${question.questionNumber}"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "b) ${question.openPartBPrompt ?: "2-qism javobini kiriting:"}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = partB,
                                onValueChange = {
                                    partB = it
                                    onAnswerOpen(partA, it)
                                },
                                placeholder = { Text("Javobingizni bu yerga yozing...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("open_input_b_q_${question.questionNumber}"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }
                    }

                    QuestionType.ESSAY -> {
                        var essayMode by remember { mutableIntStateOf(0) } // 0: Yozish, 1: Ball yozish, 2: PDF yuklash
                        var essayText by remember(question.questionNumber, answer.essayText) { mutableStateOf(answer.essayText ?: "") }
                        var manualScore by remember(question.questionNumber, answer.scoreAwarded) { mutableStateOf((answer.scoreAwarded ?: 0.0).toString()) }
                        var isPdfAttached by remember { mutableStateOf(false) }

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "45-savol: Yozma ish (Insho / Esse - 20 ball)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )

                            // 3-Mode Switcher Tabs for 45th Question
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                listOf(
                                    0 to "Matn yozish",
                                    1 to "Ball yozish (0-20)",
                                    2 to "PDF yuklash / olish"
                                ).forEach { (modeIdx, modeTitle) ->
                                    val isSel = essayMode == modeIdx
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable { essayMode = modeIdx },
                                        color = if (isSel) MaterialTheme.colorScheme.surface else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp),
                                        shadowElevation = if (isSel) 1.dp else 0.dp
                                    ) {
                                        Text(
                                            text = modeTitle,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 11.sp
                                            ),
                                            color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(vertical = 6.dp),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }

                            Text(
                                text = question.essayPrompt ?: "Insho mavzusi va talablari asosida yozma ish bajaring:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            when (essayMode) {
                                0 -> {
                                    // Mode 0: Direct Essay Writing
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        OutlinedTextField(
                                            value = essayText,
                                            onValueChange = {
                                                essayText = it
                                                onAnswerEssay(it)
                                            },
                                            placeholder = { Text("Insho matnini bu yerga yozing (Reja: 1. Kirish, 2. Asosiy qism, 3. Xulosa)...") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp)
                                                .testTag("essay_input_q_${question.questionNumber}"),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        val wordCount = essayText.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "So'zlar soni: $wordCount ta (Tavsiya: 150-200 so'z)",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (wordCount >= 150) GreenSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "Avtomatik saqlanadi",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                                color = Slate500
                                            )
                                        }
                                    }
                                }

                                1 -> {
                                    // Mode 1: Direct Score Entry (Ball yozib qo'yish)
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "45-savol uchun ballni to'g'ridan-to'g'ri kiritish (Maksimal: 20 ball)",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Qo'yilgan ball:",
                                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                                )
                                                OutlinedTextField(
                                                    value = manualScore,
                                                    onValueChange = {
                                                        manualScore = it
                                                        val sc = it.toDoubleOrNull() ?: 0.0
                                                        if (sc in 0.0..20.0) {
                                                            onAnswerEssay(if (essayText.isNotBlank()) essayText else "Ball: $sc/20")
                                                        }
                                                    },
                                                    placeholder = { Text("0-20") },
                                                    modifier = Modifier.width(90.dp),
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                            }

                                            Divider(color = MaterialTheme.colorScheme.outlineVariant)

                                            Text(
                                                text = "Baholash mezonlari:\n• Mazmun va mavzuning ochilishi (6 ball)\n• Grammatik qoidalar va imlo (5 ball)\n• Lug'at boyligi va iboralar (5 ball)\n• Matn yaxlitligi va xulosa (4 ball)",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = Slate600
                                            )
                                        }
                                    }
                                }

                                2 -> {
                                    // Mode 2: PDF Export / Upload
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(
                                                text = "Yozma ish varaqasi (PDF formati):",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            // Uploaded status indicator
                                            if (isPdfAttached) {
                                                Surface(
                                                    color = GreenContainer,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = null,
                                                            tint = GreenDark,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = "yozma_ish_45_savol.pdf biriktirildi",
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = GreenDark
                                                        )
                                                    }
                                                }
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                OutlinedButton(
                                                    onClick = {
                                                        isPdfAttached = true
                                                        onAnswerEssay("[PDF Fayl biriktirilgan: yozma_ish_45_savol.pdf]\n" + essayText)
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("PDF yuklash", fontSize = 11.sp)
                                                }

                                                Button(
                                                    onClick = {
                                                        // Download action is handled by top button or directly
                                                        isPdfAttached = true
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Varaqani olish", fontSize = 11.sp)
                                                }
                                            }

                                            Text(
                                                text = "Talabgor yozma ish varaqasini qog'ozda to'ldirib, rasm yoki PDF ko'rinishida yuklashi mumkin.",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                                color = Slate500
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation Buttons (Prev / Next / Submit)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPrev,
                    enabled = !isFirst,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("prev_question_button")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Oldingi")
                }

                if (isLast) {
                    Button(
                        onClick = onSubmitClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("submit_exam_bottom_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Testni yakunlash")
                    }
                } else {
                    Button(
                        onClick = onNext,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("next_question_button")
                    ) {
                        Text("Keyingi")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionGridNavigator(
    questions: List<QuestionEntity>,
    currentIndex: Int,
    answers: Map<Int, StudentAnswer>,
    onSelectIndex: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        Text(
            text = "Savollar ro'yxati (45 ta)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 9 Columns Grid on compact mobile, or 5 columns on tablet
        LazyVerticalGrid(
            columns = GridCells.Fixed(9),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth().height(160.dp)
        ) {
            items(questions.size) { idx ->
                val q = questions[idx]
                val ans = answers[q.questionNumber]
                val isCurrent = idx == currentIndex
                val isAnswered = ans != null && ans.isAnswered(q.type)
                val isBookmarked = ans?.isMarkedForReview == true

                val (bg, fg) = when {
                    isCurrent -> MaterialTheme.colorScheme.primary to Color.White
                    isBookmarked -> AmberContainer to AmberWarning
                    isAnswered -> GreenContainer to GreenDark
                    else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(bg)
                        .border(
                            width = if (isCurrent) 2.dp else 0.dp,
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onSelectIndex(idx) }
                        .testTag("grid_q_${idx + 1}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${idx + 1}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = fg
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = GreenContainer, label = "Javoblangan")
            LegendItem(color = AmberContainer, label = "Belgilangan")
            LegendItem(color = MaterialTheme.colorScheme.primary, label = "Hozirgi")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
