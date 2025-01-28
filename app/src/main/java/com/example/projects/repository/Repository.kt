package com.example.projects.repository

import com.example.projects.model.Exercise
import com.example.projects.model.Program
import com.example.projects.model.ProgramForFirebase
import com.example.projects.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

private const val COLLECTION_USER = "user"
private const val COLLECTION_EXERCISE = "exercise"
private const val COLLECTION_PROGRAM = "program"

class Repository {

    private fun getUserCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USER)
    }

    fun getExerciseCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection((COLLECTION_EXERCISE))
    }

    fun getProgramCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection((COLLECTION_PROGRAM))
    }

    fun createUser(id: String, name: String) {
        val userToAdd = User(id, name)
        getUserCollection().add(userToAdd)
    }

    fun createExercise(name: String, duration: Int, repetition: Int) {
        val exerciseToAdd = Exercise(name, duration, repetition)
        getExerciseCollection().add(exerciseToAdd)
    }

    fun createProgramForFirebase(program: Program) {
        val programForFirebaseToAdd = mapProgramForFirebase(program)
        getProgramCollection().add(programForFirebaseToAdd)
    }

    fun mapProgram(programForFirebase: ProgramForFirebase): Program {
        val program = Program(
            id = programForFirebase.id,
            name = programForFirebase.name,
            rest = programForFirebase.rest,
            exerciseList = programForFirebase.exerciseList.map {
                val line = it
                val splitLine = line.split("-")
                Exercise(name = splitLine[0], splitLine[1].toInt(), splitLine[2].toInt())
            }.toMutableList()
        )
        return program
    }

    private fun mapProgramForFirebase(program: Program): ProgramForFirebase {
        val exerciseListForFirebase: ArrayList<String> = arrayListOf()
        for (exercise in program.exerciseList) {
            exerciseListForFirebase.add(
                createString(
                    exercise.name,
                    exercise.duration,
                    exercise.repetition
                )
            )
        }
        return ProgramForFirebase(
            id = program.id,
            name = program.name,
            rest = program.rest,
            exerciseList = exerciseListForFirebase
        )
    }

    private fun createString(name: String, duration: Int, repetition: Int): String {
        return "$name-$duration-$repetition"
    }
}