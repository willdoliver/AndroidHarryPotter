package com.example.harrypotterapi

import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {
    @GET("character/{characterId}")
    suspend fun getCharById(@Path("characterId") characterId: String): List<Character>

    @GET("characters/staff")
    suspend fun getStaffs(): List<Character>

    @GET("characters/house/{houseName}")
    suspend fun getStudentsByHouse(@Path("houseName") houseName: String): List<Character>

    @GET("characters")
    suspend fun getAllCharacters(): List<Character>
}