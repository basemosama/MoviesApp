package com.basemosama.movies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basemosama.movies.R
import com.basemosama.movies.adapters.CastAdapter
import com.basemosama.movies.adapters.ExploreViewType
import com.basemosama.movies.adapters.MovieAdapter
import com.basemosama.movies.adapters.MovieClickListener
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.details.Artist
import com.basemosama.movies.databinding.FragmentDetailsBinding
import com.basemosama.movies.utils.Resource
import com.basemosama.movies.utils.repeatOnLifeCycle
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(), MovieClickListener, CastAdapter.ItemClickListener {
    private lateinit var detailsBinding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by activityViewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val similarAdapter = MovieAdapter(this, ExploreViewType.POSTER)
    private val recommendedAdapter = MovieAdapter(this, ExploreViewType.POSTER)
    private val castAdapter = CastAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return detailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpToolbar()
        getCurrentMovie()
    }

    private fun setUpUI() {
        val id = args.movieId
        viewModel.setCurrentId(id)
        detailsBinding.apply {
            similarRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = similarAdapter
                setHasFixedSize(true)
            }

            recommendedRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = recommendedAdapter
                setHasFixedSize(true)
            }

            castRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = castAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun getCurrentMovie() {
        repeatOnLifeCycle(viewModel.currentMovie) { resource ->
            detailsBinding.loadingView.isVisible = resource is Resource.Loading
            detailsBinding.detailsLayout.isVisible = resource !is Resource.Loading
            detailsBinding.appBar.isVisible = resource !is Resource.Loading

            if (resource !is Resource.Loading) {
                val details = resource.data
                detailsBinding.detailsLayout.fullScroll(View.FOCUS_UP);
                detailsBinding.detailsLayout.smoothScrollTo(0,0)
                detailsBinding.appBar.setExpanded(true)
                detailsBinding.movie = details?.movie
                detailsBinding.toolbar.title = details?.movie?.title

                val cast = details?.cast
                castAdapter.submitList(cast)

                val similarMovies = details?.similarMovies
                similarAdapter.submitList(similarMovies)

                val recommendedMovies = details?.recommendedMovies
                recommendedAdapter.submitList(recommendedMovies)


                val genres = details?.genres
                genres?.forEach { genre ->
                    if (!genre.name.isNullOrEmpty()) {
                        val chip = Chip(context)
                        chip.text = genre.name
                        detailsBinding.genreChips.addView(chip)
                    }

                }
            }
            if (resource is Resource.Error) {
                Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setUpToolbar() {
        detailsBinding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_navigate_back)
            setNavigationOnClickListener {
                (requireActivity() as AppCompatActivity).onSupportNavigateUp()
            }
        }
    }

    override fun onMovieClickListener(movie: Movie?) {
        movie?.id?.let { viewModel.setCurrentId(it) }
    }

    override fun onArtistClickListener(item: Artist?) {
        Toast.makeText(context, item?.name, Toast.LENGTH_SHORT).show()
    }


}