package kr.co.nottodo.presentation.modification.model

data class NotTodoData(
    val mission: String,
    val situation: String,
    val actions: List<String>?,
    val goal: String?,
    val missionId: Long,
)