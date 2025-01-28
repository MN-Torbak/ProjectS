package com.example.projects.screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projects.ProgramList
import com.example.projects.ui.theme.ProjectSTheme
import com.example.projects.viewmodel.ViewModel
import kotlin.time.ExperimentalTime

class CreateExerciseClass : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "createExercise") {

                        composable("createExercise") { createExercise(navController) }
                        composable("programList") { ProgramList(navController) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun createExercise(
    navController: NavController,
    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val context = LocalContext.current
            Text(
                text = "Define your Exercise",
                fontSize = 25.sp,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
            )
            var nameText by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                value = nameText,
                onValueChange = { newNameText ->
                    nameText = newNameText
                },
                label = { Text(text = "Name") },
            )
            var durationText by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                value = durationText,
                onValueChange = { newDurationText ->
                    durationText = newDurationText
                },
                label = { Text(text = "Duration") },
            )
            var repetitionText by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                value = repetitionText,
                onValueChange = { newRepetitionText ->
                    repetitionText = newRepetitionText
                },
                label = { Text(text = "Repetition") },
            )
            Row {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Back")
                }
                Button(
                    onClick = {
                        if (nameText.text != "" && durationText.text != "" && repetitionText.text != "") {
                            viewModel.createExercise(
                                nameText.text,
                                durationText.text.toInt(),
                                repetitionText.text.toInt()
                            )
                            navController.navigate("ProgramList")
                            val successToast = Toast.makeText(context, "Your exercise was create successfully", Toast.LENGTH_SHORT)
                            successToast.show()
                        } else {
                            val failToast = Toast.makeText(context, "You must complete each field", Toast.LENGTH_SHORT)
                            failToast.show()
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}