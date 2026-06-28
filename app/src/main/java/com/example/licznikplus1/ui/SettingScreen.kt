package com.example.licznikplus1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController,
    askChinese: Boolean,
    onAskChineseChange: (Boolean) -> Unit,
    askPolish: Boolean,
    onAskPolishChange: (Boolean) -> Unit,
    askAudio: Boolean,
    onAskAudioChange: (Boolean) -> Unit,
    autoPlayAudio: Boolean,
    onAutoPlayAudioChange: (Boolean) -> Unit,
    newCardsPerDay: Int,
    onNewCardsPerDayChange: (Int) -> Unit,
    learningCardsCount: Int,
    onLearningCardsCountChange: (Int) -> Unit,
    oneHandMode: Boolean,
    onOneHandModeChange: (Boolean) -> Unit,
    reviewNotificationsEnabled: Boolean,
    onReviewNotificationsEnabledChange: (Boolean) -> Unit,
    reviewNotificationThreshold: Int,
    onReviewNotificationThresholdChange: (Int) -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            item {

                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("← Wróć")
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Język pytań",
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Chiński")

                    Switch(
                        checked = askChinese,
                        onCheckedChange = onAskChineseChange
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Polski")

                    Switch(
                        checked = askPolish,
                        onCheckedChange = onAskPolishChange
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Audio")

                    Switch(
                        checked = askAudio,
                        onCheckedChange = onAskAudioChange
                    )
                }

                Spacer(Modifier.height(16.dp))

                Divider()

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Audio",
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Czytaj po odkryciu odpowiedzi")

                    Switch(
                        checked = autoPlayAudio,
                        onCheckedChange = onAutoPlayAudioChange
                    )
                }

                Spacer(Modifier.height(16.dp))

                Divider()

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Sesja",
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Nowe karty dziennie",
                    fontSize = 20.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            if (newCardsPerDay > 0) {
                                onNewCardsPerDayChange(newCardsPerDay - 1)
                            }
                        }
                    ) {
                        Text("-")
                    }
                    Box(
                        modifier = Modifier
                            .width(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$newCardsPerDay",
                            fontSize = 28.sp
                        )
                    }


                    Button(
                        onClick = {
                            onNewCardsPerDayChange(newCardsPerDay + 1)
                        }
                    ) {
                        Text("+")
                    }

                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Karty w trybie nauki",
                    fontSize = 20.sp
                )



                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            if (learningCardsCount > 0) {
                                onLearningCardsCountChange(
                                    learningCardsCount - 1
                                )
                            }
                        }
                    ) {
                        Text("-")
                    }
                    Box(
                        modifier = Modifier
                            .width(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$learningCardsCount",
                            fontSize = 28.sp
                        )
                    }
                    Button(
                        onClick = {
                            onLearningCardsCountChange(
                                learningCardsCount + 1
                            )
                        }
                    ) {
                        Text("+")
                    }

                }

                Spacer(Modifier.height(24.dp))
                Divider()
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Tryb jednej ręki")

                    Switch(
                        checked = oneHandMode,
                        onCheckedChange = onOneHandModeChange
                    )
                }
                Spacer(Modifier.height(24.dp))
                Divider()
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Włącz powaidomienia")

                    Switch(
                        checked = reviewNotificationsEnabled,
                        onCheckedChange = onReviewNotificationsEnabledChange
                    )
                }
                if (reviewNotificationsEnabled) {
                    Text(
                        text = "Liczba kart do wysłania powiadomienia",
                        fontSize = 14.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = {
                                if (reviewNotificationThreshold > 0) {
                                    onReviewNotificationThresholdChange(reviewNotificationThreshold - 1)
                                }
                            }
                        ) {
                            Text("-")
                        }
                        Box(
                            modifier = Modifier
                                .width(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$reviewNotificationThreshold",
                                fontSize = 28.sp
                            )
                        }


                        Button(
                            onClick = {
                                onReviewNotificationThresholdChange(reviewNotificationThreshold + 1)
                            }
                        ) {
                            Text("+")
                        }

                    }
                }
            }


        }



    }
}