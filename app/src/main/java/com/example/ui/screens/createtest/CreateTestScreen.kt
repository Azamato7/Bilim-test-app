package com.example.ui.screens.createtest

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.TestSessionEntity
import com.example.data.model.ExamSubject
import com.example.data.model.QuestionType
import com.example.data.repository.SeedDataGenerator
import com.example.domain.ai.AiTestGeneratorService
import com.example.domain.pdf.PdfGeneratorService
import com.example.ui.components.QuestionAudioPlayer
import com.example.ui.components.QuestionImageView
import com.example.ui.components.QuestionMediaAttachmentEditor
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueLight
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import kotlinx.coroutines.launch

fun createBlank45Questions(testId: String, subject: ExamSubject): List<QuestionEntity> {
    val list = mutableListOf<QuestionEntity>()
    for (i in 1..45) {
        val qType = when {
            (subject == ExamSubject.ONA_TILI || subject == ExamSubject.ENGLISH) && i == 45 -> QuestionType.ESSAY
            i in 1..35 -> QuestionType.CLOSED_ABCD
            else -> QuestionType.OPEN_TWO_PARTS
        }

        list.add(
            QuestionEntity(
                id = "${testId}_q$i",
                testId = testId,
                questionNumber = i,
                type = qType,
                questionText = "",
                imageUrl = null,
                audioUrl = null,
                audioTitle = null,
                optionA = if (qType == QuestionType.CLOSED_ABCD) "" else null,
                optionB = if (qType == QuestionType.CLOSED_ABCD) "" else null,
                optionC = if (qType == QuestionType.CLOSED_ABCD) "" else null,
                optionD = if (qType == QuestionType.CLOSED_ABCD) "" else null,
                correctOption = if (qType == QuestionType.CLOSED_ABCD) "A" else null,
                openPartAPrompt = if (qType == QuestionType.OPEN_TWO_PARTS) "" else null,
                openPartBPrompt = if (qType == QuestionType.OPEN_TWO_PARTS) "" else null,
                correctAnswerA = if (qType == QuestionType.OPEN_TWO_PARTS) "" else null,
                correctAnswerB = if (qType == QuestionType.OPEN_TWO_PARTS) "" else null,
                essayPrompt = if (qType == QuestionType.ESSAY) "" else null,
                sectionName = null,
                maxScore = if (qType == QuestionType.ESSAY) 15.0 else if (qType == QuestionType.OPEN_TWO_PARTS) 2.0 else 1.5
            )
        )
    }
    return list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTestScreen(
    onBackClick: () -> Unit,
    onSaveTest: (subject: ExamSubject, title: String, description: String, timeLimit: Int, questions: List<QuestionEntity>, onDone: (TestSessionEntity) -> Unit) -> Unit,
    onPreviewPdf: (TestSessionEntity) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedSubject by remember { mutableStateOf(ExamSubject.ONA_TILI) }
    var testTitle by remember { mutableStateOf("Yangi Milliy Sertifikat Testi") }
    var testDesc by remember { mutableStateOf("Milliy baholash tizimi talablari asosidagi 45 talik yangi test") }
    var timeLimitMinutes by remember { mutableIntStateOf(90) }
    var subjectDropdownExpanded by remember { mutableStateOf(false) }

    // User requirement: Test yaratishda savollar dastlab bo'sh (blank) turishi kerak!
    var questionsList by remember {
        mutableStateOf(createBlank45Questions("temp_test", ExamSubject.ONA_TILI))
    }
    var activeQuestionIndex by remember { mutableIntStateOf(0) }

    var createdTestDialog by remember { mutableStateOf<TestSessionEntity?>(null) }
    var showAiGenerateDialog by remember { mutableStateOf(false) }
    var aiTopicInput by remember { mutableStateOf("") }
    var isGeneratingAi by remember { mutableStateOf(false) }

    // When subject changes, reload blank questions for that subject
    fun updateSubject(newSubj: ExamSubject) {
        selectedSubject = newSubj
        testTitle = "${newSubj.titleUz} - Yangi Test #${(10..40).random()}"
        questionsList = createBlank45Questions("temp_test", newSubj)
        activeQuestionIndex = 0
    }

    fun openTelegramChannel() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Bilimtest_n1"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Telegram kanal: https://t.me/Bilimtest_n1", Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 900.dp)
                .padding(16.dp)
                .testTag("create_test_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Orqaga")
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Yangi test yaratish",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "45 ta savolli milliy sertifikat testi",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onSaveTest(
                                selectedSubject,
                                testTitle,
                                testDesc,
                                timeLimitMinutes,
                                questionsList
                            ) { created ->
                                createdTestDialog = created
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_save_test")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Testni saqlash", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Telegram & AI Smart Banner
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Gemini AI & Savollar Kitobchasi (PDF)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Barcha 45 savolni AI orqali rasmlari bilan to'ldirish yoki 1 ta PDF kitobcha yaratish",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { showAiGenerateDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_open_ai_generator")
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("AI bilan to'ldirish", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // General Test Settings Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1. Test parametrlari",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                // Clear all button
                                OutlinedButton(
                                    onClick = {
                                        questionsList = createBlank45Questions("temp_test", selectedSubject)
                                        Toast.makeText(context, "Savollar tozalandi", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ClearAll, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tozalash", fontSize = 10.sp)
                                }

                                // Load samples button
                                OutlinedButton(
                                    onClick = {
                                        questionsList = SeedDataGenerator.generate45QuestionsForTest("temp_test", selectedSubject)
                                        Toast.makeText(context, "Namunaviy savollar yuklandi", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Description, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Namuna", fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Row 1: Subject + Title
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = subjectDropdownExpanded,
                                onExpandedChange = { subjectDropdownExpanded = !subjectDropdownExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedSubject.titleUz,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Umumta'lim fani") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectDropdownExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )
                                ExposedDropdownMenu(
                                    expanded = subjectDropdownExpanded,
                                    onDismissRequest = { subjectDropdownExpanded = false }
                                ) {
                                    ExamSubject.values().forEach { subj ->
                                        DropdownMenuItem(
                                            text = { Text(subj.titleUz) },
                                            onClick = {
                                                updateSubject(subj)
                                                subjectDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = testTitle,
                                onValueChange = { testTitle = it },
                                label = { Text("Test nomi") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Row 2: 45 Questions (Locked) + Time Limit
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = "45 ta savol (Standart)",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Savollar soni") },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Qat'iy 45 ta", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = "$timeLimitMinutes daqiqa",
                                onValueChange = {
                                    val mins = it.filter { c -> c.isDigit() }.toIntOrNull() ?: 90
                                    timeLimitMinutes = mins
                                },
                                label = { Text("Vaqt chegarasi") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // PDF Booklet Integration Action Row
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Yagona Savollar Kitobchasi (PDF)",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Barcha 45 ta savol uchun yagona rasmiy PDF fayl avtomatik shakllanadi",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        val tempTest = TestSessionEntity(
                                            id = "preview_booklet",
                                            accessCode = "PDF-PREVIEW",
                                            creatorId = "creator",
                                            creatorName = "O'qituvchi",
                                            subject = selectedSubject,
                                            title = testTitle,
                                            description = testDesc,
                                            totalQuestions = 45,
                                            timeLimitMinutes = timeLimitMinutes
                                        )
                                        onPreviewPdf(tempTest)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PDF kitobcha ko'rish", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Telegram Support Link Row
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = BlueLight.copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { openTelegramChannel() }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = BlueDark)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Aloqa va Savollar bazasi uchun Telegram guruhimiz:",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = BlueDark
                                        )
                                        Text(
                                            text = "https://t.me/Bilimtest_n1 (@Bilimtest_n1)",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = BluePrimary
                                        )
                                    }
                                }

                                Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // 45-Question Navigation Strip & Question Editor
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. Savollar muharriri (45 ta)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${activeQuestionIndex + 1} / 45",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 1..45 Quick Navigator Strip
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(questionsList.size) { index ->
                                val q = questionsList[index]
                                val isSelected = index == activeQuestionIndex
                                val isFilled = q.questionText.isNotBlank()

                                val bg = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    q.type == QuestionType.ESSAY -> PurpleLight
                                    q.type == QuestionType.OPEN_TWO_PARTS -> GreenContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }

                                val fg = when {
                                    isSelected -> Color.White
                                    q.type == QuestionType.ESSAY -> PurpleAccent
                                    q.type == QuestionType.OPEN_TWO_PARTS -> GreenDark
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(bg)
                                        .clickable { activeQuestionIndex = index }
                                        .testTag("question_pill_${index + 1}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = fg
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current Question Form Editor
                        if (questionsList.isNotEmpty() && activeQuestionIndex in questionsList.indices) {
                            val currentQ = questionsList[activeQuestionIndex]

                            QuestionEditForm(
                                question = currentQ,
                                selectedSubject = selectedSubject,
                                onUpdate = { updatedQ ->
                                    val list = questionsList.toMutableList()
                                    list[activeQuestionIndex] = updatedQ
                                    questionsList = list
                                },
                                onGenerateSingleAi = {
                                    coroutineScope.launch {
                                        val singleAi = AiTestGeneratorService.generateSingleQuestionWithAi(
                                            testId = "temp_test",
                                            subject = selectedSubject,
                                            questionNumber = currentQ.questionNumber,
                                            type = currentQ.type
                                        )
                                        val list = questionsList.toMutableList()
                                        list[activeQuestionIndex] = singleAi
                                        questionsList = list
                                        Toast.makeText(context, "${currentQ.questionNumber}-savol AI orqali to'ldirildi!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // AI Full 45-Questions Generator Dialog
        if (showAiGenerateDialog) {
            AlertDialog(
                onDismissRequest = { if (!isGeneratingAi) showAiGenerateDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gemini AI orqali 45 ta savolni to'ldirish", fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Sun'iy intellekt tanlangan ${selectedSubject.titleUz} fani bo'yicha milliy standartdagi barcha 45 ta savolni to'liq yaratadi va zarur bo'lgan savollarga chizmalar/rasmlarni biriktiradi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = aiTopicInput,
                            onValueChange = { aiTopicInput = it },
                            label = { Text("Mavzu yoki yo'nalish (ixtiyoriy)") },
                            placeholder = { Text("Masalan: 11-sinf barcha boblar, Morfologiya, Integrallar...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_topic_input"),
                            singleLine = false,
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Suggested Quick Topics
                        Text(
                            text = "Tavsiya etilgan standart mavzular:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val quickTopics = when (selectedSubject) {
                            ExamSubject.ONA_TILI -> listOf("To'liq 11-sinf kursi & Insho", "Fonetika, leksika va sintaksis", "Badiiy matn tahlili")
                            ExamSubject.ENGLISH -> listOf("B2 CEFR Level Complete (Grammar & Reading)", "Vocabulary & Reading Comprehension", "Essay & Listening practice")
                            ExamSubject.MATEMATIKA -> listOf("10-11-sinf algebra va stereometriya", "Funksiyalar va integrallar", "Parametrli tenglamalar")
                            ExamSubject.FIZIKA -> listOf("Mexanika, termodinamika va optika", "Elektr zanjirlari va magnit maydon", "Kvant fizikasi")
                            ExamSubject.KIMYO -> listOf("Organik va noorganik kimyo", "Eritmalar va elektrokimyo", "Reaksiyalar tezligi")
                            ExamSubject.BIOLOGIYA -> listOf("Sitologiya, genetika va evolutsiya", "Odam anatomiyasi va fiziologiya", "Ekologiya va botanika")
                            ExamSubject.TARIX -> listOf("O'zbekiston tarixi (Eng qadimgi davrdan hozirgacha)", "Jahon tarixi & Amir Temur davri", "XX asr yangi tarixi")
                            ExamSubject.GEOGRAFIYA -> listOf("Jahon iqtisodiy geografiyasi", "O'zbekiston tabiiy geografiyasi", "Topografiya va xaritalar")
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            quickTopics.forEach { topic ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable { aiTopicInput = topic },
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = "• $topic",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        if (isGeneratingAi) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("AI 45 ta savol va chizmalarni yaratmoqda...", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isGeneratingAi = true
                            coroutineScope.launch {
                                try {
                                    val aiGenerated = AiTestGeneratorService.generate45QuestionsWithAi(
                                        testId = "temp_test",
                                        subject = selectedSubject,
                                        topicPrompt = aiTopicInput
                                    )
                                    if (aiGenerated.isNotEmpty()) {
                                        questionsList = aiGenerated
                                        testTitle = "${selectedSubject.titleUz} - ${aiTopicInput.ifBlank { "AI Milliy Sertifikat Testi" }}"
                                        Toast.makeText(context, "45 ta savol AI orqali muvaffaqiyatli to'ldirildi!", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "AI generatsiyasida xatolik: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isGeneratingAi = false
                                    showAiGenerateDialog = false
                                }
                            }
                        },
                        enabled = !isGeneratingAi,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("btn_confirm_ai_generate")
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("45 ta savolni to'ldirish")
                    }
                },
                dismissButton = {
                    if (!isGeneratingAi) {
                        TextButton(onClick = { showAiGenerateDialog = false }) {
                            Text("Bekor qilish")
                        }
                    }
                }
            )
        }

        // Test Created Success Dialog
        createdTestDialog?.let { test ->
            AlertDialog(
                onDismissRequest = { createdTestDialog = null },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Test muvaffaqiyatli yaratildi!", fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column {
                        Text(
                            "O'quvchilar ushbu testga quyidagi maxsus kod orqali ulanishlari mumkin:",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = test.accessCode,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "45 ta savoldan iborat test barcha o'quvchilar topshirgandan so'ng Rasch modeli orqali baholanadi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openTelegramChannel() },
                            color = BlueLight.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = BlueDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Telegram: @Bilimtest_n1", fontSize = 11.sp, color = BlueDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            createdTestDialog = null
                            onBackClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Bosh sahifaga qaytish")
                    }
                }
            )
        }
    }
}

@Composable
private fun QuestionEditForm(
    question: QuestionEntity,
    selectedSubject: ExamSubject,
    onUpdate: (QuestionEntity) -> Unit,
    onGenerateSingleAi: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Question Header & Type badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${question.questionNumber}-savol tahrirlash",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // AI Single Question Generate Button
                OutlinedButton(
                    onClick = onGenerateSingleAi,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("AI to'ldirish", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                }

                val typeBadgeText = when (question.type) {
                    QuestionType.CLOSED_ABCD -> "Yopiq (ABCD)"
                    QuestionType.OPEN_TWO_PARTS -> "Ochiq (a va b)"
                    QuestionType.ESSAY -> "Insho (Esse)"
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = typeBadgeText,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Media attachment editor (Image for all subjects, Audio for English & all subjects)
        QuestionMediaAttachmentEditor(
            selectedImageUrl = question.imageUrl,
            onImageSelected = { img -> onUpdate(question.copy(imageUrl = img)) },
            selectedAudioUrl = question.audioUrl,
            onAudioSelected = { aud ->
                val title = if (aud != null) "Part listening exercise audio" else null
                onUpdate(question.copy(audioUrl = aud, audioTitle = title))
            },
            isEnglishOrLanguage = selectedSubject == ExamSubject.ENGLISH,
            modifier = Modifier.fillMaxWidth()
        )

        // Previews if attached
        if (!question.imageUrl.isNullOrBlank()) {
            QuestionImageView(imageUrl = question.imageUrl)
        }
        if (!question.audioUrl.isNullOrBlank()) {
            QuestionAudioPlayer(audioUrl = question.audioUrl, audioTitle = question.audioTitle)
        }

        // Question Text Input
        OutlinedTextField(
            value = question.questionText,
            onValueChange = { onUpdate(question.copy(questionText = it)) },
            label = { Text("Savol matni") },
            placeholder = { Text("Ushbu ${question.questionNumber}-savol matnini bu yerga yozing...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("question_text_input_${question.questionNumber}"),
            minLines = 2,
            shape = RoundedCornerShape(10.dp)
        )

        when (question.type) {
            QuestionType.CLOSED_ABCD -> {
                Text(
                    text = "Variantlar va to'g'ri javobni tanlang:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OptionInputRow(
                    optionLetter = "A",
                    value = question.optionA ?: "",
                    isCorrect = question.correctOption.equals("A", ignoreCase = true),
                    onValueChange = { onUpdate(question.copy(optionA = it)) },
                    onSelectCorrect = { onUpdate(question.copy(correctOption = "A")) }
                )
                OptionInputRow(
                    optionLetter = "B",
                    value = question.optionB ?: "",
                    isCorrect = question.correctOption.equals("B", ignoreCase = true),
                    onValueChange = { onUpdate(question.copy(optionB = it)) },
                    onSelectCorrect = { onUpdate(question.copy(correctOption = "B")) }
                )
                OptionInputRow(
                    optionLetter = "C",
                    value = question.optionC ?: "",
                    isCorrect = question.correctOption.equals("C", ignoreCase = true),
                    onValueChange = { onUpdate(question.copy(optionC = it)) },
                    onSelectCorrect = { onUpdate(question.copy(correctOption = "C")) }
                )
                OptionInputRow(
                    optionLetter = "D",
                    value = question.optionD ?: "",
                    isCorrect = question.correctOption.equals("D", ignoreCase = true),
                    onValueChange = { onUpdate(question.copy(optionD = it)) },
                    onSelectCorrect = { onUpdate(question.copy(correctOption = "D")) }
                )
            }

            QuestionType.OPEN_TWO_PARTS -> {
                Text(
                    text = "Ochiq savol tuzilmasi (O'quvchi o'z javobini yozadi):",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = question.openPartAPrompt ?: "",
                    onValueChange = { onUpdate(question.copy(openPartAPrompt = it)) },
                    label = { Text("a) 1-qism topshirig'i") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = question.correctAnswerA ?: "",
                    onValueChange = { onUpdate(question.copy(correctAnswerA = it)) },
                    label = { Text("a) Namunaviy to'g'ri kalit javob") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = question.openPartBPrompt ?: "",
                    onValueChange = { onUpdate(question.copy(openPartBPrompt = it)) },
                    label = { Text("b) 2-qism topshirig'i") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = question.correctAnswerB ?: "",
                    onValueChange = { onUpdate(question.copy(correctAnswerB = it)) },
                    label = { Text("b) Namunaviy to'g'ri kalit javob") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            QuestionType.ESSAY -> {
                Text(
                    text = "Insho (Esse) mavzusi va mezonlari:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = question.essayPrompt ?: "",
                    onValueChange = { onUpdate(question.copy(essayPrompt = it)) },
                    label = { Text("Insho mavzusi va talablari") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}

@Composable
private fun OptionInputRow(
    optionLetter: String,
    value: String,
    isCorrect: Boolean,
    onValueChange: (String) -> Unit,
    onSelectCorrect: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onSelectCorrect,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = "To'g'ri javob",
                tint = if (isCorrect) GreenSuccess else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("$optionLetter varianti matni") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isCorrect) GreenSuccess else MaterialTheme.colorScheme.primary
            )
        )
    }
}

