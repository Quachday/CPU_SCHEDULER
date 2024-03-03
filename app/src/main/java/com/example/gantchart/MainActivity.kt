package com.example.gantchart


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    companion object {
        var mode: Int = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
                MyNavigation()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MyNavigation() {
        val navController = rememberNavController()
        Scaffold(bottomBar = {MyBottomNavigation(navController = navController)}) {
            Box(Modifier.padding(it)) {
                NavHost(navController = navController, startDestination = Home.route) {
                    composable(Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Select.route) {
                        SelectScreen(navController)
                    }
                    composable(Schedule.route) {
                        TableScreen(mode = MainActivity.mode)
                    }
                }
            }
        }

    }


    @Composable
    fun MyBottomNavigation(navController: NavController) {
        val destinationList = listOf<Destinations>(Home,Select)
        var selectedIndex by rememberSaveable() {
            mutableStateOf(0)
        }
        BottomAppBar() {
            destinationList.forEachIndexed { index, destinations ->
                NavigationBarItem(label = {Text(text = destinations.title)},
                    icon = {Icon(imageVector = destinations.icon, contentDescription = destinations.title)},
                    selected = index == selectedIndex ,
                    onClick = {
                    MainActivity.mode = 0
                    selectedIndex = index
                    navController.navigate(destinationList[index].route){
                        popUpTo(Home.route)
                        launchSingleTop = true
                    }})
            }
        }

    }




    @Composable
    fun ExtendedButtion(drawerState: DrawerState ,scope: CoroutineScope) {
        ExtendedFloatingActionButton(
            text = { Text("Show drawer") },
            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
            onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        )
    }
}







