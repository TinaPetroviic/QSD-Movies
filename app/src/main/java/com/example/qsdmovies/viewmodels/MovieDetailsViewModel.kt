package com.example.qsdmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qsdmovies.models.Cast
import com.example.qsdmovies.network.NetworkCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsViewModel : ViewModel() {

    private val _casts = MutableStateFlow<List<Cast>?>(null)
    val casts = _casts.asStateFlow()

    fun getCasts(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkCore.moviesAPI.getCredits(movieId)
                }

                _casts.update { response.body()!!.casts }
            } catch (e: Exception) {
                println("Movies")
            }
        }
    }
}