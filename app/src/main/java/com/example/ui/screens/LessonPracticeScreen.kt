package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.viewmodel.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun LessonPracticeScreen(
    viewModel: AppViewModel,
    lesson: Lesson,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by viewModel.userProgress.collectAsState()
    val speechText by viewModel.speechText.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val pronScore by viewModel.pronunciationScore.collectAsState()

    val context = LocalContext.current

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var currentScore by remember { mutableStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf("") }
    val selectedReorderWords = remember { mutableStateListOf<String>() }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var showLessonFinishedFinished by remember { mutableStateOf(false) }

    val currentQuestion = lesson.questions.getOrNull(currentQuestionIndex)

    // Reorder state setup
    LaunchedEffect(currentQuestionIndex) {
        selectedReorderWords.clear()
        answered = false
        selectedAnswer = ""
    }

    if (showLessonFinishedFinished) {
        LessonFinishedSummaryCard(
            lesson = lesson,
            score = currentScore,
            onBack = onBack,
            viewModel = viewModel
        )
    } else if (currentQuestion == null) {
        // Fallback or finish
        LaunchedEffect(Unit) {
            showLessonFinishedFinished = true
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Header: Exit, Lesson Title, Spacing Tracker
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
                        .testTag("exit_practice_button")
                        .semantics { contentDescription = "Exit lesson practice" }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Question ${currentQuestionIndex + 1} of ${lesson.questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Streak placeholder
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "XP points",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentScore * 10} XP",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Standard Linear Progress Bar
            LinearProgressIndicator(
                progress = { (currentQuestionIndex.toFloat()) / lesson.questions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main Hindi Prompts / Questions display board
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        RoundedCornerShape(16.dp)
                    )
                    .weight(1.5f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Question instruction in Hindi
                    Text(
                        text = when (currentQuestion.type) {
                            QuestionType.MCQ -> "सही उत्तर चुनिए / Choose the correct answer"
                            QuestionType.WHISPER_SPEAK -> "माइक दबाकर उच्चारण अभ्यास करें / Pronounce the text"
                            QuestionType.REORDER -> "शब्दों को सही क्रम में व्यवस्थित करें / Rearrange words"
                            QuestionType.FILL_BLANKS -> "रिक्त स्थान भरें / Fill in the blank"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hindi prompt core
                    Text(
                        text = currentQuestion.hindiText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                        modifier = Modifier
                            .semantics { contentDescription = "Question translation goal: ${currentQuestion.hindiText}" }
                            .testTag("hindi_question_prompt")
                    )

                    if (currentQuestion.hint.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "संकेत / Guide: ${currentQuestion.hint}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive response panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2.5f)
            ) {
                when (currentQuestion.type) {
                    QuestionType.MCQ -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            currentQuestion.options.forEach { option ->
                                MCQOptionItem(
                                    text = option,
                                    isSelected = selectedAnswer == option,
                                    isAnswered = answered,
                                    isCorrectOption = option == currentQuestion.correctAnswer,
                                    onClick = {
                                        if (!answered) {
                                            selectedAnswer = option
                                            answered = true
                                            isAnswerCorrect = option == currentQuestion.correctAnswer
                                            if (isAnswerCorrect) currentScore++
                                        }
                                    }
                                )
                            }
                        }
                    }

                    QuestionType.WHISPER_SPEAK -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Target phrase (अंग्रेजी):",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = currentQuestion.englishText,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (currentQuestion.pronunciationClue.isNotBlank()) {
                                        Text(
                                            text = "उच्चारण मार्गदर्शक: ${currentQuestion.pronunciationClue}",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = speechText.ifBlank { "मदद के लिए माइक शुरू करें... / Press mic to speak" },
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isListening) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Microphone buttons
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        if (isListening) {
                                            viewModel.stopSpeechRecognizer()
                                        } else {
                                            viewModel.startSpeechRecognizer(context, currentQuestion.englishText)
                                        }
                                    },
                                    containerColor = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .testTag("microphone_button")
                                        .semantics { contentDescription = if (isListening) "Stop recording" else "Speak into microphone to reply" }
                                ) {
                                    Icon(
                                        imageVector = if (isListening) Icons.Default.Close else Icons.Default.Star, // Microphone icons
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp),
                                        tint = Color.White
                                    )
                                }
                            }

                            // Dynamic pronunciation correctness grader
                            pronScore?.let { score ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Accuracy Score: $score%",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (score >= 70) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = if (score >= 70) "Excellent! उच्चारण बहुत बढ़िया!" else "Keep trying, refer to pronunciation clue!",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (score >= 70) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                )

                                Button(
                                    onClick = {
                                        selectedAnswer = speechText
                                        answered = true
                                        isAnswerCorrect = score >= 70
                                        if (isAnswerCorrect) currentScore++
                                    },
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .testTag("submit_pronunciation_button")
                                ) {
                                    Text("Submit Score / जवाब दर्ज करें")
                                }
                            }
                        }
                    }

                    QuestionType.REORDER -> {
                        Column {
                            // Selected phrase area
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                if (selectedReorderWords.isEmpty()) {
                                    Text(
                                        text = "Your response will appear here...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        selectedReorderWords.forEach { word ->
                                            Text(
                                                text = word,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier
                                                    .background(
                                                        MaterialTheme.colorScheme.surface,
                                                        RoundedCornerShape(4.dp)
                                                    )
                                                    .border(
                                                        1.dp,
                                                        MaterialTheme.colorScheme.outline,
                                                        RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Words chips source
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PlayfulFlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    currentQuestion.options.forEach { word ->
                                        val isUsed = selectedReorderWords.contains(word)
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isUsed) MaterialTheme.colorScheme.surfaceVariant.copy(
                                                        alpha = 0.5f
                                                    ) else MaterialTheme.colorScheme.surface,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isUsed) Color.Transparent else MaterialTheme.colorScheme.outline,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable(enabled = !isUsed && !answered) {
                                                    selectedReorderWords.add(word)
                                                }
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                                .testTag("reorder_word_$word")
                                        ) {
                                            Text(
                                                text = word,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = if (isUsed) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.5f
                                                ) else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Buttons Row (Reset and Complete)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { selectedReorderWords.clear() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    enabled = !answered,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("reorder_reset_button")
                                ) {
                                    Text("Reset")
                                }

                                Button(
                                    onClick = {
                                        val composite = selectedReorderWords.joinToString(" ")
                                        selectedAnswer = composite
                                        answered = true
                                        isAnswerCorrect = composite.equals(
                                            currentQuestion.correctAnswer,
                                            ignoreCase = true
                                        )
                                        if (isAnswerCorrect) currentScore++
                                    },
                                    enabled = selectedReorderWords.isNotEmpty() && !answered,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("reorder_complete_button")
                                ) {
                                    Text("Check Phrase")
                                }
                            }
                        }
                    }

                    QuestionType.FILL_BLANKS -> {
                        Column {
                            // Question sentence layout holding the blanks
                            Text(
                                text = currentQuestion.englishText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Choice options for filling missing piece
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                currentQuestion.options.forEach { word ->
                                    MCQOptionItem(
                                        text = word,
                                        isSelected = selectedAnswer == word,
                                        isAnswered = answered,
                                        isCorrectOption = word == currentQuestion.correctAnswer,
                                        onClick = {
                                            if (!answered) {
                                                selectedAnswer = word
                                                answered = true
                                                isAnswerCorrect = word == currentQuestion.correctAnswer
                                                if (isAnswerCorrect) currentScore++
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom popup results sheet overlay (using elegant animated layouts)
            AnimatedVisibility(
                visible = answered,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAnswerCorrect) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isAnswerCorrect) "Correct! सही जवाब! 🎉" else "Incorrect! गलत जवाब! 😢",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isAnswerCorrect) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Correct Phrase: ${currentQuestion.correctAnswer}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isAnswerCorrect) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (currentQuestionIndex < lesson.questions.size - 1) {
                                    currentQuestionIndex++
                                } else {
                                    showLessonFinishedFinished = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAnswerCorrect) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("continue_question_button")
                        ) {
                            Text(
                                if (currentQuestionIndex < lesson.questions.size - 1) "Continue / आगे बढ़ें" else "Finish Lesson!"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MCQOptionItem(
    text: String,
    isSelected: Boolean,
    isAnswered: Boolean,
    isCorrectOption: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when {
        isAnswered && isCorrectOption -> MaterialTheme.colorScheme.secondaryContainer
        isAnswered && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.errorContainer
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val outlineColor = when {
        isAnswered && isCorrectOption -> MaterialTheme.colorScheme.secondary
        isAnswered && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.error
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(2.dp, outlineColor, RoundedCornerShape(12.dp))
            .clickable(enabled = !isAnswered, onClick = onClick)
            .testTag("option_$text")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = when {
                    isAnswered && isCorrectOption -> MaterialTheme.colorScheme.secondary
                    isAnswered && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun LessonFinishedSummaryCard(
    lesson: Lesson,
    score: Int,
    onBack: () -> Unit,
    viewModel: AppViewModel
) {
    val pointsEarned = score * 10
    val accuracy = ((score.toFloat() / lesson.questions.size.toFloat()) * 100f).toInt()

    LaunchedEffect(Unit) {
        viewModel.completeLesson(lesson.title, score, lesson.questions.size, lesson.level)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "पाठ समाप्त! / Quiz Completed!",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Congratulations! बधाई हो! 🎉",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Accuracy", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "$accuracy%",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("XP Gained", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "+$pointsEarned XP",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "You got $score correct out of ${lesson.questions.size} challenges in the ${lesson.level} course.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("summary_back_home_button")
        ) {
            Text("Back to Dashboard / मुख्य स्क्रीन पर जाएं")
        }
    }
}

// Custom flow-like row layout supporting multi-wrapping for words
@Composable
fun PlayfulFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val lines = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentLine = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentLineWidth = 0
        var totalHeight = 0
        var maxWidth = 0

        val gap = 8.dp.roundToPx()

        placeables.forEach { placeable ->
            if (currentLineWidth + placeable.width > constraints.maxWidth) {
                lines.add(currentLine)
                totalHeight += (currentLine.maxOfOrNull { it.height } ?: 0) + gap
                maxWidth = maxOf(maxWidth, currentLineWidth)
                currentLine = mutableListOf()
                currentLineWidth = 0
            }
            currentLine.add(placeable)
            currentLineWidth += placeable.width + gap
        }
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
            totalHeight += (currentLine.maxOfOrNull { it.height } ?: 0)
            maxWidth = maxOf(maxWidth, currentLineWidth)
        }

        layout(
            width = minOf(constraints.maxWidth, maxOf(constraints.minWidth, maxWidth)),
            height = minOf(constraints.maxHeight, maxOf(constraints.minHeight, totalHeight))
        ) {
            var y = 0
            lines.forEach { line ->
                var x = 0
                val lineHeight = line.maxOfOrNull { it.height } ?: 0
                line.forEach { placeable ->
                    placeable.place(x, y)
                    x += placeable.width + gap
                }
                y += lineHeight + gap
            }
        }
    }
}
