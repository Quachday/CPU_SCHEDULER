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
import androidx.compose.foundation.MutatePriority
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
import androidx.compose.foundation.layout.size
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
            HomeScreen()
        }


    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview
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
               // topBar = {TopAppBar(drawerState, scope)}
            ) { contentPadding ->
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
                    execute == 1 -> TableScreen(mode = 1)
                    execute == 2 -> TableScreen(mode = 2)
                    execute == 3 -> TableScreen(mode = 3)
                    execute == 4 -> TableScreen(mode = 4)
                    execute == 5 -> TableScreen(mode = 5)
                    execute == 6 -> TableScreen(mode = 6)
                }

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
    @Composable
    fun TableScreen(mode: Int) {
        // Just a fake data.txt... a Pair of Int and String

        // Each cell of a column must have the same weight.
        val column1Weight = .2f // 30%
        val column2Weight = .2f
        val column3Weight = .2f
        val column4Weight = .2f
        // 70%
        // The LazyColumn will be our table. Notice the use of the weights below
        LazyColumn(

                ) {
            // Here is the header
            item(){
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .size(150.dp)){
            }}
            item {
                Row(Modifier.background(Color.White)) {
                    TableCell(text = "ID", weight = column1Weight)
                    TableCell(text = "Arrival", weight = column2Weight)
                    TableCell(text = "Burst", weight = column3Weight)
                    TableCell(text = "Priority", weight = column4Weight)
                }
            }
            val fileName = "C:\\Users\\quach\\StudioProjects\\CPU_SCHEDULER\\app\\src\\main\\java\\com\\example\\gantchart\\data.txt"
            val lines: List<String> = File(fileName).readLines()
            var processes: MutableList<Process> = ArrayList()
            // Here are all the lines of your table.
            for (i in 1..lines.size) {
                val numbers = lines[i-1].split(' ')
                item {
                    AddRow(
                        "P$i", numbers[0],
                        numbers[1],
                        numbers[2],
                        column1Weight
                    )
                    processes.add(
                        Process(
                            i,
                            numbers[0].toInt(),
                            numbers[1].toInt(),
                            priority = numbers[2].toInt()
                        )
                    )
                }
            }

            item(){
                when {
                    mode == 1 -> {fcfs(processes)}
                    mode == 2 -> {sjf(processes)}
                    mode == 3 -> {sjf_nonpreemptive(processes)}
                    mode == 4 -> {priority_non(processes)}
                    mode == 5 -> {priority_preemptive(processes)}
                    mode == 6 -> {roundRobin(processes , quantum = 2)}
                }
            }

            item(){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .size(500.dp)){
                }}
        }
    }

    data class Process(val id: Int, val arrivalTime: Int,
                       val burstTime: Int, var completionTime: Int = 0,
                       var turnaroundTime: Int = 0,
                       var waitingTime: Int = 0,
                        var priority: Int)


    @Composable
    fun priority_preemptive(processes: List<Process>) {
        val n = processes.size
        val burst = processes.map { it.burstTime }.toMutableList()
        val remainingTime = IntArray(n) { burst[it] }
        val completionTime = IntArray(n)
        val waitingTime = IntArray(n)
        val turnaroundTime = IntArray(n)

        var time = 0
        var mostprioritized = Int.MAX_VALUE
        var mostprioritized_id = -1
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
                // Find the most prioritized job in ready queue (Better way is that whenever a new process comes in ready queue -> check, not after one second.)
                for (i in 0 until n) {
                    if (processes[i].arrivalTime <= time && processes[i].priority < mostprioritized && remainingTime[i] > 0) {
                        mostprioritized = processes[i].priority
                        mostprioritized_id = i
                        check = true //check = true -> there is a process to run
                        switch = true
                    }
                }

                // if there is no process to run, time passes
                if (!check) {
                    time++
                    continue // return the start of while
                }

                //draw gant chart
                if(switch){
                    TableCell(text = "P${mostprioritized_id+1}\n$time", weight = 0.2f)
                }

                remainingTime[mostprioritized_id]--



                // done 1 process - which is currently the shortest process
                if (remainingTime[mostprioritized_id] == 0) {
                    finished++
                    completionTime[mostprioritized_id] = time + 1
                    turnaroundTime[mostprioritized_id] = completionTime[mostprioritized_id] - processes[mostprioritized_id].arrivalTime
                    waitingTime[mostprioritized_id] = turnaroundTime[mostprioritized_id] - processes[mostprioritized_id].burstTime
                    if (waitingTime[mostprioritized_id] < 0) {
                        waitingTime[mostprioritized_id] = 0
                    }
                    mostprioritized = Int.MAX_VALUE
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

                //draw gant chart
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
    fun sjf_nonpreemptive(processes: List<Process>) {
        //Note: id starts from 1
        var sortedProcesses = processes.sortedBy { it.arrivalTime }
        var currentTime = 0
        var totalTime = 0
        val waitTimes = mutableListOf<Int>()
        val completionTime = IntArray(processes.size+1)
        val waitingTime = IntArray(processes.size+1)
        val turnaroundTime = IntArray(processes.size+1)

        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(Modifier.background(Color.Green)) {
                TableCell(text = "Scheduling:", weight = 0.2f)}
            Row(Modifier.background(Color.Green)) {
                while (sortedProcesses.isNotEmpty()) {
                    val availableProcesses =
                        sortedProcesses.filter { it.arrivalTime <= currentTime }

                    //no process available, time passes
                    if (availableProcesses.isEmpty()) {
                        currentTime++
                        continue
                    }

                    //fine the shortest
                    val shortestJob = availableProcesses.minByOrNull { it.burstTime }!!
                    //running seamlessly till finishing the job
                    sortedProcesses -= shortestJob
                    TableCell(
                        text = "P${shortestJob.id}\n${currentTime}",
                        weight = 0.2f
                    )


                    val waitTime = currentTime - shortestJob.arrivalTime
                    waitTimes += waitTime
                    currentTime += shortestJob.burstTime
                    totalTime += currentTime - shortestJob.arrivalTime
                    val taroundTime = currentTime - shortestJob.arrivalTime
                    completionTime[shortestJob.id] = currentTime
                    waitingTime[shortestJob.id] = waitTime
                    turnaroundTime[shortestJob.id] = taroundTime



                }
                TableCell(
                    text = "End\n$currentTime",
                    weight = 0.2f
                )
            }
            Row(Modifier.background(Color.White)) {
                TableCell(text = "ID", weight = 0.2f)
                TableCell(text = "End", weight = 0.2f)
                TableCell(text = "Waiting", weight = 0.2f)
                TableCell(text = "TAround ", weight = 0.2f)
            }
            for (i in 1..processes.size)
                Row(Modifier.background(Color.White)) {
                    TableCell(text = "P${i}", weight = 0.2f)
                    TableCell(text = "${completionTime[i]}", weight = 0.2f)
                    TableCell(text = "${waitingTime[i]}", weight = 0.2f)
                    TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
                }
        }

        val avgWaitTime = waitTimes.average()
        val avgTurnaroundTime = totalTime.toDouble() / processes.size
        Row(Modifier.background(Color.White)) {
            TableCell(text = "Avg", weight = 0.2f)
            TableCell(text = "", weight = 0.2f)
            TableCell(text = "$avgWaitTime", weight = 0.2f)
            TableCell(text = "$avgTurnaroundTime", weight = 0.2f)
        }
    }

    @Composable
    fun priority_non(processes: List<Process>) {
        //Note: id starts from 1
        var sortedProcesses = processes.sortedBy { it.arrivalTime }
        var currentTime = 0
        var totalTime = 0
        val waitTimes = mutableListOf<Int>()
        val completionTime = IntArray(processes.size+1)
        val waitingTime = IntArray(processes.size+1)
        val turnaroundTime = IntArray(processes.size+1)

        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(Modifier.background(Color.Green)) {
                TableCell(text = "Scheduling:", weight = 0.2f)}
            Row(Modifier.background(Color.Green)) {
                while (sortedProcesses.isNotEmpty()) {
                    val availableProcesses =
                        sortedProcesses.filter { it.arrivalTime <= currentTime }

                    //no process available, time passes
                    if (availableProcesses.isEmpty()) {
                        currentTime++
                        continue
                    }

                    //fine the shortest
                    val prioritizedJob = availableProcesses.minByOrNull { it.priority}!!
                    //running seamlessly till finishing the job
                    sortedProcesses -= prioritizedJob
                    TableCell(
                        text = "P${prioritizedJob.id}\n${currentTime}",
                        weight = 0.2f
                    )


                    val waitTime = currentTime - prioritizedJob.arrivalTime
                    waitTimes += waitTime
                    currentTime += prioritizedJob.burstTime
                    totalTime += currentTime - prioritizedJob.arrivalTime
                    val taroundTime = currentTime - prioritizedJob.arrivalTime
                    completionTime[prioritizedJob.id] = currentTime
                    waitingTime[prioritizedJob.id] = waitTime
                    turnaroundTime[prioritizedJob.id] = taroundTime



                }
                TableCell(
                    text = "End\n$currentTime",
                    weight = 0.2f
                )
            }
            Row(Modifier.background(Color.White)) {
                TableCell(text = "ID", weight = 0.2f)
                TableCell(text = "End", weight = 0.2f)
                TableCell(text = "Waiting", weight = 0.2f)
                TableCell(text = "TAround ", weight = 0.2f)
            }
            for (i in 1..processes.size)
                Row(Modifier.background(Color.White)) {
                    TableCell(text = "P${i}", weight = 0.2f)
                    TableCell(text = "${completionTime[i]}", weight = 0.2f)
                    TableCell(text = "${waitingTime[i]}", weight = 0.2f)
                    TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
                }
        }

        val avgWaitTime = waitTimes.average()
        val avgTurnaroundTime = totalTime.toDouble() / processes.size
        Row(Modifier.background(Color.White)) {
            TableCell(text = "Avg", weight = 0.2f)
            TableCell(text = "", weight = 0.2f)
            TableCell(text = "$avgWaitTime", weight = 0.2f)
            TableCell(text = "$avgTurnaroundTime", weight = 0.2f)
        }
    }
    @Composable
    fun fcfs(processes: List<Process>) {
        //Note: id starts from 1
        var sortedProcesses = processes.sortedBy { it.arrivalTime }
        var currentTime = 0
        var totalTime = 0
        val waitTimes = mutableListOf<Int>()
        val completionTime = IntArray(processes.size+1)
        val waitingTime = IntArray(processes.size+1)
        val turnaroundTime = IntArray(processes.size+1)

        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(Modifier.background(Color.Green)) {
                TableCell(text = "Scheduling:", weight = 0.2f)}
            Row(Modifier.background(Color.Green)) {
                while (sortedProcesses.isNotEmpty()) {
                    val availableProcesses =
                        sortedProcesses.filter { it.arrivalTime <= currentTime }

                    //no process available, time passes
                    if (availableProcesses.isEmpty()) {
                        currentTime++
                        continue
                    }

                    //fine the shortest
                    val shortestJob = availableProcesses[0]!!
                    //running seamlessly till finishing the job
                    sortedProcesses -= shortestJob
                    TableCell(
                        text = "P${shortestJob.id}\n${currentTime}",
                        weight = 0.2f
                    )


                    val waitTime = currentTime - shortestJob.arrivalTime
                    waitTimes += waitTime
                    currentTime += shortestJob.burstTime
                    totalTime += currentTime - shortestJob.arrivalTime
                    val taroundTime = currentTime - shortestJob.arrivalTime
                    completionTime[shortestJob.id] = currentTime
                    waitingTime[shortestJob.id] = waitTime
                    turnaroundTime[shortestJob.id] = taroundTime

                }
                TableCell(
                    text = "End\n$currentTime",
                    weight = 0.2f
                )
            }
            Row(Modifier.background(Color.White)) {
                TableCell(text = "ID", weight = 0.2f)
                TableCell(text = "End", weight = 0.2f)
                TableCell(text = "Waiting", weight = 0.2f)
                TableCell(text = "TAround ", weight = 0.2f)
            }
            for (i in 1..processes.size)
                Row(Modifier.background(Color.White)) {
                    TableCell(text = "P${i}", weight = 0.2f)
                    TableCell(text = "${completionTime[i]}", weight = 0.2f)
                    TableCell(text = "${waitingTime[i]}", weight = 0.2f)
                    TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
                }
        }

        val avgWaitTime = waitTimes.average()
        val avgTurnaroundTime = totalTime.toDouble() / processes.size
        Row(Modifier.background(Color.White)) {
            TableCell(text = "Avg", weight = 0.2f)
            TableCell(text = "", weight = 0.2f)
            TableCell(text = "$avgWaitTime", weight = 0.2f)
            TableCell(text = "$avgTurnaroundTime", weight = 0.2f)
        }
    }
    @Composable
    fun roundRobin(processes: List<Process>, quantum: Int) {
        var sortedProcesses = processes.sortedBy { it.arrivalTime }
        val remainingTime = processes.map { it.burstTime }.toMutableList()
        val waitingTime = IntArray(processes.size+1)
        val completionTime = IntArray(processes.size+1)
        val turnaroundTime = IntArray(processes.size+1)
        var time = 0
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(Modifier.background(Color.Green)) {
                TableCell(text = "Scheduling:", weight = 0.2f)
            }
            Row(Modifier.background(Color.Green)) {
               /* while (sortedProcesses.isNotEmpty()) {
                    val availableProcesses =
                        sortedProcesses.filter { it.arrivalTime <= time }
                    if(availableProcesses.isNotEmpty()){
                    for (i in 1..availableProcesses.size) {
                        if (remainingTime[availableProcesses[i].id] > 0) {
                            TableCell(text = "P${i+1}\n$time", weight = 0.2f)
                            if (remainingTime[availableProcesses[i].id] > quantum) {
                                time += quantum
                                remainingTime[availableProcesses[i].id] -= quantum
                            } else {
                                time += remainingTime[availableProcesses[i].id]
                                waitingTime[availableProcesses[i].id] =
                                    time - availableProcesses[availableProcesses[i].id].burstTime
                                - availableProcesses[availableProcesses[i].id].arrivalTime
                                remainingTime[availableProcesses[i].id] = 0
                                completionTime[availableProcesses[i].id] = time
                                sortedProcesses -= availableProcesses[i]
                            }
                        }
                    }}
                    else {
                        time++

                    }

                }*/
            }

            Row(Modifier.background(Color.White)) {
                TableCell(text = "ID", weight = 0.2f)
                TableCell(text = "End", weight = 0.2f)
                TableCell(text = "Waiting", weight = 0.2f)
                TableCell(text = "TAround ", weight = 0.2f)
            }

            for (i in 1..processes.size) {
                turnaroundTime[i] = completionTime[i] - processes[i - 1].arrivalTime
                Row(Modifier.background(Color.White)) {
                    TableCell(text = "P${i}", weight = 0.2f)
                    TableCell(text = "${completionTime[i]}", weight = 0.2f)
                    TableCell(text = "${waitingTime[2]}", weight = 0.2f)
                    TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
                }
            }
            val totalWaitingTime = waitingTime.sum()
            val averageWaitingTime = totalWaitingTime.toFloat() / processes.size
            val totalTurnaroundTime = turnaroundTime.sum()
            val averageTurnaroundTime = totalTurnaroundTime.toFloat() / processes.size
            Row(Modifier.background(Color.White)) {
                TableCell(text = "Avg", weight = 0.2f)
                TableCell(text = "", weight = 0.2f)
                TableCell(text = "$averageWaitingTime", weight = 0.2f)
                TableCell(text = "$averageTurnaroundTime", weight = 0.2f)
            }
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

}
/*
var count by rememberSaveable() {
    mutableStateOf(0)
}
UpperPanel(count, {count++})
*/






