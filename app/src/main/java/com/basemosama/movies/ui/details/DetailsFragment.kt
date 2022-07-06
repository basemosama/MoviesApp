package com.basemosama.movies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.basemosama.movies.databinding.FragmentDetailsBinding
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var detailsBinding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by activityViewModels()
    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        detailsBinding = FragmentDetailsBinding.inflate(inflater,container,false)
        return detailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { 
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        getCurrentMovie()
     }

    private fun setUpUI(){
        val id = args.movieId
        viewModel.setCurrentId(id)
    }

    private fun getCurrentMovie(){
        repeatOnLifeCycle(viewModel.currentMovie){
            detailsBinding.movie = it
        }
    }





}