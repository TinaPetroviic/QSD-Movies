package com.example.qsdmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.databinding.FragmentFavoritesBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.observe
import com.example.qsdmovies.util.pager
import com.example.qsdmovies.util.toModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    val stateFlow = pager(config = PagingConfig(pageSize = 5)) {
        getFavorites()
    }.flow.cachedIn(lifecycleScope)

    private val adapter = MovieAdapter(
        onMovieTap = {},
        onLikeTap = { handleFavorites(it) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavorites.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        initFlow()
    }

    private fun initFlow() {
        stateFlow.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }
    }

    private suspend fun getFavorites() = suspendCancellableCoroutine { cont ->
        val db = Firebase.database
        val result = db.getReference(Constants.FAVORITES)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        result.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                cont.resumeWithException(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Movie>()
                snapshot.children.forEach {
                    val model = it.toModel<Movie>()
                    model?.let { list.add(it) }
                }

                if (cont.isActive) cont.resume(list) {} else cont.cancel()
            }
        })
    }

    private fun handleFavorites(model: Movie) {
        val db = Firebase.database
        val result = db.getReference(Constants.FAVORITES)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(model.id.toString())

        if (model.favorite) result.setValue(model) else result.removeValue()
    }
}