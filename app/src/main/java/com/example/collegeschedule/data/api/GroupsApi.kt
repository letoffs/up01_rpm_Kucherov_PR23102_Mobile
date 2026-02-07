package com.example.collegeschedule.data.api

import com.example.collegeschedule.data.dto.GroupDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupsApi {
    @GET("api/groups")
    suspend fun getAllGroups(): List<GroupDto>

    @GET("api/groups")
    suspend fun searchGroups(@Query("search") query: String): List<GroupDto>
}