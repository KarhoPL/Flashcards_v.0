package com.example.licznikplus1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.licznikplus1.data.SessionMode
import com.example.licznikplus1.data.SettingsDataStore
import com.example.licznikplus1.notifications.NotificationHelper
import com.example.licznikplus1.notifications.ReviewNotificationWorker
import com.example.licznikplus1.ui.FlashcardScreen
import com.example.licznikplus1.ui.HomeScreen
import com.example.licznikplus1.ui.SettingsScreen
import com.example.licznikplus1.ui.game.GameScreen
import com.example.licznikplus1.ui.theme.LicznikPlus1Theme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val requestNotificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            // Na razie nic tutaj nie robimy.
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannel(this)
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        val request =
            OneTimeWorkRequestBuilder<ReviewNotificationWorker>()
                .build()

        val reviewWorker =
            PeriodicWorkRequestBuilder<ReviewNotificationWorker>(
                1,
                TimeUnit.HOURS
            ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "review_notifications",
                ExistingPeriodicWorkPolicy.UPDATE,
                reviewWorker
            )
        setContent{
            LicznikPlus1Theme {

                val context = LocalContext.current


                val settingsDataStore = remember {
                    SettingsDataStore(context)
                }

                val settings by settingsDataStore.settings.collectAsState(
                    initial = com.example.licznikplus1.data.AppSettings()
                )
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("learning") {
                        FlashcardScreen(
                            navController = navController,
                            autoPlayAudio = settings.autoPlayAudio,
                            sessionMode = SessionMode.LEARNING,
                            newCardsPerDay = settings.newCardsPerDay,
                            learningCardsCount = settings.learningCardsCount,
                            askChinese = settings.askChinese,
                            askPolish = settings.askPolish,
                            askAudio = settings.askAudio,
                            oneHandMode = settings.oneHandMode
                        )
                    }

                    composable("testing") {
                        FlashcardScreen(
                            navController = navController,
                            autoPlayAudio = settings.autoPlayAudio,
                            sessionMode = SessionMode.TESTING,
                            newCardsPerDay = settings.newCardsPerDay,
                            learningCardsCount = settings.learningCardsCount,
                            askChinese = settings.askChinese,
                            askPolish = settings.askPolish,
                            askAudio = settings.askAudio,
                            oneHandMode = settings.oneHandMode
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            navController = navController,
                            askChinese = settings.askChinese,
                            onAskChineseChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveAskChinese(it)
                                }
                            },

                            askPolish = settings.askPolish,
                            onAskPolishChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveAskPolish(it)
                                }
                            },

                            askAudio = settings.askAudio,
                            onAskAudioChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveAskAudio(it)
                                }
                            },
                            autoPlayAudio = settings.autoPlayAudio,
                            onAutoPlayAudioChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveAutoPlayAudio(it)
                                }
                            },
                            newCardsPerDay = settings.newCardsPerDay,
                            onNewCardsPerDayChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveNewCardsPerDay(it)
                                }
                            },
                            learningCardsCount = settings.learningCardsCount,
                            onLearningCardsCountChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveLearningCardsCount(it)
                                }
                            },
                            oneHandMode = settings.oneHandMode,
                            onOneHandModeChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveOneHandMode(it)
                                }
                            },
                            reviewNotificationsEnabled = settings.reviewNotificationsEnabled,
                            onReviewNotificationsEnabledChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveReviewNotificationsEnabled(it)
                                }
                            },
                            reviewNotificationThreshold = settings.reviewNotificationThreshold,
                            onReviewNotificationThresholdChange = {
                                coroutineScope.launch {
                                    settingsDataStore.saveReviewNotificationThreshold(it)
                                }
                            }
                        )
                    }
                    composable("game") {
                        GameScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}