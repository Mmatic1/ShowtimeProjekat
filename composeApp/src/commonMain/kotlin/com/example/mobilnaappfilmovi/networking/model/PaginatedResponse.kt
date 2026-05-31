package com.example.mobilnaappfilmovi.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val page:Int,
    val pageSize:Int,
    val totalItems:Int,
    val totalPages:Int,
    val items:List<T>,
){

}