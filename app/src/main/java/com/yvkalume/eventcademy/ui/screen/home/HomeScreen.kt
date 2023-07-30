package com.yvkalume.eventcademy.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yvkalume.eventcademy.util.ThemePreview

@Composable
fun HomeScreen(navController: NavController) {

}

@Preview
@Composable
fun HomeScreenPreview() {
    ThemePreview {
        HomeScreen(navController = rememberNavController())
    }
}