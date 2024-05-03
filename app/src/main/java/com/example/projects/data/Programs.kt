package com.example.projects.data

import com.example.projects.model.Exercise
import com.example.projects.model.Program

val exoTestOne = Exercise("01", "Jumping Jack", 30)
val exoTestTwo = Exercise("02", "10 Pompes", 0)
val exoTestThree = Exercise("03", "10 Pompes bras écartés", 0)
val exoTestFour = Exercise("04", "Elévation frontale des bras", 30)
val exoTestFive = Exercise("05", "Elévation latérale des bras", 30)
val exoTestSix = Exercise("06", "Squat", 30)
val listExoTest: MutableList<Exercise> = mutableListOf()

fun getProgram():Program{
    listExoTest.add(exoTestOne)
    listExoTest.add(exoTestTwo)
    listExoTest.add(exoTestThree)
    listExoTest.add(exoTestFour)
    listExoTest.add(exoTestFive)
    listExoTest.add(exoTestSix)
    listExoTest.add(exoTestTwo)
    listExoTest.add(exoTestThree)
    listExoTest.add(exoTestFour)
    listExoTest.add(exoTestFive)
    listExoTest.add(exoTestSix)
    return Program("001", "Corps complet", listExoTest, 30)
}

fun getShortProgram():Program{
    listExoTest.add(exoTestOne)
    listExoTest.add(exoTestTwo)
    listExoTest.add(exoTestThree)
    listExoTest.add(exoTestFour)
    listExoTest.add(exoTestFive)
    return Program("002", "Corps complet test", listExoTest, 30)
}