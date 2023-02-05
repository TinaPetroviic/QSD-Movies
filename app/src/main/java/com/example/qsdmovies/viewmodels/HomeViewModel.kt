package com.example.qsdmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.models.TopRated
import com.example.qsdmovies.network.NetworkCore
import com.example.qsdmovies.util.pager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    val stateFlow = pager(config = PagingConfig(pageSize = 5)) {
        getMovieDataPaged(it.page)
    }.flow.cachedIn(viewModelScope)

    val stateFlowTopRated = pager(config = PagingConfig(pageSize = 5)) {
        getTopRatedPaged(it.page)
    }.flow.cachedIn(viewModelScope)

    val stateFlowTvShows = pager(config = PagingConfig(pageSize = 5)) {
        getTopTvShows(it.page)
    }.flow.cachedIn(viewModelScope)

    val firstPosterLink = MutableStateFlow<String?>(null)

    private suspend fun getMovieDataPaged(page: Int): List<Movie> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getMovieListPaging(page)
        }
        val model = response.body()!!.movies
        firstPosterLink.update { model.first().poster }
        return model
    }

    private suspend fun getTopRatedPaged(page: Int): List<TopRated> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getTopRatedListPaged(page)
        }
        return response.body()!!.topRated
    }

    private suspend fun getTopTvShows(page: Int): List<Movie> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getTvShowsList(page)
        }
        return response.body()!!.movies
    }
}