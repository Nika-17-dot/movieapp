package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val API_KEY = "4d45a81415d029f781ab09d7707cd50b"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        loadPopularMovies()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchMovies(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadPopularMovies()
                }
                return true
            }
        })


        binding.btnGoToFavorites.setOnClickListener {
            findNavController().navigate(R.id.favoritesFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun loadPopularMovies() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getPopularMovies(API_KEY)
                withContext(Dispatchers.Main) {
                    if (_binding != null && response.isSuccessful) {
                        val movies = response.body()?.results ?: emptyList()
                        updateAdapter(movies)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "შეცდომა ჩატვირთვისას", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun searchMovies(query: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.searchMovies(API_KEY, query)
                withContext(Dispatchers.Main) {
                    if (_binding != null && response.isSuccessful) {
                        val movies = response.body()?.results ?: emptyList()
                        updateAdapter(movies)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "ძებნა ვერ მოხერხდა", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun updateAdapter(movies: List<com.example.movieapp.network.Movie>) {
        binding.rvMovies.adapter = MovieAdapter(
            movies,
            onMovieClick = { movie ->
                val bundle = Bundle().apply {
                    putInt("id", movie.id)
                    putString("title", movie.title)
                    putString("overview", movie.overview)
                    putString("poster", movie.poster_path)
                }
                findNavController().navigate(R.id.detailsFragment, bundle)
            },
            onMovieLongClick = {

            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}