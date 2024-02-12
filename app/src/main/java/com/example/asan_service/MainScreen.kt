package com.example.asan_service

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController : NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var SoundisOn by remember { mutableStateOf(false) }
    var VibrateisOn by remember { mutableStateOf(false) }
    var PopUpisOn by remember { mutableStateOf(false) }

    NavigationDrawer(
        drawerContent = {
            Row(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "설정",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "소리 설정",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(150.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = if (SoundisOn) Color.White else Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            SoundisOn = !SoundisOn
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ON",
                        color = if (SoundisOn) Color.Black else Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = if (SoundisOn) Color.Gray else Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            SoundisOn = !SoundisOn
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "OFF",
                        color = if (SoundisOn) Color.White else Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "진동설정",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(150.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = if (VibrateisOn) Color.White else Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            VibrateisOn = !VibrateisOn
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "ON",
                        color = if (VibrateisOn) Color.Black else Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = if (VibrateisOn) Color.Gray else Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            VibrateisOn = !VibrateisOn
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "OFF",
                        color = if (VibrateisOn) Color.White else Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "팝업 설정",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(150.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = if (PopUpisOn) Color.White else Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            PopUpisOn = !PopUpisOn
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ON",
                        color = if (PopUpisOn) Color.Black else Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = if (PopUpisOn) Color.Gray else Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            PopUpisOn = !PopUpisOn
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "OFF",
                        color = if (PopUpisOn) Color.White else Color.Black
                    )
                }
            }

            Row(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "데이터",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(Color.Blue)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "통계량",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("StatisticScreen")
                        }
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = {
                        Text(
                            "logo"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    //
                    // 이미지는 url 형태로 전달받게 하는 것이 좋을 것 같다.
                    //

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { navController.navigate("AlarmScreen") },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(text = "발생 알람")
                        }
                        Button(
                            onClick = { navController.navigate("ConnectScreen") },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(text = "연결 상태")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxWithTextAndButtons(text: String) {
    var isOn by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(50.dp)
            .background(Color.Blue)
            .clickable {
                isOn = !isOn
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.width(150.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = if (isOn) Color.White else Color.Gray, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        isOn = !isOn
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = if (isOn) Color.Gray else Color.White, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        isOn = !isOn
                    }
            )
        }
    }
}