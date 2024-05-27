package com.example.projects

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projects.model.Program
import com.example.projects.model.getProgramTest
import com.example.projects.ui.theme.ProjectSTheme
import com.example.projects.viewmodel.ViewModel
import kotlin.time.ExperimentalTime

var mMediaPlayerClick: MediaPlayer? = null
var mMediaPlayerFanfare: MediaPlayer? = null

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProgramChosen(getProgramTest())
                }
            }
            mMediaPlayerClick = MediaPlayer.create(this, R.raw.click_sound)
            mMediaPlayerFanfare = MediaPlayer.create(this, R.raw.fanfare)
        }
    }
}

@Composable
fun TitleRow(program: Program) {
    Row(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.card_background)),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
                .weight(1f)
        ) {
            Text(
                text = "Program: " + program.name,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .fillMaxHeight(1.0f)
                    .wrapContentHeight()
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ProgramChosen(
    program: Program,
    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    viewModel.initPosition()
    viewModel.initTitle()
    val positionState = viewModel.positionLiveData.observeAsState()
    val titleState = viewModel.exerciseOrRestTextLiveData.observeAsState()
    Image(
        painter = painterResource(id = R.drawable.street_background),
        contentDescription = "fond",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f),
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
    )

    Column(
        modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleRow(program)
        ExerciseRow(titleState)
        GifImage()
        TimerRow(viewModel)

        Row(modifier = Modifier.fillMaxWidth()) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(colorResource(R.color.card_background)),
                modifier = Modifier
                    .size(width = 50.dp, height = 100.dp)
                    .weight(1f)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = exercisePositionInProgram(program, positionState.value!!),
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.50f)
                            .fillMaxHeight(1f)
                            .wrapContentHeight()
                    )
                    Box {
                        if (positionState.value != program.exerciseList.size - 1) {
                            when (titleState.value) {
                                "Start Program" -> {
                                    ImageButton(
                                        viewModel,
                                        program,
                                        R.drawable.start_session_image_button,
                                        firstStart = true
                                    )
                                }

                                "Rest" -> {
                                    ImageButton(
                                        viewModel,
                                        program,
                                        R.drawable.skip_pause_image_button
                                    )
                                }

                                else -> {
                                    ImageButton(
                                        viewModel,
                                        program,
                                        R.drawable.next_exercise_image_button
                                    )
                                }
                            }
                        } else {
                            var hasButtonBeenClicked by remember {
                                mutableStateOf(false)
                            }
                            Image(
                                painter = painterResource(id = R.drawable.finish_image_button),
                                contentDescription = "fond",
                                modifier = Modifier
                                    .clickable(enabled = !hasButtonBeenClicked) {
                                        viewModel.stop()
                                        hasButtonBeenClicked = true
                                        mMediaPlayerFanfare?.start()
                                        viewModel.exerciseOrRestTextLiveData.value =
                                            "Program Completed"
                                    }
                                    .size(width = 200.dp, height = 100.dp),
                                contentScale = ContentScale.FillBounds,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    setToSaturation(1f)
                                })
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun ExerciseRow(titleState: State<String?>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.card_background)),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
                .weight(1f)
        ) {
            Text(
                text = titleState.value!!,
                lineHeight = 35.sp,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .fillMaxHeight(1.0f)
                    .wrapContentHeight()
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
fun buttonNext(viewModel: ViewModel, program: Program, firstStart: Boolean = false) {
    mMediaPlayerClick?.start()
    viewModel.defineTitle(program)
    if (!firstStart) {
        viewModel.stop()
    }
    for (exercise in program.exerciseList) {
        if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration > 0) {
            viewModel.countdown(exercise.duration)
            break
        } else if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration == 0) {
            viewModel.stop()
            break
        }
    }
    if (viewModel.exerciseOrRestTextLiveData.value == "Rest") {
        viewModel.startExerciseWithDuration(5)
    }
}

fun exercisePositionInProgram(program: Program, position: Int): String {
    val exerciseID = position + 1
    val numberOfExerciseInProgram = program.exerciseList.size
    return "$exerciseID/$numberOfExerciseInProgram"
}

@OptIn(ExperimentalTime::class)
@Composable
fun ImageButton(viewModel: ViewModel, program: Program, id: Int, firstStart: Boolean = false) {
    Image(
        painter = painterResource(id),
        contentDescription = "fond",
        modifier = Modifier
            .clickable {
                buttonNext(viewModel, program, firstStart)
            }
            .size(width = 200.dp, height = 100.dp),
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
            setToSaturation(1f)
        })
    )
}

@OptIn(ExperimentalTime::class)
@Composable
private fun TimerRow(
    viewModel: ViewModel
) {
    val seconds = viewModel.seconds
    val minutes = viewModel.minutes
    Row(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.card_background)),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
                .weight(1F)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineSmall) {
                        Text(text = minutes, fontSize = 30.sp)
                        Text(text = ":", fontSize = 30.sp)
                        Text(text = seconds, fontSize = 30.sp)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GifImage() {
    Image(
        painter = painterResource(id = R.drawable.gif_waiting_img),
        contentDescription = "fond",
        modifier = Modifier.size(width = 300.dp, height = 150.dp),
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
            setToSaturation(1f)
        })
    )
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjectSTheme {
        ProgramChosen(getProgramTest())
    }
}