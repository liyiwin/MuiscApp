package com.example.musicapp.data.dao

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetRequestInterface {

    @GET("/v1.1/charts")
    @Headers("Content-Type: application/json")
    fun  getCharts(@Query("territory")  territory:String, @Header("Authorization") token:String): Call<ResponseBody>;


    @GET("/v1.1/featured-playlists")
    @Headers("Content-Type: application/json")
    fun   getFeaturedPlayLists(@Query("territory")  territory:String, @Header("Authorization") token:String): Call<ResponseBody>;


    @GET("/v1.1/new-hits-playlists")
    @Headers("Content-Type: application/json")
    fun getNewHitsPlayLists(@Query("territory")  territory:String, @Header("Authorization") token:String): Call<ResponseBody>;


}