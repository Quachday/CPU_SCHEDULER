package com.example.gantchart

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

interface Destinations {
    val route: String
    val icon : ImageVector
    val title: String
}
object Home: Destinations{
    override val route = "Home"
    override val icon = Icons.Filled.Home
    override val title = "Home"
}
object Select: Destinations{
    override val route = "Select"
    override val icon = Icons.Filled.List
    override val title = "Select"
}
object Schedule: Destinations{
    override val route: String = "Schedule"
    override val icon = Icons.Filled.Check
    override val title = "Schedule"
}