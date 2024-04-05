package com.example.projects.model

data class Program (
    val id: String,
    val name: String,
    val exerciseList: List<Exercise>,
    val rest: Int
)