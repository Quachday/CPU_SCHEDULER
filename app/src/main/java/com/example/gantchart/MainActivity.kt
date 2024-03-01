package com.example.gantchart


import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

        }


    }
    @Preview
    @Composable
    fun TableScreen() {
        // Just a fake data.txt... a Pair of Int and String

        // Each cell of a column must have the same weight.
        val column1Weight = .2f // 30%
        val column2Weight = .2f
        val column3Weight = .2f
        val column4Weight = .2f
        // 70%
        // The LazyColumn will be our table. Notice the use of the weights below
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
            // Here is the header
            item {
                Row(Modifier.background(Color.White)) {
                    TableCell(text = "ID", weight = column1Weight)
                    TableCell(text = "Arrival", weight = column2Weight)
                    TableCell(text = "Burst", weight = column3Weight)
                    TableCell(text = "Priority", weight = column4Weight)
                }
            }
            val fileName = "C:\\Users\\LTC\\AndroidStudioProjects\\gantchart\\app\\src\\main\\java\\com\\example\\gantchart\\data.txt"
            val lines: List<String> = File(fileName).readLines()
            // Here are all the lines of your table.
            for (i in 1..5) {
            item {
                AddRow("P$i",lines[i-1][0].toString(),
                    lines[i-1][2].toString(),
                    lines[i-1][4].toString(),
                    column1Weight)
            }
            }


            val processes = listOf(
                Process(1, lines[0][0].digitToIntOrNull()!!,
                    lines[0][2].digitToIntOrNull()!!,lines[0][4].digitToIntOrNull()!!),
                Process(2, lines[1][0].digitToIntOrNull()!!,
                    lines[1][2].digitToIntOrNull()!!,
                    lines[1][4].digitToIntOrNull()!!),
                Process(3, lines[2][0].digitToIntOrNull()!!,
                    lines[2][2].digitToIntOrNull()!!,lines[2][4].digitToIntOrNull()!!),
                Process(4, lines[3][0].digitToIntOrNull()!!,
                    lines[3][2].digitToIntOrNull()!!,lines[3][4].digitToIntOrNull()!!),
                Process(5 ,lines[4][0].digitToIntOrNull()!!,
                    lines[4][2].digitToIntOrNull()!!,lines[4][4].digitToIntOrNull()!!)
            )
            item() {
            sjf(processes)}
        }
    }

    data class Process(val id: Int, val arrivalTime: Int, var burstTime: Int, val priority: Int)

    @Composable
    fun sjf(processes: List<Process>) {
        val n = processes.size
        val burst = processes.map { it.burstTime }.toMutableList()
        val remainingTime = IntArray(n) { burst[it] }
        val completionTime = IntArray(n)
        val waitingTime = IntArray(n)
        val turnaroundTime = IntArray(n)

        var time = 0
        var minBurst = Int.MAX_VALUE
        var shortest = -1
        var check = false
        var finished = 0
        var switch = false
        // loop until finish = n - indicating that all process done
        Row(Modifier.background(Color.Green)) {
            TableCell(text = "Scheduling: ", weight = 0.2f)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.Green)
        )  {
            while (finished != n) {
                switch = false
                // Find shortest job in ready queue
                for (i in 0 until n) {
                    if (processes[i].arrivalTime <= time && remainingTime[i] < minBurst && remainingTime[i] > 0) {
                        minBurst = remainingTime[i]
                        shortest = i
                        check = true //check = true -> there is a process to run
                        switch = true
                    }
                }

                // if there is no process to run, time passes
                if (!check) {
                    time++
                    continue // return the start of while
                }

                if(switch){
                            TableCell(text = "P${shortest+1}\n$time", weight = 0.2f)
                }

                remainingTime[shortest]--

                minBurst = remainingTime[shortest]
                if (minBurst == 0) {
                    minBurst = Int.MAX_VALUE
                }

                // done 1 process - which is currently the shortest process
                if (remainingTime[shortest] == 0) {
                    finished++
                    completionTime[shortest] = time + 1
                    turnaroundTime[shortest] =
                        completionTime[shortest] - processes[shortest].arrivalTime
                    waitingTime[shortest] = turnaroundTime[shortest] - processes[shortest].burstTime
                    if (waitingTime[shortest] < 0) {
                        waitingTime[shortest] = 0
                    }
                    switch = true
                }
                time++
            }
            //total of time running
            var total: Int = 0
            burst.forEach{ e -> total += e}
            TableCell("E\n$total",0.2f)
        }
        val totalWaitingTime = waitingTime.sum()
        val averageWaitingTime = totalWaitingTime.toFloat() / n
        val totalTurnaroundTime = turnaroundTime.sum()
        val averageTurnaroundTime = totalTurnaroundTime.toFloat() / n
        Column(
            Modifier
                .fillMaxWidth()
               ) {


                Row(Modifier.background(Color.White)) {
                    TableCell(text = "ID", weight = 0.2f)
                    TableCell(text = "End", weight = 0.2f)
                    TableCell(text = "Waiting", weight = 0.2f)
                    TableCell(text = "TAround ", weight = 0.2f)
                }

        }
        println("Process\t Arrival Burst\t Completion\t Waiting\t Turnaround")
        for (i in 0 until n) {
            Row(Modifier.background(Color.White)) {
                TableCell(text = "P${i+1}", weight = 0.2f)
                TableCell(text = "${completionTime[i]}", weight = 0.2f)
                TableCell(text = "${waitingTime[i]}", weight = 0.2f)
                TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
            }
        }
        Row(Modifier.background(Color.White)) {
            TableCell(text = "Avg", weight = 0.2f)
            TableCell(text = "", weight = 0.2f)
            TableCell(text = "$averageWaitingTime", weight = 0.2f)
            TableCell(text = "$averageTurnaroundTime", weight = 0.2f)
        }

    }



    @Composable
    fun AddRow(id: String,arrival: String, burst: String, Priority:String, column1Weight : Float) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            TableCell(text = id, weight= column1Weight)
            TableCell(text = arrival, weight= column1Weight)
            TableCell(text = burst, weight= column1Weight)
            TableCell(text = Priority, weight= column1Weight)
        }
    }
    @Composable
    fun RowScope.TableCell(
        text: String,
        weight: Float
    ) {
        Text(
            text = text,
            Modifier
                .border(1.dp, Color.Black)
                .weight(weight)
                .padding(8.dp)
        )

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    @Composable
    fun HomeScreen() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet { /* Drawer content */
                Text("#1")
                    Text("#2")
                    Text("#3")
                    Text("#4")}
            },
        ) {
            Scaffold(
                topBar = {TopAppBar(drawerState, scope)}
            ) { contentPadding ->
                // Screen content
                var count by rememberSaveable() {
                    mutableStateOf(0)
                }
                UpperPanel(count, {count++})
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



    //@Preview(showBackground = true)

}
/*
var count by rememberSaveable() {
    mutableStateOf(0)
}
UpperPanel(count, {count++})
*/






