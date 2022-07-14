package com.basemosama.movies.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.basemosama.movies.adapters.ExploreAdapter
import com.basemosama.movies.adapters.SliderAdapter
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreItem
import com.basemosama.movies.data.model.utils.SortOrder
import com.basemosama.movies.databinding.FragmentExploreBinding
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ExploreFragment : Fragment(), ExploreAdapter.ItemClickListener,
    SliderAdapter.SliderItemClickListener {

    private lateinit var exploreBinding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by activityViewModels()
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var sliderAdapter: SliderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        exploreBinding = FragmentExploreBinding.inflate(inflater, container, false)
        setupUI()
        return exploreBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getExploreItems()
        setUpSlider()
        handleLoadingState()

    }

    private fun setupUI() {
        exploreAdapter = ExploreAdapter(this)
        exploreBinding.exploreRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = exploreAdapter
            setHasFixedSize(true)

        }
    }


    private fun setUpSlider() {
        sliderAdapter = SliderAdapter(emptyList(), this)

        exploreBinding.pager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = sliderAdapter

            getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
            exploreBinding.dotsIndicator.attachTo(exploreBinding.pager)
        }
//

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getTickerFlow(3000, 3000).collectLatest {
                    exploreBinding.pager.setCurrentItem(it, true)
                }

            }

        }


    }


    private fun handleLoadingState(){
        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading->
            exploreBinding.loadingView.isVisible = isLoading
            exploreBinding.appBar.isVisible = !isLoading
            exploreBinding.exploreRv.isVisible = !isLoading
        }
    }


    private fun getExploreItems() {
        repeatOnLifeCycle(viewModel.exploreItems) {
            val exploreItems = it.map { (explore, movies) ->
                //if i had api i would create an endpoint for this and get the data from there
                if (explore.sortOrder == SortOrder.NOW_PLAYING) {
                    setUpSliderItems(movies)
                }
                ExploreItem(explore, movies)
            }.toList()

            exploreAdapter.submitList(exploreItems)
            viewModel.setIsLoading(false)
        }
    }


    private fun setUpSliderItems(movies: List<Movie>) {
        val sliderMovies = movies.sortedBy { movie ->
            movie.popularity
        }.take(5)
        sliderAdapter.updateList(sliderMovies)
        viewModel.sliderItemsCount.value = sliderMovies.size

    }

    override fun onMoreClickListener(item: ExploreInfo?) {

        item?.let {
            val action = ExploreFragmentDirections.actionExploreFragmentToMoviesFragment(
                it.title,
                it.sortOrder
            )
            findNavController().navigate(action)
        }

    }

    override fun onMovieClickListener(item: Movie?) {
        item?.id?.let { id ->
            val action = ExploreFragmentDirections.actionExploreFragmentToDetailsFragment(id)
            findNavController().navigate(action)
        }
    }

    override fun onSliderMovieClickListener(movie: Movie?) {
        movie?.id?.let { id ->
            val action = ExploreFragmentDirections.actionExploreFragmentToDetailsFragment(id)
            findNavController().navigate(action)
        }
    }


}



