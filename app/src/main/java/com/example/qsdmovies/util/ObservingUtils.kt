package com.example.qsdmovies.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline action: suspend (t: T) -> Unit
) = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        .collect { action(it) }
}

inline fun <reified T> DataSnapshot.toModel(): T? {

    value ?: return null
    val responseString = Gson().toJson(value)
    return Gson().fromJson(responseString, T::class.java)
}