package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.viewmodel.AppViewModel

@Composable
fun StoryPracticeScreen(
    viewModel: AppViewModel,
    story: Story,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSegmentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOptionId by remember { mutableStateOf("") }
    var selectedBlankWord by remember { mutableStateOf("") }
    var isSegmentAnswered by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var showStoryFinished by remember { mutableStateOf(false) }

    val segments = story.segments
    val currentSegment = segments.getOrNull(currentSegmentIndex)

    // Reset status flags when segment changes
    LaunchedEffect(currentSegmentIndex) {
        selectedOptionId = ""
        selectedBlankWord = ""
        isSegmentAnswered = false
        isAnswerCorrect = false
        feedbackMessage = ""
    }

    if (showStoryFinished || currentSegment == null) {
        // Complete current story and save scores
        LaunchedEffect(Unit) {
            viewModel.completeStoryRewards(story, score)
        }

        StoryFinishedCard(
            story = story,
            score = score,
            totalSegments = segments.size,
            onBack = onBack
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("exit_story_button")
                        .semantics { contentDescription = "Exit story module" }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Interactive Story / कहानी",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = story.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Progress Indicator
                Text(
                    text = "${currentSegmentIndex + 1}/${segments.size}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Progress bar
            LinearProgressIndicator(
                progress = (currentSegmentIndex + 1).toFloat() / segments.size.toFloat(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Speaker Badge & Context Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                            RoundedCornerShape(24.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (currentSegment.speaker == "Narrator")
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                        else
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (currentSegment.speaker) {
                                        "Narrator" -> "📖"
                                        "Rahul" -> "👦"
                                        "Aanya" -> "👧"
                                        "Shopkeeper" -> "🛒"
                                        "Teacher" -> "👩‍🏫"
                                        "Doctor" -> "🩺"
                                        else -> "👤"
                                    },
                                    fontSize = 18.sp
                                )
                            }
                            Text(
                                text = when (currentSegment.speaker) {
                                    "Narrator" -> "NARRATOR / कथावाचक"
                                    else -> currentSegment.speaker.uppercase()
                                },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentSegment.hindiContext,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = currentSegment.narration,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Interactive Segment Section
                if (currentSegment.isFillInBlank) {
                    // BEGINNER FILL IN THE BLANK
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "FILL IN THE BLANK / खाली स्थान भरें",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Highlighted Sentence with Blank representation
                            val displaySentence = if (selectedBlankWord.isEmpty()) {
                                currentSegment.sentenceWithBlank
                            } else {
                                currentSegment.sentenceWithBlank.replace("___", "[ $selectedBlankWord ]")
                            }

                            Text(
                                text = displaySentence,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Choice pills
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                mainAxisSpacing = 10.dp,
                                crossAxisSpacing = 10.dp
                            ) {
                                currentSegment.blankOptions.forEachIndexed { idx, option ->
                                    val isSelected = selectedBlankWord == option
                                    val buttonBg = when {
                                        isSelected && isSegmentAnswered && isAnswerCorrect -> Color(0xFFD4EDDA) // pastel green
                                        isSelected && isSegmentAnswered && !isAnswerCorrect -> Color(0xFFF8D7DA) // pastel red
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                    val textCol = when {
                                        isSelected && isSegmentAnswered && isAnswerCorrect -> Color(0xFF155724)
                                        isSelected && isSegmentAnswered && !isAnswerCorrect -> Color(0xFF721C24)
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(buttonBg)
                                            .clickable(enabled = !isSegmentAnswered) {
                                                selectedBlankWord = option
                                                // Validate immediately
                                                isSegmentAnswered = true
                                                val correct = option == currentSegment.blankCorrectAnswer
                                                isAnswerCorrect = correct
                                                if (correct) {
                                                    score++
                                                    feedbackMessage = "Correct! / सही जवाब! 🌟"
                                                } else {
                                                    feedbackMessage = "Oops! Try again / गलत जवाब। सही विकल्प ' ${currentSegment.blankCorrectAnswer} ' है।"
                                                }
                                            }
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                                RoundedCornerShape(16.dp)
                                            )
                                            .padding(horizontal = 20.dp, vertical = 12.dp)
                                            .testTag("story_option_$idx")
                                    ) {
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = textCol
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (currentSegment.isDialogueChoice) {
                    // ADVANCED MULTIPLE CHOICE DIALOGUE SCENARIO
                    Text(
                        text = "CHOOSE THE BEST RESPONSE / सर्वोत्तम प्रतिक्रिया चुनें",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    currentSegment.dialogueOptions.forEachIndexed { idx, option ->
                        val isSelected = selectedOptionId == option.id
                        val borderCol = when {
                            isSelected && isAnswerCorrect -> Color(0xFF2E7D32)
                            isSelected && !isAnswerCorrect -> Color(0xFFC62828)
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        }
                        val cardBg = when {
                            isSelected && isAnswerCorrect -> Color(0xFFE8F5E9) // soft pastel green
                            isSelected && !isAnswerCorrect -> Color(0xFFFFEBEE) // soft pastel pink/red
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.5.dp, borderCol, RoundedCornerShape(20.dp))
                                .clickable(enabled = !isSegmentAnswered) {
                                    selectedOptionId = option.id
                                    isSegmentAnswered = true
                                    isAnswerCorrect = option.isCorrect
                                    feedbackMessage = option.feedback
                                    if (option.isCorrect) {
                                        score++
                                    }
                                }
                                .testTag("story_option_$idx")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = option.text,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected && isAnswerCorrect) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Dynamic pedagogical Feedback Box
                if (isSegmentAnswered && feedbackMessage.isNotEmpty()) {
                    val boxBg = if (isAnswerCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    val borderCol = if (isAnswerCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
                    val textCol = if (isAnswerCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = boxBg),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, borderCol, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(if (isAnswerCorrect) "✅" else "❌", fontSize = 20.sp)
                            Text(
                                text = feedbackMessage,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = textCol,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Footer navigation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                if (isSegmentAnswered) {
                    Button(
                        onClick = {
                            if (currentSegmentIndex < segments.size - 1) {
                                currentSegmentIndex++
                            } else {
                                showStoryFinished = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("next_story_segment_button")
                    ) {
                        Text(
                            text = if (currentSegmentIndex < segments.size - 1) "NEXT SEGMENT / अगला भाग ➡️" else "FINISH STORY / कहानी पूरी करें 🎉",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    Button(
                        onClick = {},
                        enabled = false,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = "SELECT AN OPTION / एक विकल्प चुनें",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryFinishedCard(
    story: Story,
    score: Int,
    totalSegments: Int,
    onBack: () -> Unit
) {
    val xpMain = story.xpValue
    val accuracy = if (totalSegments > 0) (score.toFloat() / totalSegments.toFloat()) * 100f else 0.0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎉", fontSize = 44.sp)
                }

                Text(
                    text = "Story Completed!",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "कहानी सफलता पूर्वक समाप्त हुई!",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Score / अंक",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$score / $totalSegments",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "XP Earned",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "+$xpMain",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))

                Text(
                    text = "Accuracy: ${accuracy.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (accuracy >= 70) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("story_finish_back_button")
                ) {
                    Text(
                        text = "Back to Home / घर वापस जाएं 🏠",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// FlowRow wrapper helper to avoid dependency clashes with old gradle versions
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    crossAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val layoutWidth = constraints.maxWidth
        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0

        placeables.forEach { placeable ->
            if (currentRowWidth + placeable.width > layoutWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width + mainAxisSpacing.roundToPx()
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        val totalHeight = rows.sumOf { row -> row.maxOf { it.height } } + (rows.size - 1) * crossAxisSpacing.roundToPx()

        layout(layoutWidth, totalHeight) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                val rowHeight = row.maxOf { it.height }
                row.forEach { placeable ->
                    placeable.placeRelative(x, y + (rowHeight - placeable.height) / 2)
                    x += placeable.width + mainAxisSpacing.roundToPx()
                }
                y += rowHeight + crossAxisSpacing.roundToPx()
            }
        }
    }
}
