package com.mdi2.addit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mdi2.addit.add.AddTab
import com.mdi2.addit.detail.TaskDetailTab
import com.mdi2.addit.edit.EditTaskTab
import com.mdi2.addit.home.HomeTab
import com.mdi2.addit.settingsScreen.SettingsTab
import com.mdi2.addit.ui.theme.ADDitTheme
import kotlinx.coroutines.delay


data class NavItem(val route:String, val icon: ImageVector)

class Destinations{
    companion object {
        val entries = listOf(
            NavItem(HOME, Icons.Filled.Home),
            NavItem(ADD, Icons.Filled.Add),
            NavItem(SETTINGS, Icons.Filled.Settings)
        )
        const val HOME = "Home"
        const val ADD = "Add"
        const val SETTINGS = "Settings"
        const val TASK_DETAIL = "TaskDetail"
        const val EDIT_TASK = "EditTask"
        const val SPLASH = "SPLASH"

    }
}
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ADDitTheme {
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {


                        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets){
                            Destinations.entries.forEach{
                                destination ->
                                    NavigationBarItem(
                                        selected = currentRoute == destination.route,
                                        onClick = {navController.navigate(destination.route){
                                            popUpTo(navController.graph.startDestinationId){
//                                                saveState = true
                                                inclusive = false
                                            }
                                            launchSingleTop = true
//                                            restoreState = true
                                        } },
                                        icon = {Icon(destination.icon, contentDescription = null)},
                                        label = {Text(destination.route)}
                                    )
                                }
                            }
                        }
                )
                {innerPadding ->
                    NavHost(navController = navController, startDestination = Destinations.SPLASH
                    , modifier = Modifier.padding(innerPadding)){
                        composable(Destinations.HOME){ HomeTab(navController) }
                        composable(Destinations.ADD){ AddTab(navController) }
                        composable(Destinations.SETTINGS){SettingsTab( )}
                        composable("${Destinations.TASK_DETAIL}/{taskId}") {
                            backStackEntry ->
                            val taskId = backStackEntry.arguments
                                ?.getString("taskId")
                                ?.toIntOrNull() ?: 0
                            TaskDetailTab(taskId = taskId, navController= navController)
                        }
                        composable( route = "${Destinations.EDIT_TASK}/{taskId}"){ backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
                            EditTaskTab(taskId = taskId, navController = navController)
                        }
                        composable(
                            Destinations.SPLASH
                        ) {
                            splashScreen(navController)
                        }
                    }
//                    LandingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// add a splash screen


@Composable
fun splashScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(
            Destinations.HOME
        ) {
            popUpTo(0)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.offset(y = (-80).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    R.drawable.addit_logo
                ),
                contentDescription = "ADDit Logo",
                modifier = Modifier.size(150.dp)
            )
            Text(
                text = "ADDit",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Track movies and TV shows"
            )
        }
    }
}