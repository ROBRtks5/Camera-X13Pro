package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.MainCameraScreen
import com.example.ui.screens.MasterGuideScreen
import com.example.ui.theme.TeleCamTheme
import com.example.viewmodel.CameraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TeleCamTheme {
                val navController = rememberNavController()
                val cameraViewModel: CameraViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "camera"
                ) {
                    composable("camera") {
                        MainCameraScreen(
                            viewModel = cameraViewModel,
                            onNavigateToGallery = { navController.navigate("gallery") },
                            onNavigateToGuide = { navController.navigate("guide") }
                        )
                    }

                    composable("gallery") {
                        GalleryScreen(
                            viewModel = cameraViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("guide") {
                        MasterGuideScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
