package com.example.projects.model

data class Program(
    val id: String,
    val name: String,
    val exerciseList: MutableList<Exercise>,
    val rest: Int
)

val exoTestOne = Exercise("01", "Jumping Jack", 30)
val exoTestTwo = Exercise("02", "10 Push-Up", 0)
val exoTestThree = Exercise("03", "10 Wide Hands Push-Up", 0)
val exoTestFour = Exercise("04", "Frontal Arm Raise", 30)
val exoTestFive = Exercise("05", "Lateral Arm Raise", 30)
val exoTestSix = Exercise("06", "Squat", 30)


fun getProgramTest(): Program {
    val listExoTest: MutableList<Exercise> = mutableListOf()
    listExoTest.add(exoTestOne)
    listExoTest.add(exoTestTwo)
    listExoTest.add(exoTestThree)
    listExoTest.add(exoTestFour)
    listExoTest.add(exoTestFive)
    listExoTest.add(exoTestSix)
    return Program("001", "Full Body", listExoTest, 30)
}

