package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CustomHomework
import com.example.data.StudentScore
import com.example.data.UserProgress
import com.example.viewmodel.AppViewModel

@Composable
fun TeacherDashboardScreen(
    viewModel: AppViewModel,
    progress: UserProgress,
    modifier: Modifier = Modifier
) {
    val scores by viewModel.studentScores.collectAsState()
    val homeworkList by viewModel.customHomework.collectAsState()
    val backupState by viewModel.backupStatus.collectAsState()
    val backupLogs by viewModel.backupLogs.collectAsState()
    val lastBackup by viewModel.lastBackupTime.collectAsState()

    var activeTab by remember { mutableStateOf("METRICS") } // "METRICS", "HOMEWORK", "BACKUP"

    var inputEnglish by remember { mutableStateOf("") }
    var inputHindi by remember { mutableStateOf("") }
    var inputHint by remember { mutableStateOf("") }
    var inputHWLevel by remember { mutableStateOf("Beginner") }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Teacher Title & Exit Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "शिक्षक डैशबोर्ड",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Teacher Workspace",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Button(
                onClick = { viewModel.setRole("student") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .testTag("exit_teacher_workspace_button")
                    .semantics { contentDescription = "Exit to Student screen" }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Student view")
            }
        }

        // Segmented Tabs - Bento style
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(20.dp)
                )
                .padding(6.dp)
        ) {
            val tabs = listOf(
                "METRICS" to "छात्र प्रगति / Grades",
                "HOMEWORK" to "गृहकार्य / Homework",
                "BACKUP" to "क्लाउड बैकअप / Sync"
            )
            tabs.forEach { (key, label) ->
                val isSelected = activeTab == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                        .clickable { activeTab = key }
                        .testTag("teacher_tab_$key"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Content Switching
        when (activeTab) {
            "METRICS" -> {
                Column(modifier = Modifier.weight(1f)) {
                    // Highlights Board
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val avgAccuracy = if (scores.isNotEmpty()) {
                            scores.map { it.accuracy }.average().toInt()
                        } else 0

                        val totalTests = scores.size

                        DiagnosticMetricCard(
                            title = "Average Accuracy",
                            value = "$avgAccuracy%",
                            subtitle = "Cumulative performance",
                            modifier = Modifier.weight(1f),
                            progress = progress
                        )
                        DiagnosticMetricCard(
                            title = "Tests Answered",
                            value = "$totalTests",
                            subtitle = "Total quizzes logged",
                            modifier = Modifier.weight(1f),
                            progress = progress
                        )
                    }

                    // Student Progress Grade Table
                    Text(
                        text = "छात्र रिकॉर्ड तालिका / Student Activity Log",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    if (scores.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No student records logged yet. Records will populate as quizzes are taken.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(scores) { score ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = score.studentName,
                                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Badge(
                                                    containerColor = if (score.level == "Beginner") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary
                                                ) {
                                                    Text(
                                                        score.level,
                                                        color = if (score.level == "Beginner") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Quiz: ${score.lessonTitle}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                            )
                                            Text(
                                                text = "Completed: ${score.completionDate}",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }

                                        Column(
                                            horizontalAlignment = Alignment.End,
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = "${score.score}/${score.totalQuestions}",
                                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                                color = if (score.accuracy >= 70f) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                            )
                                            Text(
                                                text = "${score.accuracy.toInt()}% correct",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "HOMEWORK" -> {
                Column(modifier = Modifier.weight(1f)) {
                    // Create Custom Homework Form Card
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "नया गृहकार्य असाइन करें / Assign Custom Homework",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = inputEnglish,
                                onValueChange = { inputEnglish = it },
                                label = { Text("English Phrase / अंग्रेजी वाक्य") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("homework_english_input"),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = inputHindi,
                                onValueChange = { inputHindi = it },
                                label = { Text("Hindi Translation / हिंदी अनुवाद") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("homework_hindi_input"),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = inputHint,
                                onValueChange = { inputHint = it },
                                label = { Text("Teacher Hint / सुराग") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("homework_hint_input"),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Level Selection row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = inputHWLevel == "Beginner",
                                        onClick = { inputHWLevel = "Beginner" },
                                        modifier = Modifier.testTag("hw_radio_beginner")
                                    )
                                    Text("Beginner")
                                    Spacer(modifier = Modifier.width(12.dp))
                                    RadioButton(
                                        selected = inputHWLevel == "Advanced",
                                        onClick = { inputHWLevel = "Advanced" },
                                        modifier = Modifier.testTag("hw_radio_advanced")
                                    )
                                    Text("Advanced")
                                }

                                Button(
                                    onClick = {
                                        if (inputEnglish.isNotBlank() && inputHindi.isNotBlank()) {
                                            viewModel.addHomework(
                                                inputEnglish,
                                                inputHindi,
                                                inputHint,
                                                inputHWLevel
                                            )
                                            inputEnglish = ""
                                            inputHindi = ""
                                            inputHint = ""
                                            focusManager.clearFocus()
                                        }
                                    },
                                    modifier = Modifier.testTag("add_homework_button")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Save HW")
                                }
                            }
                        }
                    }

                    // Existing Homework List
                    Text(
                        text = "वर्तमान गृहकार्य कार्य / Assigned Homework Tasks",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (homeworkList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No custom homework assigned yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(homeworkList) { hw ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = hw.englishPhrase,
                                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Badge { Text(hw.level) }
                                            }
                                            Text(
                                                text = "Hindi: ${hw.hindiTranslation}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                            )
                                            if (hw.hint.isNotBlank()) {
                                                Text(
                                                    text = "Hint: ${hw.hint}",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = { viewModel.removeHomework(hw.id) },
                                            modifier = Modifier.testTag("delete_hw_${hw.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Delete homework",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "BACKUP" -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "सुरक्षित क्लाउड बैकअप और सिंक / Cyber Security Sync Node",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Ensures student data backup is synced to primary school node and cloud backups offline.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Last Successful Sync",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = lastBackup,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Button(
                                onClick = { viewModel.triggerCloudBackup() },
                                enabled = backupState != "SYNCING",
                                modifier = Modifier
                                    .testTag("cloud_backup_sync_button")
                                    .semantics { contentDescription = "Trigger secure cloud backup" }
                            ) {
                                if (backupState == "SYNCING") {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Encrypting & Syncing...")
                                } else {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp)) // standard icon representing sync node activity
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Force Sync Now")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "सुरक्षा और कनेक्शन लॉग / Security Logs",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Logs Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Black, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        if (backupLogs.isEmpty()) {
                            Text(
                                text = "> System Idle. Standby node waiting for transaction trigger.",
                                color = Color.Green,
                                style = MaterialTheme.typography.bodyMedium,
                                letterSpacing = 0.5.sp
                            )
                        } else {
                            LazyColumn(reverseLayout = false) {
                                items(backupLogs) { log ->
                                    Text(
                                        text = "> $log",
                                        color = if (log.contains("SUCCESS") || log.contains("successfully")) Color.Cyan else if (log.contains("Backup uploaded")) Color.Green else Color.LightGray,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        letterSpacing = 0.5.sp
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

@Composable
fun DiagnosticMetricCard(
    title: String,
    value: String,
    subtitle: String,
    progress: UserProgress,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .border(
                2.dp,
                if (progress.highContrastMode) MaterialTheme.colorScheme.primary else Color.Transparent,
                RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
            )
        }
    }
}
