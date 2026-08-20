package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay

data class SampleMediaAsset(
    val id: String,
    val title: String,
    val description: String,
    val type: String, // "image" or "audio"
    val durationSeconds: Int = 0,
    val gradientColors: List<Color> = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
)

object QuestionMediaPresets {
    val sampleImages = listOf(
        SampleMediaAsset("img_geom_1", "Geometriya: Uchburchak balandligi", "Katetlar va gipotenuza munosabati chizmasi", "image", gradientColors = listOf(Color(0xFF0F766E), Color(0xFF14B8A6))),
        SampleMediaAsset("img_anat_1", "Biologiya: Hujayra organoidlari", "Xloroplast va mitoxondriya tuzilishi", "image", gradientColors = listOf(Color(0xFF15803D), Color(0xFF22C55E))),
        SampleMediaAsset("img_chart_1", "Ona tili: Gap bo'laklari jadvali", "Bosh va ikkinchi darajali bo'laklar sxemasi", "image", gradientColors = listOf(Color(0xFF4338CA), Color(0xFF6366F1))),
        SampleMediaAsset("img_map_1", "Tarix: Samarqand Registon me'morchiligi", "Qadimiy xaritalar va me'moriy ansambl", "image", gradientColors = listOf(Color(0xFFB45309), Color(0xFFF59E0B))),
        SampleMediaAsset("img_phys_1", "Fizika: Nyuton qonunlari diagrammasi", "Kuch vektorlari va jism harakati", "image", gradientColors = listOf(Color(0xFFBE123C), Color(0xFFF43F5E)))
    )

    val sampleAudios = listOf(
        SampleMediaAsset("audio_eng_1", "Part 1: University Campus Dialogue", "Two students discussing lecture assignments", "audio", durationSeconds = 95, gradientColors = listOf(Color(0xFF1E40AF), Color(0xFF3B82F6))),
        SampleMediaAsset("audio_eng_2", "Part 2: Academic Lecture - Artificial Intelligence", "Professor lecturing about machine learning impact", "audio", durationSeconds = 140, gradientColors = listOf(Color(0xFF6D28D9), Color(0xFF8B5CF6))),
        SampleMediaAsset("audio_eng_3", "Part 3: Interview with a Young Entrepreneur", "Career pathways and modern tech startups", "audio", durationSeconds = 80, gradientColors = listOf(Color(0xFF0E7490), Color(0xFF06B6D4))),
        SampleMediaAsset("audio_eng_4", "Part 4: Flight & Station Announcement", "Short comprehension audio for speed listening", "audio", durationSeconds = 45, gradientColors = listOf(Color(0xFFBE185D), Color(0xFFEC4899)))
    )
}

/**
 * Question Image View (Rendered inside Student Exam and Teacher Preview)
 */
@Composable
fun QuestionImageView(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isNullOrBlank()) return

    val preset = QuestionMediaPresets.sampleImages.firstOrNull { it.id == imageUrl }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Savolga biriktirilgan rasm / chizma:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            preset?.gradientColors ?: listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = preset?.title ?: "Biriktirilgan diagramma / rasm",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = preset?.description ?: imageUrl,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

/**
 * Interactive Audio Listening Player for English / Foreign Language questions
 */
@Composable
fun QuestionAudioPlayer(
    audioUrl: String?,
    audioTitle: String? = null,
    modifier: Modifier = Modifier
) {
    if (audioUrl.isNullOrBlank()) return

    val preset = QuestionMediaPresets.sampleAudios.firstOrNull { it.id == audioUrl }
    val totalSeconds = preset?.durationSeconds ?: 90
    val displayTitle = audioTitle ?: preset?.title ?: "Listening Audio Track"

    var isPlaying by remember { mutableStateOf(false) }
    var currentSeconds by remember { mutableIntStateOf(0) }
    var playSpeed by remember { mutableStateOf("1.0x") }

    LaunchedEffect(isPlaying, playSpeed) {
        if (isPlaying) {
            val speedFactor = when (playSpeed) {
                "0.8x" -> 1250L
                "1.2x" -> 800L
                else -> 1000L
            }
            while (isPlaying && currentSeconds < totalSeconds) {
                delay(speedFactor)
                currentSeconds++
            }
            if (currentSeconds >= totalSeconds) {
                isPlaying = false
            }
        }
    }

    val progress = (currentSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    val curMins = currentSeconds / 60
    val curSecs = currentSeconds % 60
    val totMins = totalSeconds / 60
    val totSecs = totalSeconds % 60

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Audio",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Listening Comprehension (Audio)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = displayTitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Speed selector
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.clickable {
                        playSpeed = when (playSpeed) {
                            "1.0x" -> "1.2x"
                            "1.2x" -> "0.8x"
                            else -> "1.0x"
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = playSpeed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Waveform & Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Player Controls & Timers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Play/Pause button
                    IconButton(
                        onClick = {
                            if (currentSeconds >= totalSeconds) {
                                currentSeconds = 0
                            }
                            isPlaying = !isPlaying
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pauza" else "Ijro",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Replay button
                    IconButton(
                        onClick = {
                            currentSeconds = 0
                            isPlaying = true
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Boshidan eshitish",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = "%02d:%02d / %02d:%02d".format(curMins, curSecs, totMins, totSecs),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Media Attachment Picker for Question Editor in CreateTestScreen
 */
@Composable
fun QuestionMediaAttachmentEditor(
    selectedImageUrl: String?,
    onImageSelected: (String?) -> Unit,
    selectedAudioUrl: String?,
    onAudioSelected: (String?) -> Unit,
    isEnglishOrLanguage: Boolean,
    modifier: Modifier = Modifier
) {
    var showImagePresets by remember { mutableStateOf(false) }
    var showAudioPresets by remember { mutableStateOf(false) }
    var customImageUrl by remember { mutableStateOf("") }
    var customAudioUrl by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = "Media biriktirmalar (Rasm & Audio):",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Image Toggle Button
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showImagePresets = !showImagePresets },
                color = if (selectedImageUrl != null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedImageUrl != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = if (selectedImageUrl != null) MaterialTheme.colorScheme.primary else Slate500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedImageUrl != null) "Rasm: Biriktirilgan" else "📷 Rasm qo'shish",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (selectedImageUrl != null) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        ),
                        color = if (selectedImageUrl != null) MaterialTheme.colorScheme.primary else Slate700
                    )
                }
            }

            // Audio Toggle Button (Active for English or whenever requested)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showAudioPresets = !showAudioPresets },
                color = if (selectedAudioUrl != null) PurpleLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedAudioUrl != null) PurpleAccent else MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Audiotrack,
                        contentDescription = null,
                        tint = if (selectedAudioUrl != null) PurpleAccent else Slate500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedAudioUrl != null) "Audio: Biriktirilgan" else "🎧 Audio qo'shish",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (selectedAudioUrl != null) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        ),
                        color = if (selectedAudioUrl != null) PurpleAccent else Slate700
                    )
                }
            }
        }

        // Image Presets Box
        if (showImagePresets) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Namunaviy diagramma / chizma tanlang:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Slate700
                        )
                        if (selectedImageUrl != null) {
                            TextButton(onClick = { onImageSelected(null) }) {
                                Text("O'chirish", color = RedError, fontSize = 11.sp)
                            }
                        }
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    ) {
                        items(QuestionMediaPresets.sampleImages) { sample ->
                            val isChosen = selectedImageUrl == sample.id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChosen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) MaterialTheme.colorScheme.primary else Color.Transparent),
                                modifier = Modifier
                                    .clickable { onImageSelected(sample.id) }
                                    .padding(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp).width(140.dp)) {
                                    Text(
                                        text = sample.title,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = sample.description,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                        color = Slate500,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Audio Presets Box
        if (showAudioPresets) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Listening audio trek tanlang (Ingliz tili / Chet tillari):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PurpleAccent
                        )
                        if (selectedAudioUrl != null) {
                            TextButton(onClick = { onAudioSelected(null) }) {
                                Text("O'chirish", color = RedError, fontSize = 11.sp)
                            }
                        }
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    ) {
                        items(QuestionMediaPresets.sampleAudios) { audioSample ->
                            val isChosen = selectedAudioUrl == audioSample.id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChosen) PurpleLight.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) PurpleAccent else Color.Transparent),
                                modifier = Modifier
                                    .clickable { onAudioSelected(audioSample.id) }
                                    .padding(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp).width(160.dp)) {
                                    Text(
                                        text = audioSample.title,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${audioSample.durationSeconds} sek • ${audioSample.description}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                        color = Slate500,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
