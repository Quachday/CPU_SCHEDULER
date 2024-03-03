package com.example.gantchart


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.io.File


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
   // val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    //val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxHeight()
            .padding(4.dp)
            .background(Color(0XFF495E57)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TopAppBar(drawerState, scope)
        Text(
            text = "Gant Chart",
            fontSize = 32.sp,
            color = Color(0xFFF4CE14),
        )
        Text(
            text = "CPU SCHEDULER",
            fontSize = 32.sp,
            color = Color(0xFFF4CE14),
        )
        Row() {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "ID") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp)
                // Adjust the weight to control the size of TextField
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Arrival") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp, end = 5.dp)
                // Adjust the weight to control the size of TextField
            )

            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Bust Time") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)// Adjust the weight to control the size of TextField
            )
        }
        Image(painterResource(R.drawable.linux), contentDescription = "")
    }

}



/*
var count by rememberSaveable() {
    mutableStateOf(0)
}
UpperPanel(count, {count++})
*/

/*
@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text="Home Screen", color = Color.Cyan)
        Button(onClick = { navController.navigate(Schedule.route)
                         },
            colors = ButtonDefaults.buttonColors(containerColor =  Color(0XFFF4CE14))) {
            Text(text = "Start Scheduling")
        }
    }
}
*/
