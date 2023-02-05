package com.example.qsdmovies.util

import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.*

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun SearchView.afterTextChangedDebounce(delayMillis: Long, input: (String) -> Unit) {
    var lastInput = ""
    var debounceJob: Job? = null
    val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(p0: String?): Boolean {
            if (p0 != null) {
                val newtInput = p0.toString()
                debounceJob?.cancel()
                if (lastInput != newtInput) {
                    lastInput = newtInput
                    debounceJob = uiScope.launch {
                        delay(delayMillis)
                        if (lastInput == newtInput) {
                            input(newtInput)
                        }
                    }
                }
            }

            return true
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return true
        }
    })
}