package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.databinding.FragmentFavoritesBinding
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.db.FavoriteMovie
import com.example.movieapp.network.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        setupRecyclerView()

        val backBtn = view.findViewById<ImageButton>(R.id.btnBack)
        backBtn?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun loadFavorites() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                val favoriteMovies = db.movieDao().getAllFavorites()

                val moviesList = favoriteMovies.map { fav ->
                    Movie(
                        id = fav.id,
                        title = fav.title,
                        poster_path = fav.posterPath ?: "",
                        overview = fav.overview ?: ""
                    )
                }

                withContext(Dispatchers.Main) {
                    if (_binding != null) {

                        binding.rvFavorites.adapter = MovieAdapter(
                            movies = moviesList,
                            onMovieClick = { selectedMovie ->
                                val bundle = Bundle().apply {
                                    putInt("id", selectedMovie.id)
                                    putString("title", selectedMovie.title)
                                    putString("overview", selectedMovie.overview)
                                    putString("poster", selectedMovie.poster_path)
                                }
                                findNavController().navigate(R.id.detailsFragment, bundle)
                            },
                            onMovieLongClick = { movieToDelete ->
                                showDeleteDialog(movieToDelete)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "შეცდომა ჩატვირთვისას", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDeleteDialog(movie: Movie) {
        AlertDialog.Builder(requireContext())
            .setTitle("წაშლა")
            .setMessage("ნამდვილად გსურთ '${movie.title}'-ის წაშლა ფავორიტებიდან?")
            .setPositiveButton("დიახ") { _, _ ->
                deleteMovie(movie)
            }
            .setNegativeButton("არა", null)
            .show()
    }

    private fun deleteMovie(movie: Movie) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())

            val favoriteToDelete = FavoriteMovie(
                id = movie.id,
                title = movie.title,

                posterPath = movie.poster_path ?: "",
                overview = movie.overview ?: ""
            )

            db.movieDao().deleteFavorite(favoriteToDelete)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}