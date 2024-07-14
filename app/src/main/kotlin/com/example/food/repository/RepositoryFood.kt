package com.example.food.repository

import com.example.food.api.ApiService
import com.example.food.model.InsertResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryFood @Inject constructor(private val apiService: ApiService) {
    suspend fun insertFood(foodName: String, foodQty: String, imageFile: File): InsertResponse {
        val foodNameBody = foodName.toRequestBody("text/plain".toMediaTypeOrNull())
        val foodQtyBody = foodQty.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("foodimage", imageFile.name, requestFile)
        return apiService.insertFood(foodNameBody, foodQtyBody, body).body() ?: InsertResponse(
            0,
            "Unknown error"
        )
    }
}