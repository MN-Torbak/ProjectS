package com.example.projects

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projects.model.Exercise
import com.example.projects.model.Program
import com.example.projects.model.getArmsProgram
import com.example.projects.model.getProgramFromName
import com.example.projects.model.getProgramTest
import com.example.projects.model.getUpperBodyProgram
import com.example.projects.screen.ExerciseCard
import com.example.projects.screen.ProgramCard
import com.example.projects.screen.TitleRow
import com.example.projects.screen.createExercise
import com.example.projects.screen.createProgram
import com.example.projects.ui.theme.ProjectSTheme
import com.example.projects.viewmodel.ViewModel
import kotlin.time.ExperimentalTime

class ChoseProgram : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    var fabVisible by remember { mutableStateOf(true) }
                    Scaffold(modifier = Modifier.fillMaxSize(),

                        floatingActionButton = {
                            if (fabVisible)  {
                                fabMenu(navController, fabVisible)
                            }
                        }) {
                        NavHost(navController, startDestination = "programList") {

                            composable("programList") { ProgramList(navController) ; fabVisible = true }
                            composable("createExercise") { createExercise(navController) ; fabVisible = false}
                            composable("createProgram") { createProgram(navController) ; fabVisible = false}
                            composable(
                                route = "programExecution/{programName}",
                                arguments = listOf(navArgument("programName") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val programName = backStackEntry.arguments?.getString("programName")
                                val programChosen = programName?.let { getProgramFromName(it) }
                                if (programChosen != null) {
                                    ProgramChosen(programChosen)
                                }
                                fabVisible = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun fabMenu(navController: NavController, visibleBoolean: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        MiniFabItems(Icons.Filled.AddCircleOutline, "Create a Program  "),
        MiniFabItems(Icons.Filled.AddCircleOutline, "Create an Exercise")
    )
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
        ) {
            LazyColumn(Modifier.padding(bottom = 8.dp)) {
                items(items.size) {
                    ItemUi(icon = items[it].icon, title = items[it].title, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        val transition = updateTransition(targetState = expanded, label = "transition")
        val rotation by transition.animateFloat(label = "rotation") {
            if (it) 315f else 0f
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = Color(0xFFFF9800)
        ) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
fun ItemUi(icon: ImageVector, title: String, navController: NavController) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(10.dp))
        HomeScreen(navController, icon, title)
    }
}

data class MiniFabItems(
    val icon: ImageVector,
    val title: String,
)

@OptIn(ExperimentalTime::class)
@Composable
fun ProgramList(
    navController: NavController,
    viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    Image(
        painter = painterResource(id = R.drawable.white_style_background),
        contentDescription = "fond",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f),
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
    )
    val listProgramTemp = viewModel.getAllProgram()
    Column(
        modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleRow("Chose your Program")
        Row {
           LazyColumn {
               itemsIndexed(listProgramTemp) { _, program ->
                   ProgramCard(program = program, navController = navController)
               }
           }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    icon: ImageVector,
    title: String,
) {
    Column {
        if (title == "Create an Exercise") {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("CreateExercise") },
                text = { Text(text = title) },
                icon = { Icon(icon, "Extended floating action button.") }
            )
        } else if (title == "Create a Program  ") {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("CreateProgram") },
                text = { Text(text = title) },
                icon = { Icon(icon, "Extended floating action button.") }
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun Preview() {
    ProjectSTheme {
        ProgramChosen(getProgramTest())
    }
}