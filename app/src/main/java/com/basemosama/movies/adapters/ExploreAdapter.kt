package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreItem
import com.basemosama.movies.databinding.ItemExploreBinding

class ExploreAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<ExploreItem,
        ExploreAdapter.ExploreViewHolder>(DIFF_CALLBACK) {


    interface ItemClickListener {
        fun onMoreClickListener(item: ExploreInfo?)

        fun onMovieClickListener(item: Movie?)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        return ExploreViewHolder.from(parent,itemClickListener)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ExploreViewHolder(
        private val exploreBinding: ItemExploreBinding,
        private val itemClickListener: ItemClickListener,
    ) :
        RecyclerView.ViewHolder(exploreBinding.root), MovieClickListener, View.OnClickListener {
        private var recyclerView : RecyclerView = exploreBinding.exploreMoviesRv
        private var moviesAdapter : MovieAdapter = MovieAdapter(this)

        init {
            exploreBinding.moreBtn.setOnClickListener(this)
            recyclerView.apply {
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setRecycledViewPool(RecyclerView.RecycledViewPool())
                isNestedScrollingEnabled = false
                recyclerView.adapter = moviesAdapter
            }
        }

        fun bind(item: ExploreItem) {
            exploreBinding.explore = item.explore
            if (item.movies !=null) moviesAdapter.submitList(item.movies)
        }
        override fun onClick(p0: View?) {
            val item = exploreBinding.explore
            itemClickListener.onMoreClickListener(item)
        }

        override fun onMovieClickListener(movie: Movie?) {
            itemClickListener.onMovieClickListener(movie)
        }

        companion object {
            fun from(parent: ViewGroup,itemClickListener: ItemClickListener): ExploreViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemExploreBinding = ItemExploreBinding.inflate(inflater, parent, false)
                return ExploreViewHolder(itemExploreBinding,itemClickListener)
            }
        }




    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExploreItem>() {
            override fun areItemsTheSame(oldItem: ExploreItem, newItem: ExploreItem): Boolean {
                return oldItem.explore.exploreId == newItem.explore.exploreId
            }

            override fun areContentsTheSame(oldItem: ExploreItem, newItem: ExploreItem): Boolean {
                return oldItem.explore == newItem.explore
            }
        }


    }


}

