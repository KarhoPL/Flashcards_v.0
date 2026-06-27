package com.example.licznikplus1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Fiszki",
            fontSize = 36.sp
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            onClick = {
                navController.navigate("learning")            }
        ) {
            Text("Tryb nauki")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                navController.navigate("testing")
            }
        ) {
            Text("Tryb testu")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                navController.navigate("game")
            }
        ) {
            Text("Gra")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                navController.navigate("settings")
            }
        ) {
            Text("Ustawienia")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = { }
        ) {
            Text("Statystyki (wkrótce)")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = { }
        ) {
            Text("Fiszki (wkrótce)")
        }
    }
}