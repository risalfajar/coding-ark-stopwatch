package ark.coding.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ark.coding.stopwatch.ui.MainViewModel
import ark.coding.stopwatch.ui.theme.StopwatchTheme
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchTheme {
                Surface {
                    MainApp(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun MainApp(viewModel: MainViewModel) {
    MainApp(
        isPlaying = viewModel.isPlaying,
        seconds = viewModel.seconds,
        minutes = viewModel.minutes,
        hours = viewModel.hours,
        onStart = { viewModel.start() },
        onPause = { viewModel.pause() },
        onStop = { viewModel.stop() }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainApp(
    isPlaying: Boolean,
    seconds: String,
    minutes: String,
    hours: String,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
) {
    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row {
                val numberTransitionSpec: AnimatedContentScope<String>.() -> ContentTransform = {
                    slideInVertically(
                        initialOffsetY = { it }
                    ) + fadeIn() with slideOutVertically(
                        targetOffsetY = { -it }
                    ) + fadeOut() using SizeTransform(
                        false
                    )
                }

                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.h3) {
                    AnimatedContent(targetState = hours, transitionSpec = numberTransitionSpec) {
                        Text(text = it)
                    }
                    Text(text = ":")
                    AnimatedContent(targetState = minutes, transitionSpec = numberTransitionSpec) {
                        Text(text = it)
                    }
                    Text(text = ":")
                    AnimatedContent(targetState = seconds, transitionSpec = numberTransitionSpec) {
                        Text(text = it)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.background(Color.LightGray, RoundedCornerShape(50))
            ) {
                AnimatedContent(targetState = isPlaying) {
                    if (it)
                        IconButton(onClick = onPause) {
                            Icon(imageVector = Icons.Filled.Pause, contentDescription = "")
                        }
                    else
                        IconButton(onClick = onStart) {
                            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")
                        }
                }
                IconButton(onClick = onStop) {
                    Icon(imageVector = Icons.Filled.Stop, contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StopwatchTheme {
        MainApp(
            false,
            "00",
            "00",
            "00"
        )
    }
}