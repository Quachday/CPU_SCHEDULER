package com.example.gantchart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.io.File


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
                mode == 6 -> {
                    var burst = processes.sortedBy { it.burstTime }
                    var quantum = burst[(processes.size*0.8-1).toInt()].burstTime
                    roundRobin_2(processes , quantum=2)
                }
            }
        }

       /* item(){
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .size(500.dp)){
            }
        }*/
    }
}

data class Process(val id: Int, val arrivalTime: Int,
                   val burstTime: Int, var completionTime: Int = 0,
                   var turnaroundTime: Int = 0,
                   var waitingTime: Int = 0,
                   var priority: Int,
                   var firstRun:Int = -1)


@Composable
fun priority_preemptive(processes: List<Process>) {
    val n = processes.size
    val burst = processes.map { it.burstTime }.toMutableList()
    val remainingTime = IntArray(n) { burst[it] }
    val completionTime = IntArray(n)
    val waitingTime = IntArray(n)
    val turnaroundTime = IntArray(n)
    var responseTime = IntArray(n)

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
                    if (processes[i].firstRun == -1) {
                        processes[i].firstRun = time
                    }
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
    Draw_Scheduling()
    for (i in 0 until n) {
        responseTime[i] = processes[i].firstRun - processes[i].arrivalTime
        Row(Modifier.background(Color.White)) {
            TableCell(text = "P${i+1}", weight = 0.2f)
            TableCell(text = "${completionTime[i]}", weight = 0.2f)
            TableCell(text = "${waitingTime[i]}", weight = 0.2f)
            TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
            TableCell(text = "${responseTime[i]}", weight = 0.2f)
        }
    }
    val averageWaitingTime = waitingTime.average()
    val averageTurnaroundTime = turnaroundTime.average()
    val averageResponseTime = responseTime.average()
    Draw_avg(averageWaitingTime, averageTurnaroundTime,
        averageResponseTime )

}

@Composable
fun sjf(processes: List<Process>) {
    val n = processes.size
    val burst = processes.map { it.burstTime }.toMutableList()
    val remainingTime = IntArray(n) { burst[it] }
    val completionTime = IntArray(n)
    val waitingTime = IntArray(n)
    val turnaroundTime = IntArray(n)
    val responseTime = IntArray(n)

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
            if (processes[shortest].firstRun == -1) {
                processes[shortest].firstRun = time
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

    Draw_Scheduling()
    for (i in 0 until n) {
        responseTime[i] = processes[i].firstRun - processes[i].arrivalTime
        Row(Modifier.background(Color.White)) {
            TableCell(text = "P${i+1}", weight = 0.2f)
            TableCell(text = "${completionTime[i]}", weight = 0.2f)
            TableCell(text = "${waitingTime[i]}", weight = 0.2f)
            TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
            TableCell(text = "${responseTime[i]}", weight = 0.2f)
        }
    }
    val averageWaitingTime = waitingTime.average()
    val averageTurnaroundTime = turnaroundTime.average()
    val averageResponseTime = responseTime.average()
    Draw_avg(averageWaitingTime, averageTurnaroundTime,
        averageResponseTime )

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
    val responseTime = IntArray(processes.size+1)

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
                processes[shortestJob.id-1].firstRun = currentTime

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
        Draw_Scheduling()
        for (i in 0 until processes.size) {
            responseTime[i+1] = processes[i].firstRun - processes[i].arrivalTime
            Row(Modifier.background(Color.White)) {
                TableCell(text = "P${i+1}", weight = 0.2f)
                TableCell(text = "${completionTime[i+1]}", weight = 0.2f)
                TableCell(text = "${waitingTime[i+1]}", weight = 0.2f)
                TableCell(text = "${turnaroundTime[i+1]}", weight = 0.2f)
                TableCell(text = "${responseTime[i+1]}", weight = 0.2f)
            }
        }
    }


    val avgWaitTime = waitTimes.average()
    val avgTurnaroundTime = turnaroundTime.sum().toDouble() / processes.size
    val avgResponseTime = responseTime.sum().toDouble() / processes.size
    Draw_avg(avgWaitTime, avgTurnaroundTime, avgResponseTime)
}

//PRI-NON
//1- First input the processes with their burst time
//   and priority.
//2- Sort the processes, burst time and priority
//   according to the priority.
//3- Now simply apply FCFS algorithm.
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
    val responseTime = IntArray(processes.size+1)

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
                processes[prioritizedJob.id-1].firstRun = currentTime

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
        Draw_Scheduling()
        for (i in 0 until processes.size){
            responseTime[i+1] = processes[i].firstRun - processes[i].arrivalTime
            Row(Modifier.background(Color.White)) {
                TableCell(text = "P${i+1}", weight = 0.2f)
                TableCell(text = "${completionTime[i+1]}", weight = 0.2f)
                TableCell(text = "${waitingTime[i+1]}", weight = 0.2f)
                TableCell(text = "${turnaroundTime[i+1]}", weight = 0.2f)
                TableCell(text = "${responseTime[i+1]}", weight = 0.2f)
            }
        }
    }

    responseTime[0]= 0
    val avgWaitTime = waitTimes.average()
    val avgTurnaroundTime = turnaroundTime.sum().toDouble() / processes.size
    val avgResponseTime = responseTime.sum().toDouble() / processes.size
    Draw_avg(avgWaitTime = avgWaitTime,
        avgTurnaroundTime =avgTurnaroundTime ,
        avgResponseTime =avgResponseTime )
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
    val responseTime = IntArray(processes.size+1)
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
                val firstJob = availableProcesses[0]!!
                //running seamlessly till finishing the job
                processes[firstJob.id-1].firstRun = currentTime
                sortedProcesses -= firstJob
                TableCell(
                    text = "P${firstJob.id}\n${currentTime}",
                    weight = 0.2f
                )


                val waitTime = currentTime - firstJob.arrivalTime
                waitTimes += waitTime
                currentTime += firstJob.burstTime
                totalTime += currentTime - firstJob.arrivalTime
                val taroundTime = currentTime - firstJob.arrivalTime
                completionTime[firstJob.id] = currentTime
                waitingTime[firstJob.id] = waitTime
                turnaroundTime[firstJob.id] = taroundTime

            }
            TableCell(
                text = "End\n$currentTime",
                weight = 0.2f
            )
        }
        Draw_Scheduling()
        for (i in 1..processes.size) {
            responseTime[i] = processes[i-1].firstRun - processes[i-1].arrivalTime

            Row(Modifier.background(Color.White)) {
                TableCell(text = "P${i}", weight = 0.2f)
                TableCell(text = "${completionTime[i]}", weight = 0.2f)
                TableCell(text = "${waitingTime[i]}", weight = 0.2f)
                TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
                TableCell(text = "${responseTime[i]}", weight = 0.2f)
            }
        }
    }

    val avgWaitTime = waitTimes.average()
    val avgTurnaroundTime = totalTime.toDouble() / processes.size
    val avgResponseTime = responseTime.sum().toDouble() / processes.size
    Draw_avg(avgWaitTime,avgTurnaroundTime, avgResponseTime)
}


//this below code is true for the situation of all processes arrive
//at the same time
@Composable
fun roundRobin_2(processes: List<Process>, quantum: Int){
    val remainingTime = processes.map { it.burstTime }.toMutableList()
    val waitingTime = IntArray(processes.size) { 0 }
    val completionTime = IntArray(processes.size) { 0 }
    val turnaroundTime = IntArray(processes.size) { 0 }
    val responseTime = IntArray(processes.size) { 0 }
    var time = 0
    var index = 0
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Row(Modifier.background(Color.Green)) {
            TableCell(text = "Scheduling: quantum = $quantum (>~80%)", weight = 0.2f)}
        Row(Modifier.background(Color.Green)) {
            while (true) {
                var done = true
                for (i in processes.indices) {
                    if (remainingTime[i] > 0) {
                        TableCell(text = "P${i+1}\n$time", weight =0.2f )
                        if (processes[i].firstRun == -1) {
                            processes[i].firstRun = time
                        }
                        done = false
                        if (remainingTime[i] > quantum) {
                            time += quantum
                            remainingTime[i] -= quantum
                        } else {
                            time += remainingTime[i]
                            waitingTime[i] =
                                time - processes[i].burstTime - processes[i].arrivalTime
                            remainingTime[i] = 0
                            completionTime[i] = time
                        }
                    }
                }
                if (done) {
                    TableCell(text = "E\n$time", weight =0.2f )
                    break
                }
            }
        }

    }
    Draw_Scheduling()
    for (i in 0 until processes.size) {
        responseTime[i] = processes[i].firstRun - processes[i].arrivalTime
        turnaroundTime[i] = completionTime[i] - processes[i].arrivalTime
        Row(Modifier.background(Color.White)) {
            TableCell(text = "P${i+1}", weight = 0.2f)
            TableCell(text = "${completionTime[i]}", weight = 0.2f)
            TableCell(text = "${waitingTime[i]}", weight = 0.2f)
            TableCell(text = "${turnaroundTime[i]}", weight = 0.2f)
            TableCell(text = "${responseTime[i]}", weight = 0.2f)
        }
    }
    val avgWaitTime = waitingTime.average()
    val avgTurnaroundTime = turnaroundTime.average()
    val avgResponseTime = responseTime.average()
    Draw_avg(avgWaitTime,avgTurnaroundTime,avgResponseTime)
}

@Composable
fun Draw_Scheduling() {
    Row(Modifier.background(Color.White)) {
        TableCell(text = "ID", weight = 0.2f)
        TableCell(text = "End", weight = 0.2f)
        TableCell(text = "Waiting", weight = 0.2f)
        TableCell(text = "TAround ", weight = 0.2f)
        TableCell(text = "Res ", weight = 0.2f)
    }
}
@Composable
fun Draw_avg(avgWaitTime:Double, avgTurnaroundTime: Double,
             avgResponseTime: Double) {
    Row(Modifier.background(Color.White)) {
        TableCell(text = "Avg", weight = 0.2f)
        TableCell(text = "", weight = 0.2f)
        TableCell(text = "$avgWaitTime", weight = 0.2f)
        TableCell(text = "$avgTurnaroundTime", weight = 0.2f)
        TableCell(text = "$avgResponseTime", weight = 0.2f)
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