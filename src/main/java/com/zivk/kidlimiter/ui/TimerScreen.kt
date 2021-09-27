package com.zivk.kidlimiter.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivk.kidlimiter.R
import com.zivk.kidlimiter.viewModel.CountTimeViewModel

@ExperimentalAnimationApi
@Composable
fun TimerApp(countTimeViewModel: CountTimeViewModel, modifier: Modifier = Modifier) {

    val secs = countTimeViewModel.seconds.collectAsState()
    val minutes = countTimeViewModel.minutes.collectAsState()
    val hours = countTimeViewModel.hours.collectAsState()
    val resumed = countTimeViewModel.isRunning.collectAsState()

    val progress = countTimeViewModel.progress.collectAsState(1f)
    val timeShow = countTimeViewModel.time.collectAsState(initial = "00:00:00")

    Surface(color = MaterialTheme.colors.background) {
        val typography = MaterialTheme.typography

        Image(
            painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding()) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Let's start the countdown!",
                    fontSize = 24.sp,
                    style = typography.h4,
                    color = Color.White,
                    fontStyle = FontStyle.Italic
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(Modifier.padding(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color.Yellow,
                        modifier = Modifier.size(250.dp),
                        progress = progress.value,
                        strokeWidth = 12.dp
                    )
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        ReusableHeaderText(
                            text = timeShow.value,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.dp, end = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = CountTimeViewModel.Companion.TimeUnit.HOUR.name,
                    fontSize = 20.sp,
                    color = Color.White,
                    style = typography.caption
                )
                Text(
                    text = CountTimeViewModel.Companion.TimeUnit.MIN.name,
                    fontSize = 20.sp,
                    color = Color.White,
                    style = typography.caption
                )
                Text(
                    text = CountTimeViewModel.Companion.TimeUnit.SEC.name,
                    fontSize = 20.sp,
                    color = Color.White,
                    style = typography.caption
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimerComponent(
                    value = hours.value,
                    enabled = !resumed.value
                ) {
                    countTimeViewModel.modifyTime(CountTimeViewModel.Companion.TimeUnit.HOUR, it)
                }

                Text(text = " : ", fontSize = 36.sp)

                TimerComponent(
                    value = minutes.value,
                    enabled = !resumed.value
                ) {
                    countTimeViewModel.modifyTime(CountTimeViewModel.Companion.TimeUnit.MIN, it)
                }

                Text(text = " : ", fontSize = 36.sp)

                TimerComponent(
                    value = secs.value,
                    enabled = !resumed.value
                ) {
                    countTimeViewModel.modifyTime(CountTimeViewModel.Companion.TimeUnit.SEC, it)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        if (!(secs.value == 0 && minutes.value == 0 && hours.value == 0)) {
                            if (!resumed.value) {
                                countTimeViewModel.startCountDown()
                            } else {
                                countTimeViewModel.cancelTimer()
                            }
                        }
                    },
                    modifier = modifier
                        .padding(16.dp)
                        .height(48.dp)
                        .widthIn(min = 48.dp),
                    backgroundColor = Color.Yellow,
                    contentColor = MaterialTheme.colors.onPrimary
                ) {
                    AnimatingFabContent(
                        icon = {
                            if (!resumed.value)
                                Icon(
                                    imageVector = Icons.Outlined.PlayArrow,
                                    contentDescription = null
                                ) else
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null
                                )
                        },
                        text = {
                            if (!resumed.value)
                                Text(
                                    color = Color.DarkGray,
                                    text = "Count Down!"
                                ) else
                                Text(
                                    color = Color.DarkGray,
                                    text = "Pause"
                                )
                        },
                        extended = true

                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerComponent(
    value: Int?,
    enabled: Boolean,
    onClick: (CountTimeViewModel.Companion.TimeOperator) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MaterialTheme.typography

        Spacer(modifier = Modifier.height(8.dp))

        OperatorButton(
            timeOperator = CountTimeViewModel.Companion.TimeOperator.INCREASE,
            isEnabled = enabled,
            onClick = onClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = String.format("%02d", value ?: 0),
            fontSize = 32.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        OperatorButton(
            timeOperator = CountTimeViewModel.Companion.TimeOperator.DECREASE,
            isEnabled = enabled,
            onClick = onClick
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun OperatorButton(
    isEnabled: Boolean,
    timeOperator: CountTimeViewModel.Companion.TimeOperator,
    onClick: (CountTimeViewModel.Companion.TimeOperator) -> Unit
) {
    AnimatedVisibility(
        visible = isEnabled
    ) {
        Button(
            onClick = { onClick.invoke(timeOperator) },
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray,
                //    backgroundColor = MaterialTheme.colors.background,
                disabledBackgroundColor = MaterialTheme.colors.background
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {

            when (timeOperator) {
                CountTimeViewModel.Companion.TimeOperator.INCREASE -> Icon(
                    Icons.Outlined.KeyboardArrowUp,
                    null,
                    Modifier.size(24.dp)
                )
                CountTimeViewModel.Companion.TimeOperator.DECREASE -> Icon(
                    Icons.Outlined.ArrowDropDown,
                    null,
                    Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ReusableHeaderText(
    text: String,
    color: Color
) {
    Text(
        text = text,
        fontSize= 42.sp,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1,
        color = color
    )
}


