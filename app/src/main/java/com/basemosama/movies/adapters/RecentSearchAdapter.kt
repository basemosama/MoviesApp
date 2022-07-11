package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.model.search.RecentSearch
import com.basemosama.movies.databinding.ItemRecentSearchBinding

class RecentSearchAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<RecentSearch,
        RecentSearchAdapter.RecentSearchViewHolder>(DIFF_CALLBACK) {


    interface ItemClickListener {
        fun onRecentSearchClickListener(item: RecentSearch?)

        fun onCopyRecentSearchClickListener(item: RecentSearch?)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        return RecentSearchViewHolder.from(parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }




    class RecentSearchViewHolder(
        private val recentSearchBinding: ItemRecentSearchBinding,
        private val itemClickListener: ItemClickListener,
    ) :
        RecyclerView.ViewHolder(recentSearchBinding.root),  View.OnClickListener {

        init {
            recentSearchBinding.root.setOnClickListener(this)
            recentSearchBinding.recentSearchCopyImage.setOnClickListener(){
                itemClickListener.onCopyRecentSearchClickListener(recentSearchBinding.search)
            }
        }

        fun bind(item: RecentSearch) {
            recentSearchBinding.search = item

        }

        override fun onClick(p0: View?) {
            itemClickListener.onRecentSearchClickListener(recentSearchBinding.search)
        }


        companion object {
            fun from(parent: ViewGroup, itemClickListener: ItemClickListener): RecentSearchViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemRecentSearchBinding = ItemRecentSearchBinding.inflate(inflater, parent, false)
                return RecentSearchViewHolder(itemRecentSearchBinding, itemClickListener)
            }
        }


    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecentSearch>() {
            override fun areItemsTheSame(oldItem: RecentSearch, newItem: RecentSearch): Boolean {
                return oldItem.query == newItem.query
            }

            override fun areContentsTheSame(oldItem: RecentSearch, newItem: RecentSearch): Boolean {
                return oldItem == newItem
            }
        }


    }


}

