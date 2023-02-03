package com.example.qsdmovies.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.databinding.FragmentHomeBinding
import com.example.qsdmovies.models.*
import com.example.qsdmovies.network.NetworkCore
import com.example.qsdmovies.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.movie_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    val TAG = "HomeFragment"
    private var id = ""
    private var isInMyFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("HomeFragment").d("onCreate")

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

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {

            checkIsFavorite()

        }


        getMovieData { movies: List<Movie> ->
            Glide.with(this)
                .load(Constants.IMAGE_BASE + movies.first().poster)
                .into(binding.ivPoster)
            binding.rvWatching.adapter = MovieAdapter(movies.map { it.poster })
        }
        getTopRatedList { topRated: List<TopRated> ->
            binding.rvTopRated.adapter = MovieAdapter(topRated.map { it.poster })
        }
        getTvShowsList { tvShows: List<TvShows> ->
            binding.rvTelevisionShows.adapter = MovieAdapter(tvShows.map { it.poster })
        }
        getMovieData { movies: List<Movie> ->
            binding.rvPopularMovies.adapter = MovieAdapter(movies.map { it.poster })
        }

        favorite?.setOnClickListener {

            if (isInMyFavorite) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        NetworkCore.moviesAPI.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun getTopRatedList(callback: (List<TopRated>) -> Unit) {
        NetworkCore.moviesAPI.getTopRatedList().enqueue(object : Callback<TopRatedResponse> {
            override fun onResponse(
                call: Call<TopRatedResponse>,
                response: Response<TopRatedResponse>
            ) {
                return callback(response.body()!!.topRated)
            }

            override fun onFailure(call: Call<TopRatedResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun getTvShowsList(callback: (List<TvShows>) -> Unit) {
        NetworkCore.moviesAPI.getTvShowsList().enqueue(object : Callback<TvShowsResponse> {
            override fun onResponse(
                call: Call<TvShowsResponse>,
                response: Response<TvShowsResponse>
            ) {
                return callback(response.body()!!.tvShows)
            }

            override fun onFailure(call: Call<TvShowsResponse>, t: Throwable) {
                Timber.e(t)
                t.printStackTrace()
            }
        })
    }

    private fun addToFavorite() {

        Timber.tag(TAG).d("addToFavorite: Adding to fav")
        val timestamp = System.currentTimeMillis()
        val hashMap = HashMap<String, Any>()
        hashMap["movieId"] = id
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(auth.uid!!).child("Favorites").child(id).setValue(hashMap)
            .addOnSuccessListener {

                Timber.tag(TAG).d("addToFavorite: Added to favorite")
                Toast.makeText(
                    requireContext(),
                    "Added to favorite",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->

                Timber.tag(TAG).d("addToFavorite: Failed to add to fav due to ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Failed to add to fav due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun removeFromFavorite() {
        Timber.tag(TAG).d("removeFromFavorite: Removing from fav")

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(auth.uid!!).child("Favorites").child(id)
            .removeValue()
            .addOnSuccessListener {

                Timber.tag(TAG).d("removeFromFavorite: Removed from favorite")
                Toast.makeText(
                    requireContext(),
                    "Removed from favorite",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->

                Timber.tag(TAG)
                    .d("removeFromFavorite: Failed to remove from fav due to ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Failed to remove from fav due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()


            }
    }

    private fun checkIsFavorite() {
        Timber.tag(TAG).d("checkIsFavorite: Checking if book is in fav or not")

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(auth.uid!!).child("Favorites").child(id)
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyFavorite = snapshot.exists()
                    if (isInMyFavorite) {

                        Timber.tag(TAG).d("onDataChange: available in favorite")

                        favorite?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_heart_filled,
                            0,
                            0
                        )
                        favorite?.text = "Remove Favorite"


                    } else {

                        Timber.tag(TAG).d("onDataChange: not available in favorite")


                        favorite?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_heart_outlined,
                            0,
                            0
                        )
                        favorite?.text = "Add Favorite"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }
}