package kr.co.nottodo.data.model.Home

data class ResponseHomeDaily(
//    val missions: Int,
    val id: Long,
    val title: String,
    val completionStatus: String,
    val situation: String
)