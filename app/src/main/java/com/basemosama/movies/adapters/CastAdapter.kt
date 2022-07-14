package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.model.details.Artist
import com.basemosama.movies.databinding.ItemCastBinding

class CastAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<Artist,
        CastAdapter.CastViewHolder>(DIFF_CALLBACK) {


    interface ItemClickListener {
        fun onArtistClickListener(item: Artist?)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastAdapter.CastViewHolder.from(parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }




    class CastViewHolder(
        private val itemCastBinding : ItemCastBinding,
        private val itemClickListener: ItemClickListener,
    ) :
        RecyclerView.ViewHolder(itemCastBinding.root),  View.OnClickListener {

        init {
            itemCastBinding.root.setOnClickListener(this)
        }

        fun bind(item: Artist) {
            itemCastBinding.artist = item

        }

        override fun onClick(p0: View?) {
            itemClickListener.onArtistClickListener(itemCastBinding.artist)
        }


        companion object {
            fun from(parent: ViewGroup, itemClickListener: ItemClickListener): CastViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemCastBinding = ItemCastBinding.inflate(inflater, parent, false)
                return CastViewHolder(
                    itemCastBinding,
                    itemClickListener
                )
            }
        }


    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }


    }


}

