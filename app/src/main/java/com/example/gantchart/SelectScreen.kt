package com.example.gantchart

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun SelectScreen(navController: NavController){

            // Screen content
            var count by rememberSaveable() {
                mutableStateOf(0)
            }
            var execute by rememberSaveable() {
                mutableStateOf(0)
            }
            UpperPanel(count, {count++},{execute = 1},
                {execute = 2}, {execute = 3},{execute = 4},{execute = 5}
                ,{execute = 6}
            )
            when{
                execute == 1 -> MainActivity.mode = 1
                execute == 2 -> MainActivity.mode = 2
                execute == 3 -> MainActivity.mode = 3
                execute == 4 -> MainActivity.mode = 4
                execute == 5 -> MainActivity.mode = 5
                execute == 6 -> MainActivity.mode = 6
            }
            if (MainActivity.mode != 0 ) navController.navigate(Schedule.route)

   }
