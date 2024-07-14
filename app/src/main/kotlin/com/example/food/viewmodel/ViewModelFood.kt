package com.example.food.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.model.InsertResponse
import com.example.food.repository.RepositoryFood
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ViewModelFood @Inject constructor(private val repository: RepositoryFood) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _isNoNetwork = MutableLiveData<Boolean>()
    val isNoNetwork: LiveData<Boolean> get() = _isNoNetwork


    private val _insertResponse = MutableLiveData<InsertResponse>()
    val insertResponse: LiveData<InsertResponse> get() = _insertResponse


    init {
        _isLoading.value = false
        _isNoNetwork.value = false
    }


    fun insertFood(foodName: String, foodQty: String, imageFile: File) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.insertFood(foodName, foodQty, imageFile)
                _insertResponse.postValue(response)
            } catch (e: Exception) {
                _insertResponse.postValue(InsertResponse(0, "Error: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

