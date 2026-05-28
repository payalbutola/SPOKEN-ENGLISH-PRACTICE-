package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.AccessibilityControls
import com.example.viewmodel.AppViewModel

@Composable
fun StudentHomeScreen(
    viewModel: AppViewModel,
    progress: UserProgress,
    onStartLesson: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    val homeworkList by viewModel.customHomework.collectAsState()
    val backupState by viewModel.backupStatus.collectAsState()
    val lastBackupTime by viewModel.lastBackupTime.collectAsState()

    var showAccessibilityCard by remember { mutableStateOf(false) }

    val filteredLessons = LessonsData.lessonsList.filter {
        it.level == progress.currentLevel
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Welcoming Row (Bento Top App Bar style)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(100.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Rahul profile avatar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Namaste, ${progress.name}!",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.semantics { contentDescription = "Friendly greeting to Rahul" }
                        )
                        Text(
                            text = "English Saathi",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // Workspace role switcher (Student vs Teacher)
                Button(
                    onClick = { viewModel.setRole("teacher") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .testTag("teacher_dashboard_mode_button")
                        .semantics { contentDescription = "Switch to Teacher dashboard space" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Teacher view")
                }
            }
        }

        // Bento Card 1: Primary Progress Card / Current Goal (Wide/Large)
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (progress.highContrastMode) MaterialTheme.colorScheme.primary else Color.Transparent,
                        RoundedCornerShape(24.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Abstract decorative bubble in the background
                    Box(
                        modifier = Modifier
                            .offset(x = 30.dp, y = (-30).dp)
                            .size(100.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .align(Alignment.TopEnd)
                    )
                    
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CURRENT GOAL / वर्तमान लक्ष्य",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (progress.currentLevel == "Beginner") "Daily Greetings" else "Advanced Dialogue",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = if (progress.currentLevel == "Beginner") "नमस्ते को Hello कहना सीखें" else "अंग्रेजी में बातचीत करना सीखें",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val progressFraction = if (progress.xp >= 100) 1.0f else (progress.xp % 100) / 100f
                        val progressPercent = (progressFraction * 100).toInt()

                        LinearProgressIndicator(
                            progress = progressFraction,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$progressPercent% Complete",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }

        // Streak Card & Status Overview
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Streak System (Gamified Engagement)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            2.dp,
                            if (progress.highContrastMode) MaterialTheme.colorScheme.primary else Color.Transparent,
                            RoundedCornerShape(24.dp)
                        )
                        .testTag("streak_count_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "दैनिक सिलसिला / Streak",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${progress.streak} Days",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🔥",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Text(
                            text = "Next bonus in 1 day!",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                // Cumulative XP System
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            2.dp,
                            if (progress.highContrastMode) MaterialTheme.colorScheme.primary else Color.Transparent,
                            RoundedCornerShape(24.dp)
                        )
                        .testTag("xp_score_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "कुल अंक / Total XP",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${progress.xp} XP",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Star icon",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Level: ${progress.currentLevel}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Accessibility Slider toggle card
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAccessibilityCard = !showAccessibilityCard }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "अभिगम्यता नियंत्रण / Accessibility Controls",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Adjust text sizes & High-contrast styling here",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        if (showAccessibilityCard) "COLLAPSE" else "EXPAND",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Expanded Accessibility settings
        if (showAccessibilityCard) {
            item {
                AccessibilityControls(
                    progress = progress,
                    onContrastToggle = { viewModel.setHighContrastEnabled(it) },
                    onMultiplierChange = { viewModel.setTextSizeMultiplier(it) }
                )
            }
        }

        // Level Selector Buttons (Beginner vs Advanced) - Bento layout active pills
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(6.dp)
            ) {
                val levels = listOf("Beginner" to "शुरुआती / Beginner", "Advanced" to "उन्नत / Advanced")
                levels.forEach { (key, label) ->
                    val isSelected = progress.currentLevel == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .clickable { viewModel.setLevel(key) }
                            .testTag("level_tab_$key"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        // Cloud backup synchronized visual indicator
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (backupState == "SUCCESS") Color(0xFF2E7D32) else MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (backupState == "SUCCESS") "Database secured & synced (Cloud backup ok!)" else "Database offline (Local Storage, sync needed)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.weight(1f)
                    )
                    if (backupState != "SUCCESS") {
                        Text(
                            text = "Sync now",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { viewModel.triggerCloudBackup() }
                                .testTag("home_sync_anchor")
                        )
                    }
                }
            }
        }

        // School Custom Homework Row (Assigned by teacher) - Bento style peach/maroon card
        val activeHW = homeworkList.filter { it.level == progress.currentLevel }
        if (activeHW.isNotEmpty()) {
            item {
                Text(
                    text = "स्कूल से गृहकार्य / School Homework Tasks",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(activeHW) { hw ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            // Turn custom homework into a mini practice lesson
                            val miniLesson = Lesson(
                                id = "custom_${hw.id}",
                                title = "School Homework",
                                hindiTitle = "स्कूल का काम",
                                description = "Pronounce custom homework assigned by your teacher.",
                                hindiDescription = "अपने शिक्षक द्वारा दिए गए गृहकार्य का अभ्यास करें।",
                                level = progress.currentLevel,
                                questions = listOf(
                                    QuizQuestion(
                                        id = "hw_${hw.id}",
                                        type = QuestionType.WHISPER_SPEAK,
                                        hindiText = "Translate or Speak:\n${hw.hindiTranslation}",
                                        englishText = hw.englishPhrase,
                                        hint = hw.hint,
                                        correctAnswer = hw.englishPhrase
                                    )
                                ),
                                xpValue = 15
                            )
                            onStartLesson(miniLesson)
                        }
                        .testTag("homework_task_${hw.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "HOMEWORK ASSIGNMENT / गृहकार्य",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("📝", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hw.hindiTranslation,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Tap to practice vocabulary & pronunciation feedback!",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Playful course lessons header
        item {
            Text(
                text = "अभ्यास पाठ चुनें / Choose Practice Lesson",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Lesson Cards List - Clean white/slate grids with large rounded corners
        items(filteredLessons) { lesson ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onStartLesson(lesson) }
                    .testTag("lesson_card_${lesson.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lesson.hindiTitle,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lesson.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Text(
                            text = lesson.hindiDescription,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // XP value badge styled exactly like the bento badge
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${lesson.xpValue} XP",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
