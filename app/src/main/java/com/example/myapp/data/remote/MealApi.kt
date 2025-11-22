package com.example.myapp.data.remote

import com.example.myapp.data.remote.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php") 
    suspend fun getRandom(): MealsResponse
    
    @GET("search.php")  
    suspend fun search(@Query("s") q: String): MealsResponse
    
    @GET("lookup.php")  
    suspend fun lookup(@Query("i") id: String): MealsResponse
}
