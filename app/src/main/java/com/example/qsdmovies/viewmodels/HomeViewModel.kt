package com.example.qsdmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.models.TopRated
import com.example.qsdmovies.network.NetworkCore
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.pager
import com.example.qsdmovies.util.toModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    val stateFlow = MutableStateFlow<List<Movie>>(listOf())

    val stateFlowTopRated = pager(config = PagingConfig(pageSize = 5)) {
        getTopRatedPaged(it.page)
    }.flow.cachedIn(viewModelScope)

    val stateFlowTvShows = pager(config = PagingConfig(pageSize = 5)) {
        getTopTvShows(it.page)
    }.flow.cachedIn(viewModelScope)

    val stateFlowMovies = pager(config = PagingConfig(pageSize = 5)) {
        getMovieDataPaged(it.page)
    }.flow.cachedIn(viewModelScope)

    val firstPosterLink = MutableStateFlow<String?>(null)

    private suspend fun getMovieDataPaged(page: Int): List<Movie> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getMovieListPaging(page)
        }
        val favoriteIds = getFavorites().map { it.id.toInt() }
        val model = response.body()!!.movies
        val modifiedList = mutableListOf<Movie>()
        modifiedList.addAll(model)
        modifiedList.forEach {
            if (favoriteIds.contains(it.id.toInt())) {
                it.favorite = true
            }
        }
        firstPosterLink.update { model.first().poster_path }
        return modifiedList
    }

    private suspend fun getTopRatedPaged(page: Int): List<TopRated> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getTopRatedListPaged(page)
        }
        val favoriteIds = getFavorites().map { it.id.toInt() }
        val model = response.body()!!.topRated
        val modifiedList = mutableListOf<TopRated>()
        modifiedList.addAll(model)
        modifiedList.forEach {
            if (favoriteIds.contains(it.id?.toInt())) {
                it.favorite = true
            }
        }
        firstPosterLink.update { model.first().poster }
        return modifiedList
        // return list
    }

    private suspend fun getTopTvShows(page: Int): List<Movie> {
        val response = withContext(Dispatchers.IO) {
            NetworkCore.moviesAPI.getTvShowsList(page)
        }
        val favoriteIds = getFavorites().map { it.id.toInt() }
        val model = response.body()!!.movies
        val modifiedList = mutableListOf<Movie>()
        modifiedList.addAll(model)
        modifiedList.forEach {
            if (favoriteIds.contains(it.id.toInt())) {
                it.favorite = true
            }
        }
        return modifiedList
    }

    suspend fun getFavorites() = suspendCancellableCoroutine { continuation ->
        val db = Firebase.database
        val result = db.getReference(Constants.FAVORITES)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Movie>()
                snapshot.children.forEach {
                    val model = it.toModel<Movie>()
                    model?.let { list.add(it) }
                }

                stateFlow.update { list }
                if (continuation.isActive) continuation.resume(list) {}
            }
        }

        result.addValueEventListener(listener)
    }
}