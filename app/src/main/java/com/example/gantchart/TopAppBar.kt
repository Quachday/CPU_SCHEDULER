package com.example.gantchart


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopAppBar(drawerState: DrawerState, scope: CoroutineScope) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        } }) {
            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "CPU SCHEDULER",
                modifier = Modifier.size(20.dp) )
        }
        Image(painter = painterResource(id = R.drawable.linux)
            , contentDescription = "linux",
            modifier = Modifier.fillMaxWidth().size(24.dp)
                .padding(horizontal = 20.dp),
            alignment = Alignment.Center)

    }
}