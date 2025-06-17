package com.mario8a.trackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.GoogleMap
import com.mario8a.trackerapp.presentation.maps.MapsSection

import com.mario8a.trackerapp.ui.theme.TrackerappTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            TrackerappTheme {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = MapScreenDes
                        ){
                            composable<MapScreenDes> (){
                                MapsSection(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            composable<CameraScreenDes> {

                            }
                        }
                    }
            }
        }
    }
}
@Serializable
data object MapScreenDes

@Serializable
data object CameraScreenDes

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrackerappTheme {
        Greeting("Android")
    }
}