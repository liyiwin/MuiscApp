package com.example.musicapp.data.dao

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
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


    @GET("/v1.1/charts/{playlist_id}")
    @Headers("Content-Type: application/json")
    fun getTracksInChart(
        @Path("playlist_id") playListId:String,
        @Query("offset") offset:String,
        @Query("limit") limit:String,
        @Query("territory")  territory:String,
        @Header("Authorization") token:String
    ): Call<ResponseBody>;

    @GET("/v1.1/featured-playlists/{playlist_id}")
    @Headers("Content-Type: application/json")
    fun getTracksInFeaturedPlayList(
        @Path("playlist_id") playListId:String,
        @Query("offset") offset:String,
        @Query("limit") limit:String,
        @Query("territory")  territory:String,
        @Header("Authorization") token:String
    ): Call<ResponseBody>;

    @GET("/v1.1/new-hits-playlists/{playlist_id}")
    @Headers("Content-Type: application/json")
    fun getTracksInNewHitsPlayList(
        @Path("playlist_id") playListId:String,
        @Query("offset") offset:String,
        @Query("limit") limit:String,
        @Query("territory")  territory:String,
        @Header("Authorization") token:String
    ): Call<ResponseBody>;


    @GET("/v1.1/artists/{artist_id}/top-tracks")
    @Headers("Content-Type: application/json")
    fun getTopTracksOfArtist(
        @Path("artist_id") artistId:String,
        @Query("offset") offset:String,
        @Query("limit") limit:String,
        @Query("territory")  territory:String,
        @Header("Authorization") token:String
    ): Call<ResponseBody>;

    @GET("/v1.1/search")
    @Headers("Content-Type: application/json")
    fun search(
        @Query("q")  keyword:String,
        @Query("type") type:String,
        @Query("territory")  territory:String,
        @Header("Authorization") token:String,
        @Query("offset") offset:String,
        @Query("limit") limit:String,
    ): Call<ResponseBody>;

    @GET("/v1.1/me/daily-recommended-tracks")
    @Headers("Content-Type: application/json")
    fun getDailyRecommendedTracks(
         @Query("territory")  territory:String,
         @Query("offset") offset:String,
         @Query("limit") limit:String,
         @Header("Authorization") token:String,
    ): Call<ResponseBody>

    @GET("/v1.1/me/recommendations-from-listened")
    @Headers("Content-Type: application/json")
    fun getPersonalRecommendedTracks(
        @Query("territory")  territory:String,
        @Query("offset") offset:String,
        @Query("limit") limit:String,
        @Header("Authorization") token:String,
    ): Call<ResponseBody>

    @GET("/v1.1/me")
    @Headers("Content-Type: application/json")
    fun getPersonalInformation(
      @Header("Authorization") token:String,
    ): Call<ResponseBody>
}