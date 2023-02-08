package com.example.qsdmovies.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qsdmovies.activity.MovieDetailsActivity
import com.example.qsdmovies.adapter.SimpleMovieAdapter
import com.example.qsdmovies.databinding.FragmentFavoritesBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.toModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.callbackFlow

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val adapter = SimpleMovieAdapter(
        listOf(),
        onMovieTap = { openMovieActivity(it) },
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

        getFavorites()
    }

    private fun getFavorites() {
        val db = Firebase.database
        val result = db.getReference(Constants.FAVORITES)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        result.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Movie>()
                snapshot.children.forEach {
                    val model = it.toModel<Movie>()
                    model?.let { list.add(it) }
                }

                adapter.setMovies(list)
            }
        })
    }

    private fun getFavoritesFlow() = callbackFlow<List<Movie>> {
        val db = Firebase.database
        val result = db.getReference(Constants.FAVORITES)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        result.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                // cont.resumeWithException(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Movie>()
                snapshot.children.forEach {
                    val model = it.toModel<Movie>()
                    model?.let { list.add(it) }
                }

                trySend(list)
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

    private fun openMovieActivity(model: Movie) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java).apply {
            putExtra(Constants.MOVIE_TITLE, model.title)
            putExtra(Constants.MOVIE_RATING, model.vote_average)
            putExtra(Constants.MOVIE_OVERVIEW, model.overview)
            putExtra(Constants.MOVIE_POSTER, model.poster_path)
            putExtra(Constants.MOVIE_ID, model.id.toInt())
        }

        requireActivity().startActivity(intent)
    }
}