package com.example.gantchart

import java.text.ParseException

// Java program for implementation of FCFS
// scheduling
internal object GFG {
    // Function to find the waiting time for all
    // processes
    fun findWaitingTime(
        processes: IntArray?, n: Int,
        bt: IntArray, wt: IntArray
    ) {
        // waiting time for first process is 0
        wt[0] = 0

        // calculating waiting time
        for (i in 1 until n) {
            wt[i] = bt[i - 1] + wt[i - 1]
        }
    }

    // Function to calculate turn around time
    fun findTurnAroundTime(
        processes: IntArray?, n: Int,
        bt: IntArray, wt: IntArray, tat: IntArray
    ) {
        // calculating turnaround time by adding
        // bt[i] + wt[i]
        for (i in 0 until n) {
            tat[i] = bt[i] + wt[i]
        }
    }

    //Function to calculate average time
    fun findavgTime(processes: IntArray?, n: Int, bt: IntArray) {
        val wt = IntArray(n)
        val tat = IntArray(n)
        var total_wt = 0
        var total_tat = 0

        //Function to find waiting time of all processes
        findWaitingTime(processes, n, bt, wt)

        //Function to find turn around time for all processes
        findTurnAroundTime(processes, n, bt, wt, tat)

        //Display processes along with all details
        System.out.printf(
            "Processes Burst time Waiting"
                    + " time Turn around time\n"
        )

        // Calculate total waiting time and total turn
        // around time
        for (i in 0 until n) {
            total_wt = total_wt + wt[i]
            total_tat = total_tat + tat[i]
            System.out.printf(" %d ", i + 1)
            System.out.printf("	 %d ", bt[i])
            System.out.printf("	 %d", wt[i])
            System.out.printf("	 %d\n", tat[i])
        }
        val s = total_wt.toFloat() / n.toFloat()
        val t = total_tat / n
        System.out.printf("Average waiting time = %f", s)
        System.out.printf("\n")
        System.out.printf("Average turn around time = %d ", t)
    }

    // Driver code
    @Throws(ParseException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        //process id's
        val processes = intArrayOf(1, 2, 3)
        val n = processes.size

        //Burst time of all processes
        val burst_time = intArrayOf(10, 5, 8)
        findavgTime(processes, n, burst_time)
    }
}
