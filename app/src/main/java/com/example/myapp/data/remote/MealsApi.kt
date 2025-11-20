package com.example.myapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// -------- Retrofit service --------
interface MealsApiService {
    // search by name, e.g. "Arrabiata"
    @GET("search.php")
    suspend fun searchByName(@Query("s") name: String): SearchResponse

    // details by id, e.g. "52772"
    @GET("lookup.php")
    suspend fun detail(@Query("i") id: String): DetailResponse

    // one random recipe
    @GET("random.php")
    suspend fun random(): DetailResponse
}

object MealsApi {
    val service: MealsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealsApiService::class.java)
    }
}

// -------- DTOs --------
data class SearchResponse(
    val meals: List<MealSummary>?
)

data class DetailResponse(
    val meals: List<MealDetail>?
)

/** Lightweight item used in lists / search results */
data class MealSummary(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumb: String,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strArea") val area: String?
)

/** Full detail payload (includes up to 20 ingredients + measures) */
data class MealDetail(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumb: String,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strArea") val area: String?,
    @SerializedName("strInstructions") val instructions: String?,

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
    @SerializedName("strIngredient20") val strIngredient20: String?, @SerializedName("strMeasure20") val strMeasure20: String?,
)

/** Convenience: collect non-blank ingredient + measure pairs */
fun MealDetail.ingredients(): List<Pair<String, String>> {
    val pairs = listOf(
        strIngredient1  to strMeasure1,  strIngredient2  to strMeasure2,
        strIngredient3  to strMeasure3,  strIngredient4  to strMeasure4,
        strIngredient5  to strMeasure5,  strIngredient6  to strMeasure6,
        strIngredient7  to strMeasure7,  strIngredient8  to strMeasure8,
        strIngredient9  to strMeasure9,  strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11, strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13, strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15, strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17, strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19, strIngredient20 to strMeasure20,
    )
    return pairs.mapNotNull { (ing, mea) ->
        val i = ing?.trim().orEmpty()
        if (i.isBlank()) null else i to (mea?.trim().orEmpty())
    }
}
