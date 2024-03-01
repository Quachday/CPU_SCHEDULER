package com.example.gantchart

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UpperPanel( count : Int, onIncrement: () -> Unit ) {
    var context = LocalContext.current

    Column(
        Modifier
            .fillMaxHeight()
            .padding(4.dp)
            .background(Color(0XFF495E57)),
        verticalArrangement =  Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally) {
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
        Button(onClick = {
            Toast.makeText(context, "Draw Succesful!", Toast.LENGTH_SHORT).show()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "Draw", color = Color.Black)
        }

        Image(painterResource(R.drawable.draw),contentDescription = "")
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton( onClick = { onIncrement() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add")
            }
            Text(text = "$count")
        }
    }
}