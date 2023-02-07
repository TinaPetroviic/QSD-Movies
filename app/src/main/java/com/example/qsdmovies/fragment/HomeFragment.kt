package com.example.qsdmovies.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.map
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.qsdmovies.R
import com.example.qsdmovies.activity.MovieDetailsActivity
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.databinding.FragmentHomeBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.observe
import com.example.qsdmovies.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.movie_item.*
import timber.log.Timber

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private var isInMyFavorite = false
    private var id = ""

    private lateinit var viewModel: HomeViewModel

    private val movieAdapter = MovieAdapter {
        openMovieActivity(it)
    }
    private val topRatedAdapter = MovieAdapter {
        openMovieActivity(it)
    }
    private val tvShowsAdapter = MovieAdapter {
        openMovieActivity(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {

            checkIsFavorite()

        }

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check_box?.setOnClickListener {

            if (isInMyFavorite) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }
        }

        initFlows()
        binding.rvWatching.adapter = movieAdapter
        binding.rvPopularMovies.adapter = movieAdapter
        binding.rvTopRated.adapter = topRatedAdapter
        binding.rvTelevisionShows.adapter = tvShowsAdapter
    }

    private fun initFlows() {
        viewModel.stateFlow.observe(viewLifecycleOwner) {
            movieAdapter.submitData(it)

        }

        viewModel.stateFlowTopRated.observe(viewLifecycleOwner) { it ->
            topRatedAdapter.submitData(it.map {
                Movie(
                    it.id?.toLong() ?: -1,
                    it.title ?: "",
                    it.overview,
                    it.poster ?: "",
                    it.vote.toFloat()
                )
            })
        }

        viewModel.stateFlowTvShows.observe(viewLifecycleOwner) {
            tvShowsAdapter.submitData(it)
        }

        viewModel.firstPosterLink.observe(viewLifecycleOwner) {
            it?.let { loadPoster(it) }
        }
    }

    private fun loadPoster(link: String) {
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w342$link")
            .transform(CenterCrop())
            .into(binding.ivPoster)
    }

    private fun openMovieActivity(model: Movie) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java).apply {
            putExtra(Constants.MOVIE_TITLE, model.title)
            putExtra(Constants.MOVIE_RATING, model.rating)
            putExtra(Constants.MOVIE_OVERVIEW, model.overview)
            putExtra(Constants.MOVIE_POSTER, model.poster)
            putExtra(Constants.MOVIE_ID, model.id.toInt())
        }

        requireActivity().startActivity(intent)
    }

        private fun addToFavorite() {

            Timber.d("addToFavorite: Adding to fav")
            val timestamp = System.currentTimeMillis()
            val hashMap = HashMap<String, Any>()
            hashMap["movieId"] = id
            hashMap["timestamp"] = timestamp

            val ref = FirebaseDatabase.getInstance().getReference("User")
            ref.child(auth.uid!!).child("Favorites").child(id).setValue(hashMap)
                .addOnSuccessListener {

                    Timber.d("addToFavorite: Added to favorite")
                    Toast.makeText(
                        requireContext(),
                        "Added to favorite",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                .addOnFailureListener { e ->

                    Timber.d("addToFavorite: Failed to add to fav due to ${e.message}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to add to fav due to ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        }

        private fun removeFromFavorite() {
            Timber.d("removeFromFavorite: Removing from fav")

            val ref = FirebaseDatabase.getInstance().getReference("User")
            ref.child(auth.uid!!).child("Favorites").child(id)
                .removeValue()
                .addOnSuccessListener {

                    Timber.d("removeFromFavorite: Removed from favorite")
                    Toast.makeText(
                        requireContext(),
                        "Removed from favorite",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                .addOnFailureListener { e ->

                    Timber.d("removeFromFavorite: Failed to remove from fav due to ${e.message}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to remove from fav due to ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()


                }
        }

        private fun checkIsFavorite() {
            Timber.d("checkIsFavorite: Checking if book is in fav or not")

            val ref = FirebaseDatabase.getInstance().getReference("User")
            ref.child(auth.uid!!).child("Favorites").child(id)
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        isInMyFavorite = snapshot.exists()
                        if (isInMyFavorite) {

                            Timber.d("onDataChange: available in favorite")

                            check_box?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                R.drawable.ic_heart_filled,
                                0,
                                0
                            )
                            check_box?.text = "Remove Favorite"


                        } else {

                            Timber.d("onDataChange: not available in favorite")


                            check_box?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                R.drawable.ic_heart_outlined,
                                0,
                                0
                            )
                            check_box?.text = "Add Favorite"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
