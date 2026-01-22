package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.FragmentDetailsBinding
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.db.FavoriteMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)


        val movieId = arguments?.getInt("id") ?: -1
        val title = arguments?.getString("title") ?: "Unknown"
        val overview = arguments?.getString("overview") ?: "No description available"
        val poster = arguments?.getString("poster") ?: ""


        binding.tvDetailsTitle.text = title
        binding.tvDetailsOverview.text = overview

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$poster")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(binding.ivDetailsPoster)


        val backBtn = view.findViewById<ImageButton>(R.id.btnBack)
        backBtn?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        val favBtn = view.findViewById<ImageButton>(R.id.btnAddToFavorites)
        favBtn?.setOnClickListener {
            if (movieId != -1) {
                saveToFavorites(movieId, title, poster, overview)
            } else {
                Toast.makeText(requireContext(), "შეცდომა: ID ვერ მოიძებნა", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToFavorites(id: Int, title: String, poster: String, overview: String) {
        val favoriteMovie = FavoriteMovie(
            id = id,
            title = title,
            posterPath = poster,
            overview = overview
        )
        println("DEBUG_ID: Saving movie with ID: $id")

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                db.movieDao().insertFavorite(favoriteMovie)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "«$title» დაემატა ფავორიტებში!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                    Toast.makeText(requireContext(), "შეცდომა შენახვისას", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}