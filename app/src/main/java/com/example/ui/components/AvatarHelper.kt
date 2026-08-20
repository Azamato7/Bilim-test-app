package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RedError
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800

data class AvatarPreset(
    val id: String,
    val name: String,
    val bgGradient: List<Color>,
    val iconTint: Color,
    val roleType: String
)

object AvatarHelper {
    val presets = listOf(
        AvatarPreset("avatar_boy_1", "O'quvchi (Azizbek)", listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6)), Color.White, "student"),
        AvatarPreset("avatar_girl_1", "O'quvchi (Elmira)", listOf(Color(0xFF701A75), Color(0xFFD946EF)), Color.White, "student"),
        AvatarPreset("avatar_boy_2", "Talaba (Sardor)", listOf(Color(0xFF065F46), Color(0xFF10B981)), Color.White, "student"),
        AvatarPreset("avatar_girl_2", "Talaba (Farangiz)", listOf(Color(0xFF9F1239), Color(0xFFF43F5E)), Color.White, "student"),
        AvatarPreset("avatar_boy_3", "Talabgor (Oybek)", listOf(Color(0xFF0F766E), Color(0xFF06B6D4)), Color.White, "student"),
        AvatarPreset("avatar_teacher_1", "Ustoz (Alisher aka)", listOf(Color(0xFF1E293B), Color(0xFF475569)), Color.White, "teacher"),
        AvatarPreset("avatar_teacher_2", "Ustoz (Dilnoza opa)", listOf(Color(0xFF831843), Color(0xFFBE185D)), Color.White, "teacher")
    )

    fun getPresetById(id: String?): AvatarPreset {
        return presets.firstOrNull { it.id == id } ?: presets.first()
    }
}

@Composable
fun UserAvatarView(
    avatarId: String?,
    fullName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: androidx.compose.ui.graphics.Shape = CircleShape,
    is3x4PassportStyle: Boolean = false
) {
    val preset = AvatarHelper.getPresetById(avatarId)
    val initials = fullName.trim().split(" ")
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .take(2)
        .joinToString("")
        .ifBlank { "U" }

    if (is3x4PassportStyle) {
        // 3x4 Passport Portrait Box for Certificate
        Box(
            modifier = modifier
                .clip(shape)
                .background(Brush.verticalGradient(preset.bgGradient)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(size * 0.45f)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar Foto",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.35f)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "3x4 FOTO",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 7.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    } else {
        // Standard circle/rounded avatar
        Box(
            modifier = modifier
                .size(size)
                .clip(shape)
                .background(Brush.linearGradient(preset.bgGradient)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.38f).sp
            )
        }
    }
}

@Composable
fun AvatarPickerRow(
    selectedAvatarId: String?,
    onSelectAvatar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Profil rasmi / Avatar tanlang:",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(AvatarHelper.presets) { preset ->
                val isSelected = (selectedAvatarId ?: AvatarHelper.presets.first().id) == preset.id
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSelectAvatar(preset.id) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(preset.bgGradient))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (preset.roleType == "teacher") Icons.Default.School else Icons.Default.Person,
                            contentDescription = preset.name,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = preset.name.substringBefore(" ("),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
