package com.example.food.api

import com.example.food.model.InsertResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("retrofit/api.php")
    suspend fun insertFood(
        @Part("foodname") foodName: RequestBody,
        @Part("foodqty") foodQty: RequestBody,
        @Part foodImage: MultipartBody.Part
    ): Response<InsertResponse>
}
