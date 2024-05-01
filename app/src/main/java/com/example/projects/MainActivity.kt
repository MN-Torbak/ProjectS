package com.example.projects

import android.media.MediaPlayer
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.currentCompositionLocalContext
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import com.example.projects.model.Exercise
import com.example.projects.model.Program
import com.example.projects.ui.theme.ProjectSTheme
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.projects.data.getProgram
import com.example.projects.data.getShortProgram
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
                    ProgramChosen(program = getProgram())
                }
            }
            mMediaPlayerClick = MediaPlayer.create(this, R.raw.click_sound)
            mMediaPlayerFanfare = MediaPlayer.create(this, R.raw.fanfare)
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

    Image(
        painter = painterResource(id = R.drawable.test_fond_3),
        contentDescription = LocalContext.current.getString(R.string.background),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f),
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
    )
    ProgramScafold(program = program, viewModel)


}
@OptIn(ExperimentalTime::class)
@Composable
fun ProgramScafold(
    program: Program,
    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
){
    val positionState = viewModel.positionLiveData.observeAsState()
    val titleState = viewModel.exerciseOrRestTextLiveData.observeAsState()

    Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        ProgramNameRow(program = program)
        ExerciseNameRow(program = program, viewModel = viewModel)

        GifImage()
        MainApp(viewModel)

        Row(modifier = Modifier.fillMaxWidth()) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(colorResource(R.color.fond_color_test)),
                modifier = Modifier
                    .size(width = 50.dp, height = 100.dp)
                    .weight(1f)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = numberExerciseInProgram(program, positionState.value!!),
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
                                "Rest" -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.skip_pause_1),
                                        contentDescription = LocalContext.current.getString(R.string.background),
                                        modifier = Modifier
                                            .clickable {
                                                mMediaPlayerClick?.start()
                                                viewModel.defineTitle(program)
                                                viewModel.stop()
                                                for (exercise in program.exerciseList) {
                                                    if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration > 0) {
                                                        viewModel.countdown(exercise.duration)
                                                    } else if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration == 0) {
                                                        viewModel.stop()
                                                    }
                                                }
                                                if (viewModel.exerciseOrRestTextLiveData.value == "Rest") {
                                                    viewModel.startExerciseWithDuration(5)
                                                }
                                            }
                                            .size(width = 200.dp, height = 100.dp),
                                        contentScale = ContentScale.FillBounds,
                                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                            setToSaturation(1f)
                                        })
                                    )
                                }

                                "Start Program" -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.start_session),
                                        contentDescription = LocalContext.current.getString(R.string.background),
                                        modifier = Modifier
                                            .clickable {
                                                mMediaPlayerClick?.start()
                                                viewModel.defineTitle(program)
                                                for (exercise in program.exerciseList) {
                                                    if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration > 0) {
                                                        viewModel.countdown(exercise.duration)
                                                    } else if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration == 0) {

                                                    }
                                                }
                                                if (viewModel.exerciseOrRestTextLiveData.value == "Rest") {
                                                    viewModel.startExerciseWithDuration(5)
                                                }
                                            }
                                            .size(width = 200.dp, height = 100.dp),
                                        contentScale = ContentScale.FillBounds,
                                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                            setToSaturation(1f)
                                        })
                                    )
                                }

                                else -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.next_exercise),
                                        contentDescription = LocalContext.current.getString(R.string.background),
                                        modifier = Modifier
                                            .clickable {
                                                mMediaPlayerClick?.start()
                                                viewModel.defineTitle(program)
                                                viewModel.stop()
                                                for (exercise in program.exerciseList) {
                                                    Log.d(
                                                        "toto",
                                                        "${exercise.duration}, ${exercise.name}"
                                                    )
                                                    if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration > 0) {
                                                        viewModel.countdown(exercise.duration)
                                                    } else if ("Exercise: " + exercise.name == viewModel.exerciseOrRestTextLiveData.value && exercise.duration == 0) {
                                                        viewModel.stop()
                                                    }
                                                }
                                                if (viewModel.exerciseOrRestTextLiveData.value == "Rest") {
                                                    viewModel.startExerciseWithDuration(5)
                                                }
                                            }
                                            .size(width = 200.dp, height = 100.dp),
                                        contentScale = ContentScale.FillBounds,
                                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                            setToSaturation(1f)
                                        })
                                    )
                                }
                            }
                        } else {
                            var hasButtonBeenClicked by remember {
                                mutableStateOf(false)
                            }
                            Image(
                                painter = painterResource(id = R.drawable.finish),
                                contentDescription = LocalContext.current.getString(R.string.background),
                                modifier = Modifier
                                    .clickable(enabled = !hasButtonBeenClicked) {
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

@OptIn(ExperimentalTime::class)
@Composable
fun ProgramNameRow(
    program: Program,
){
    Row(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.fond_color_test)),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
                .weight(1f)
        ) {
            Text(
                text = LocalContext.current.getString(R.string.program, program.name),
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
fun ExerciseNameRow(program: Program,
                    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),){
    val titleState = viewModel.exerciseOrRestTextLiveData.observeAsState()
    Row(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.fond_color_test)),
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

fun numberExerciseInProgram(program: Program, position: Int): String {
    val exerciseID = position + 1
    val numberOfExerciseInProgram = program.exerciseList.size
    return "$exerciseID/$numberOfExerciseInProgram"
}
@OptIn(ExperimentalTime::class)
@Composable
fun MainApp(viewModel: ViewModel) {
    MainApp(
        isPlaying = viewModel.isPlaying,
        seconds = viewModel.seconds,
        minutes = viewModel.minutes,
        onStart = { viewModel.start() },
        onPause = { viewModel.pause() },
        onStop = { viewModel.stop() }
    )
}

@Composable
private fun MainApp(
    isPlaying: Boolean,
    seconds: String,
    minutes: String,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},

    ) {
    Row(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.fond_color_test)),
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

@OptIn(ExperimentalTime::class)
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    /*val context = LocalContext.current
    val gif = viewModel.gifLiveData.observeAsState()
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

        Image(
            painter = painterResource(id = R.drawable.gif_waiting_img),
            contentDescription = "fond",
            modifier = Modifier.size(width = 300.dp, height = 150.dp),
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                setToSaturation(1f)
            })
        )


    // Pour mettre un GIF plutôt qu'une image

    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = getGifReferenceFromString(gif.value!!)).apply(block = {
                size(coil.size.Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.size(width = 400.dp, height = 200.dp),
    )*/

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

fun getGifReferenceFromString(gif: String): Int {
    //TODO: when avec une enum class faite dans le viewmodel
    //return R.drawable.push_up_gif
    return R.drawable.gif_waiting_img
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjectSTheme {
        ProgramChosen(getShortProgram())
    }
}