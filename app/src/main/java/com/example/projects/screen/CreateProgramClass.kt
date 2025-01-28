package com.example.projects.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projects.ProgramList
import com.example.projects.R
import com.example.projects.model.Exercise
import com.example.projects.model.Program
import com.example.projects.ui.theme.ProjectSTheme
import com.example.projects.ui.theme.c3
import com.example.projects.ui.theme.c4
import com.example.projects.viewmodel.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.time.ExperimentalTime

class CreateProgramClass : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "createProgram") {
                        composable("createProgram") { createProgram(navController) }
                        composable("programList") { ProgramList(navController) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun createProgram(
    navController: NavController,
    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val focusManager = LocalFocusManager.current
    var nameText by remember { mutableStateOf(TextFieldValue("")) }
    var restText by remember { mutableStateOf(TextFieldValue("")) }
    Image(
        painter = painterResource(id = R.drawable.white_style_background),
        contentDescription = "fond",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f),
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Define your Program",
            fontSize = 40.sp,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
        )

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedTextField(
                value = nameText,
                modifier = Modifier
                    .weight(1F),
                onValueChange = { newNameText ->
                    nameText = newNameText
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = c4,
                    unfocusedContainerColor = c3,
                    disabledContainerColor = Color.White
                ),
                label = { Text(text = "Name") },
            )
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedTextField(
                value = restText,
                modifier = Modifier.weight(1F),
                onValueChange = { newRestText ->
                    restText = newRestText
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = c4,
                    unfocusedContainerColor = c3,
                    disabledContainerColor = Color.White
                ),
                label = { Text(text = "Rest") },
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "\uD83C\uDF3F  List of Exercises",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 30.sp
            )
        }
        val exerciseList = remember { mutableStateListOf<Exercise>() }
        val newExerciseList = remember { mutableStateListOf<Exercise>() }
        ExerciseList(exerciseList, newExerciseList)
        Row(modifier = Modifier.weight(1f, false)) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp),
            ) {
                Text("Back")
            }
            Button(
                onClick = {
                    val newProgram = Program(5.toString(), nameText.text, newExerciseList, restText.text.toInt())
                    viewModel.createProgramForFirebase(newProgram)
                    navController.popBackStack()
                },
                modifier = Modifier.padding(8.dp),
            ) {
                Text("Confirm")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun ExerciseList(exerciseList: SnapshotStateList<Exercise> = mutableStateListOf(), newExerciseList: SnapshotStateList<Exercise> = mutableStateListOf()) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("exercise")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val documents = querySnapshot.documents
            documents.forEach { document ->
                val data = document.data
                val exerciseItem = Exercise(
                    name = data?.get("name") as String? ?: "",
                    duration = (data?.get("duration") as Long).toInt(),
                    repetition = (data["repetition"] as Long).toInt()
                )
                exerciseList.add(exerciseItem)
            }
        }
    Row {
        Spacer(modifier = Modifier.width(2.dp))
        LazyColumn(
            modifier = Modifier
                .weight(2F)
                .fillMaxWidth()
                .height(350.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            c3,
                            c4
                        )
                    )
                )
                .shadow(1.dp),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            itemsIndexed(exerciseList) { _, exercise ->
                ExerciseCard(
                    exercise.name,
                    exercise.duration,
                    exercise.repetition,
                    onClick = {
                        newExerciseList.add(exercise)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(2.dp))
        LazyColumn(
            modifier = Modifier
                .weight(2F)
                .fillMaxWidth()
                .height(350.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            c3,
                            c4
                        )
                    )
                )
                .shadow(1.dp),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            itemsIndexed(newExerciseList) { _, exercise ->
                ExerciseCard(
                    exercise.name,
                    exercise.duration,
                    exercise.repetition,
                    onClick = {
                        newExerciseList.remove(exercise)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(2.dp))
    }
}
