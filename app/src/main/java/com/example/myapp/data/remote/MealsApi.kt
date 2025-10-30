package com.example.myapp.data.remote

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ==== Models (TheMealDB) ====
data class MealResponse(
    @SerializedName("meals") val meals: List<Meal>?
)

data class Meal(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val strMeal: String,
    @SerializedName("strMealThumb") val strMealThumb: String?,
    @SerializedName("strArea") val strArea: String?,
    @SerializedName("strCategory") val strCategory: String?
)

// ==== Retrofit service ====
interface MealsService {
    // Search meals by text, e.g. s=chicken
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse

    // One random meal (unused but handy)
    @GET("random.php")
    suspend fun randomMeal(): MealResponse
}

// ==== Singleton API client ====
object MealsApi {
    private val client by lazy {
        val log = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        OkHttpClient.Builder()
            .addInterceptor(log)
            .build()
    }

    val service: MealsService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MealsService::class.java)
    }
}
