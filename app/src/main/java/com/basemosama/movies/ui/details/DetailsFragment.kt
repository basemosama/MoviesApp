package com.basemosama.movies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.basemosama.movies.databinding.FragmentDetailsBinding
import com.basemosama.movies.ui.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var detailsBinding: FragmentDetailsBinding
    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        detailsBinding = FragmentDetailsBinding.inflate(inflater,container,false)
        return detailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { 
        super.onViewCreated(view, savedInstanceState)
        getCurrentMovie()
     }



    private fun getCurrentMovie(){
        viewModel.currentMovie.observe(viewLifecycleOwner){
            detailsBinding.movie = it
        }
    }





}