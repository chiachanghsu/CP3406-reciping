package com.example.myapp.data.remote

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ---------- Lightweight list/search model ----------
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

// ---------- Full detail model (has ingredients, measures, instructions) ----------
data class MealDetailResponse(
    @SerializedName("meals") val meals: List<MealDetail>?
)

data class MealDetail(
    @SerializedName("idMeal") val idMeal: String,
    @SerializedName("strMeal") val strMeal: String,
    @SerializedName("strMealThumb") val strMealThumb: String?,
    @SerializedName("strArea") val strArea: String?,
    @SerializedName("strCategory") val strCategory: String?,
    @SerializedName("strInstructions") val strInstructions: String?,

    // ingredients 1..20
    @SerializedName("strIngredient1")  val strIngredient1: String?,  @SerializedName("strMeasure1")  val strMeasure1: String?,
    @SerializedName("strIngredient2")  val strIngredient2: String?,  @SerializedName("strMeasure2")  val strMeasure2: String?,
    @SerializedName("strIngredient3")  val strIngredient3: String?,  @SerializedName("strMeasure3")  val strMeasure3: String?,
    @SerializedName("strIngredient4")  val strIngredient4: String?,  @SerializedName("strMeasure4")  val strMeasure4: String?,
    @SerializedName("strIngredient5")  val strIngredient5: String?,  @SerializedName("strMeasure5")  val strMeasure5: String?,
    @SerializedName("strIngredient6")  val strIngredient6: String?,  @SerializedName("strMeasure6")  val strMeasure6: String?,
    @SerializedName("strIngredient7")  val strIngredient7: String?,  @SerializedName("strMeasure7")  val strMeasure7: String?,
    @SerializedName("strIngredient8")  val strIngredient8: String?,  @SerializedName("strMeasure8")  val strMeasure8: String?,
    @SerializedName("strIngredient9")  val strIngredient9: String?,  @SerializedName("strMeasure9")  val strMeasure9: String?,
    @SerializedName("strIngredient10") val strIngredient10: String?, @SerializedName("strMeasure10") val strMeasure10: String?,
    @SerializedName("strIngredient11") val strIngredient11: String?, @SerializedName("strMeasure11") val strMeasure11: String?,
    @SerializedName("strIngredient12") val strIngredient12: String?, @SerializedName("strMeasure12") val strMeasure12: String?,
    @SerializedName("strIngredient13") val strIngredient13: String?, @SerializedName("strMeasure13") val strMeasure13: String?,
    @SerializedName("strIngredient14") val strIngredient14: String?, @SerializedName("strMeasure14") val strMeasure14: String?,
    @SerializedName("strIngredient15") val strIngredient15: String?, @SerializedName("strMeasure15") val strMeasure15: String?,
    @SerializedName("strIngredient16") val strIngredient16: String?, @SerializedName("strMeasure16") val strMeasure16: String?,
    @SerializedName("strIngredient17") val strIngredient17: String?, @SerializedName("strMeasure17") val strMeasure17: String?,
    @SerializedName("strIngredient18") val strIngredient18: String?, @SerializedName("strMeasure18") val strMeasure18: String?,
    @SerializedName("strIngredient19") val strIngredient19: String?, @SerializedName("strMeasure19") val strMeasure19: String?,
    @SerializedName("strIngredient20") val strIngredient20: String?, @SerializedName("strMeasure20") val strMeasure20: String?
)

// ---------- Retrofit ----------
interface MealsService {
    @GET("search.php")  suspend fun searchMeals(@Query("s") query: String): MealResponse
    @GET("random.php")  suspend fun randomMeal(): MealResponse
    @GET("lookup.php")  suspend fun lookupMeal(@Query("i") id: String): MealDetailResponse
}

object MealsApi {
    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
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
