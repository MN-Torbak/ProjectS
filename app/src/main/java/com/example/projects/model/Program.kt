package com.example.projects.model

data class Program(
    var id: String,
    val name: String,
    val exerciseList: MutableList<Exercise>,
    val rest: Int
)

data class ProgramForFirebase(
    var id: String,
    val name: String,
    val rest: Int,
    val exerciseList: ArrayList<String>,
)

//cardio
val jumpingJack = Exercise("Jumping Jack", 30, 0)

//upper body
val pushUp = Exercise("Push-Up", 0, 10)
val wideHandsPushUp = Exercise("Wide Hands Push-Up", 0, 10)
val raisedPushUp = Exercise("Raised Push-Up", 0, 10)
val diamondPushUp = Exercise("Diamond Push-Up", 0, 10)

//arms
val frontalArmRaise = Exercise("Frontal Arm Raise", 30, 0)
val lateralArmRaise = Exercise("Lateral Arm Raise", 30, 0)
val curlLeftArm = Exercise("Curl Left Arm", 0, 10)
val curlRightArm = Exercise("Curl Right Arm", 0,10)

//legs
val squat = Exercise("Squat", 30,0)
val lunge = Exercise("Lunge", 30,0)
val sitToStand = Exercise("Sit to Stand", 30,0)

fun getExerciseListTest(): ArrayList<String> {
    val listExoTestForDropDownMenu: ArrayList<String> = arrayListOf()
    listExoTestForDropDownMenu.add(jumpingJack.name)
    listExoTestForDropDownMenu.add(pushUp.name)
    listExoTestForDropDownMenu.add(wideHandsPushUp.name)
    listExoTestForDropDownMenu.add(frontalArmRaise.name)
    return listExoTestForDropDownMenu
}

fun getProgramTest(): Program {
    val listExoTest: MutableList<Exercise> = mutableListOf()
    listExoTest.add(jumpingJack)
    listExoTest.add(pushUp)
    listExoTest.add(wideHandsPushUp)
    listExoTest.add(frontalArmRaise)
    listExoTest.add(lateralArmRaise)
    listExoTest.add(squat)
    return Program("001", "Full Body", listExoTest, 30)
}

fun getUpperBodyProgram(): Program {
    val upperBodyList: MutableList<Exercise> = mutableListOf()
    upperBodyList.add(pushUp)
    upperBodyList.add(wideHandsPushUp)
    upperBodyList.add(raisedPushUp)
    upperBodyList.add(diamondPushUp)
    return Program("002", "Upper Body", upperBodyList, 30)
}

fun getArmsProgram(): Program {
    val armsList: MutableList<Exercise> = mutableListOf()
    armsList.add(frontalArmRaise)
    armsList.add(lateralArmRaise)
    armsList.add(curlLeftArm)
    armsList.add(curlRightArm)
    return Program("003", "Arms", armsList, 30)
}

fun getProgramFromName(name: String): Program {
    return when (name) {
        "Upper Body" ->
            getUpperBodyProgram()
        "Arms" ->
            getArmsProgram()
        else ->
            getProgramTest()
    }
}



