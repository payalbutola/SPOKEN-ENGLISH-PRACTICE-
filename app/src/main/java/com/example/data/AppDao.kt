package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    fun getUserProgress(): Flow<UserProgress?>

    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    suspend fun getUserProgressOnce(): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProgress(progress: UserProgress)

    @Query("SELECT * FROM student_score ORDER BY id DESC")
    fun getAllStudentScores(): Flow<List<StudentScore>>

    @Query("SELECT * FROM student_score ORDER BY id DESC")
    suspend fun getAllStudentScoresOnce(): List<StudentScore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentScore(score: StudentScore)

    @Query("UPDATE student_score SET isBackupSynced = 1")
    suspend fun markAllScoresSynced()

    @Query("SELECT * FROM custom_homework ORDER BY id DESC")
    fun getAllCustomHomework(): Flow<List<CustomHomework>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomHomework(homework: CustomHomework)

    @Query("DELETE FROM custom_homework WHERE id = :id")
    suspend fun deleteCustomHomework(id: Int)

    @Query("SELECT * FROM downloaded_lesson")
    fun getAllDownloadedLessons(): Flow<List<DownloadedLesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadedLesson(downloaded: DownloadedLesson)

    @Query("DELETE FROM downloaded_lesson WHERE id = :id")
    suspend fun deleteDownloadedLesson(id: String)
}
