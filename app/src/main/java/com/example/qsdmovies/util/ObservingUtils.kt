package com.example.qsdmovies.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline action: suspend (t: T) -> Unit
) = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        .collect { action(it) }
}