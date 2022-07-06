package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.ExploreItem
import com.basemosama.movies.databinding.ItemExploreBinding
import com.basemosama.movies.ui.explore.ExploreViewModel
import com.basemosama.movies.utils.repeatOnLifeCycle
import timber.log.Timber

class ExploreAdapter(private val itemClickListener: ItemClickListener,
                     private val viewModel :ExploreViewModel,
                     private val fragment:Fragment) : ListAdapter<ExploreItem,
        ExploreAdapter.ExploreViewHolder>(DIFF_CALLBACK) {

    interface ItemClickListener {
        fun onMoreClickListener(item: ExploreItem?)

        fun onMovieClickListener(item: Movie?)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        return ExploreViewHolder.from(parent,itemClickListener,viewModel,fragment)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ExploreViewHolder(
        private val exploreBinding: ItemExploreBinding,
        private val itemClickListener: ItemClickListener,
        private val viewModel :ExploreViewModel,
        private val fragment: Fragment
    ) :
        RecyclerView.ViewHolder(exploreBinding.root), MovieClickListener, View.OnClickListener {
        private var recyclerView : RecyclerView = exploreBinding.exploreMoviesRv
        private var pagingAdapter : PagingMovieAdapter = PagingMovieAdapter(this)

        init {
            exploreBinding.moreBtn.setOnClickListener(this)
            recyclerView.apply {
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setRecycledViewPool(RecyclerView.RecycledViewPool())
                recyclerView.adapter = pagingAdapter
            }
        }

        fun bind(item: ExploreItem) {
            exploreBinding.explore = item
            Timber.d("MOVIES BINDING :${item.title}")

            fragment.repeatOnLifeCycle(viewModel.getMovies(item.sortOrder)) {
                Timber.d("MOVIES BINDING ADAPTER :${item.title}")

                pagingAdapter.submitData(it)
            }

        }
        override fun onClick(p0: View?) {
            val item = exploreBinding.explore
            itemClickListener.onMoreClickListener(item)
        }

        override fun onMovieClickListener(movie: Movie?) {
            itemClickListener.onMovieClickListener(movie)
        }

        companion object {
            fun from(parent: ViewGroup,itemClickListener: ItemClickListener, viewModel: ExploreViewModel,fragment: Fragment): ExploreViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemExploreBinding = ItemExploreBinding.inflate(inflater, parent, false)
                return ExploreViewHolder(itemExploreBinding,itemClickListener,viewModel,fragment)
            }
        }




    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExploreItem>() {
            override fun areItemsTheSame(oldItem: ExploreItem, newItem: ExploreItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ExploreItem, newItem: ExploreItem): Boolean {
                return oldItem == newItem
            }
        }


    }


}

