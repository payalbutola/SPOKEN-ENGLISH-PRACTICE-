package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1, // Single student profile for simplicity
    val name: String = "Rahul Kumar",
    val role: String = "student", // "student" or "teacher"
    val currentLevel: String = "Beginner", // "Beginner" or "Advanced"
    val xp: Int = 120,
    val streak: Int = 3,
    val lastActiveDate: String = "2026-05-27", // YYYY-MM-DD format
    val textSizeMultiplier: Float = 1.0f, // 1.0, 1.25, 1.5, 1.75
    val highContrastMode: Boolean = false
)

@Entity(tableName = "student_score")
data class StudentScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val lessonTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val completionDate: String,
    val accuracy: Float,
    val level: String, // "Beginner" or "Advanced"
    val isBackupSynced: Boolean = false
)

@Entity(tableName = "custom_homework")
data class CustomHomework(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val englishPhrase: String,
    val hindiTranslation: String,
    val hint: String,
    val level: String // "Beginner" or "Advanced"
)
