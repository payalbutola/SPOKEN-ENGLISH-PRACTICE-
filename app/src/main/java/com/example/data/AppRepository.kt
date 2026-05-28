package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
    val userProgress: Flow<UserProgress?> = dao.getUserProgress()
    val allScores: Flow<List<StudentScore>> = dao.getAllStudentScores()
    val allHomework: Flow<List<CustomHomework>> = dao.getAllCustomHomework()
    val allDownloadedLessons: Flow<List<DownloadedLesson>> = dao.getAllDownloadedLessons()

    suspend fun getUserProgressOnce(): UserProgress? {
        return dao.getUserProgressOnce()
    }

    suspend fun saveUserProgress(progress: UserProgress) {
        dao.saveUserProgress(progress)
    }

    suspend fun insertStudentScore(score: StudentScore) {
        dao.insertStudentScore(score)
    }

    suspend fun markAllScoresSynced() {
        dao.markAllScoresSynced()
    }

    suspend fun insertCustomHomework(homework: CustomHomework) {
        dao.insertCustomHomework(homework)
    }

    suspend fun deleteCustomHomework(id: Int) {
        dao.deleteCustomHomework(id)
    }

    suspend fun insertDownloadedLesson(id: String, isStory: Boolean, title: String) {
        dao.insertDownloadedLesson(DownloadedLesson(id, isStory, title))
    }

    suspend fun deleteDownloadedLesson(id: String) {
        dao.deleteDownloadedLesson(id)
    }

    suspend fun getAllStudentScoresOnce(): List<StudentScore> {
        return dao.getAllStudentScoresOnce()
    }
}
