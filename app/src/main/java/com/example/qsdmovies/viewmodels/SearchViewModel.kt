package com.example.qsdmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.network.NetworkCore
import com.example.qsdmovies.util.pager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {

    val stateFlow = pager(config = PagingConfig(pageSize = 5)) {
        getMovieDataPaged(it.page)
    }.flow.cachedIn(viewModelScope)

    val stateFlowTvShows = pager(config = PagingConfig(pageSize = 5)) {
        getTopTvShows(it.page)
    }.flow.cachedIn(viewModelScope)

    private suspend fun getMovieDataPaged(page: Int): List<Movie> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getMovieListPaging(page)
        }
        return response.body()!!.movies
    }


    private suspend fun getTopTvShows(page: Int): List<Movie> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getTvShowsList(page)
        }
        return response.body()!!.movies
    }

    private suspend fun searchInternal(page: Int, query: String): List<Movie> {
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkCore.moviesAPI.search(page, query)
            }
            response.body()!!.movies.filter { it.media_type == "movie" }
        } catch (e: Exception) {
            listOf()
        }
    }

    private suspend fun searchShowsInternal(page: Int, query: String): List<Movie> {
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkCore.moviesAPI.search(page, query)
            }
            response.body()!!.movies.filter { it.media_type == "tv" }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun search(query: String): Flow<PagingData<Movie>> {
        return pager(config = PagingConfig(pageSize = 5)) {
            searchInternal(it.page, query)
        }.flow.cachedIn(viewModelScope)
    }

    fun searchShows(query: String): Flow<PagingData<Movie>> {
        return pager(config = PagingConfig(pageSize = 5)) {
            searchShowsInternal(it.page, query)
        }.flow.cachedIn(viewModelScope)
    }
}