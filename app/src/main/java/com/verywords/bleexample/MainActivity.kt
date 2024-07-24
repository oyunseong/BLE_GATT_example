package com.verywords.bleexample

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.verywords.bleexample.ble.server.GATTServerSample
import com.verywords.bleexample.ui.BLEScanIntentSample
import com.verywords.bleexample.ui.ConnectGATTSample
import com.verywords.bleexample.ui.theme.BLEExampleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    private val menus: List<MenuItem> = listOf(
        MenuItem("connect", R.drawable.ic_launcher_foreground, true),
        MenuItem("Server", R.drawable.ic_launcher_foreground),
        MenuItem("BLEScanIntent", R.drawable.ic_launcher_foreground),
    )

    private val _navigationMenus: MutableStateFlow<List<MenuItem>> = MutableStateFlow(menus)
    val navigationMenus = _navigationMenus.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationMenus = navigationMenus.collectAsState()
            BLEExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        ) {
                            ScreenContent(navigationMenus.value)
                        }
                        BottomNavigation(
                            menus = navigationMenus.value,
                            onClick = {
                                onClickBottomNavigationMenu(menuItem = it)
                            }
                        )
                    }

                }
            }
        }
    }

    private fun onClickBottomNavigationMenu(menuItem: MenuItem) {
        _navigationMenus.update {
            navigationMenus.value.map {
                it.copy(isSelected = it == menuItem)
            }
        }
    }
}

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenContent(menus: List<MenuItem>) {
    menus.find { it.isSelected }?.let { selectedMenu ->
        when (selectedMenu.title) {
            "connect" -> ConnectGATTSample()
            "Server" -> GATTServerSample()
            "BLEScanIntent" -> BLEScanIntentSample()
            else -> Text("Not implemented")
        }
    }
}

data class MenuItem(
    val title: String,
    val icon: Int,
    val isSelected: Boolean = false
)

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    menus: List<MenuItem> = emptyList(),
    onClick: (MenuItem) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            menus.forEach { menuItem ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(if (menuItem.isSelected) Color.Blue else Color.Gray),
                    )
                    Text(
                        text = menuItem.title,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onClick(menuItem) },
                    )
                }

            }
        }
    }
}