package com.example.fetchhiringapp.Apis

import com.example.fetchhiringapp.Data.FetchDataItem
import retrofit2.Response
import retrofit2.http.GET


interface IFetchApi {
    @GET("hiring.json")
    suspend fun getItems(): Response<List<FetchDataItem>>
}