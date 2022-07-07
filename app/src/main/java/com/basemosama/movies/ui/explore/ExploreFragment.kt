package com.basemosama.movies.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basemosama.movies.adapters.ExploreAdapter
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreItem
import com.basemosama.movies.databinding.FragmentExploreBinding
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(), ExploreAdapter.ItemClickListener {

    private lateinit var exploreBinding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by activityViewModels()
    private lateinit  var exploreAdapter :ExploreAdapter

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
    }

    private fun setupUI() {
        exploreAdapter = ExploreAdapter(this)
        exploreBinding.exploreRv.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = exploreAdapter
        }
    }

    private fun getExploreItems() {
        repeatOnLifeCycle(viewModel.exploreItems){
           val exploreItems= it.map { (explore,movies) ->
                ExploreItem(explore,movies)
            }.toList()

            exploreAdapter.submitList(exploreItems)
            }
    }


    override fun onMoreClickListener(info: ExploreInfo?) {
        Toast.makeText(context, "More Clicked", Toast.LENGTH_SHORT).show()

        info?.sortOrder?.let { sort->
            val action = ExploreFragmentDirections.actionExploreFragmentToMoviesFragment(sort)
            findNavController().navigate(action)
        }

    }

    override fun onMovieClickListener(movie: Movie?) {
        movie?.id?.let { id->
            val action = ExploreFragmentDirections.actionExploreFragmentToDetailsFragment(id)
            findNavController().navigate(action)
        }
    }


}
