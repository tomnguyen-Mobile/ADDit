package com.mdi2.addit.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class OmdbMovie(
    val Title: String?,
    val Poster: String?,
    val Year: String?,
    val Type: String?,
    val Plot: String?,
    val Response: String?,
    val Error: String?
)
data class OmdbSearchItem(
    val Title: String?,
    val Year: String?,
    val Poster: String?,
    val imdbID: String?
)
data class OmdbSearchResponse(
    val Search: List<OmdbSearchItem>?,
    val totalResults: String?,
    val Response: String?,
    val Error: String?
)
interface OmdbApi {

    @GET("/")
    suspend fun getMovie(
        @Query("t") title: String,
        @Query("apikey") apiKey: String
    ): OmdbMovie

    @GET("/")
    suspend fun searchMovies(
        @Query("s") search: String,
        @Query("apikey") apiKey: String
    ): OmdbSearchResponse

    @GET("/")
    suspend fun getMovieById(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): OmdbMovie
}
//http://www.omdbapi.com/?i=tt3896198&apikey=475ed561
object RetrofitInstance {

    private const val BASE_URL = "https://www.omdbapi.com/"

    val api: OmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApi::class.java)
    }
}