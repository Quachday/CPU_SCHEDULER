package com.example.gantchart

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.gantchart.MainActivity
import java.io.File

@Composable
fun UpperPanel( count : Int, onIncrement: () -> Unit, onFCFS: () -> Unit, onSJF_pre: () -> Unit,onSJF_non: () -> Unit,
                onPri_non: () -> Unit,onPri_pre: () -> Unit,onRobin_Round: () -> Unit
                ) {
    var context = LocalContext.current

    Column(
        Modifier
            .fillMaxHeight()
            .padding(4.dp)
            .background(Color(0XFF495E57)),
        verticalArrangement =  Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally) {

        Button(onClick = { onFCFS()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "FCFS", color = Color.Black)
        }
        Button(onClick = { onSJF_pre()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "SJF-pre", color = Color.Black)
        }
        Button(onClick = { onSJF_non()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "SJF-non", color = Color.Black)
        }
        Button(onClick = { onPri_non()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "Pri-non", color = Color.Black)
        }
        Button(onClick = { onPri_pre()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "Pri-pre", color = Color.Black)
        }
        Button(onClick = { onRobin_Round()
        },
            border = BorderStroke(1.dp, Color.Black),
            shape =  RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF4CE14))
        ) {
            Text(text = "Robin", color = Color.Black)
        }

        Image(painterResource(R.drawable.linux),contentDescription = "")
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton( onClick = { onIncrement() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add")
            }
            //Text(text = "$count")
        }
    }
}
