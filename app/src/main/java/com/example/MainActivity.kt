package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.example.ui.screens.LessonPracticeScreen
import com.example.ui.screens.StudentHomeScreen
import com.example.ui.screens.StoryPracticeScreen
import com.example.ui.screens.TeacherDashboardScreen
import com.example.ui.theme.EnglishShikshaTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val progress by viewModel.userProgress.collectAsState()
            val screen by viewModel.currentScreen.collectAsState()

            // Safely read user progress customizations, default if database is loading
            val highContrast = progress?.highContrastMode ?: false
            val textMultiplier = progress?.textSizeMultiplier ?: 1.0f

            EnglishShikshaTheme(
                highContrastMode = highContrast,
                textSizeMultiplier = textMultiplier
            ) {
                // Outer scaffold support with automatic safe edge-to-edge content window insets
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (progress != null) {
                        val activeProgress = progress!!
                        when (val activeScreen = screen) {
                            is AppScreen.StudentHome -> {
                                StudentHomeScreen(
                                    viewModel = viewModel,
                                    progress = activeProgress,
                                    onStartLesson = { lesson ->
                                        viewModel.navigateTo(AppScreen.LessonPractice(lesson))
                                    },
                                    onStartStory = { story ->
                                        viewModel.navigateTo(AppScreen.StoryPractice(story))
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            is AppScreen.LessonPractice -> {
                                LessonPracticeScreen(
                                    viewModel = viewModel,
                                    lesson = activeScreen.lesson,
                                    onBack = {
                                        viewModel.navigateTo(AppScreen.StudentHome)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            is AppScreen.StoryPractice -> {
                                StoryPracticeScreen(
                                    viewModel = viewModel,
                                    story = activeScreen.story,
                                    onBack = {
                                        viewModel.navigateTo(AppScreen.StudentHome)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            is AppScreen.TeacherDashboard -> {
                                TeacherDashboardScreen(
                                    viewModel = viewModel,
                                    progress = activeProgress,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
