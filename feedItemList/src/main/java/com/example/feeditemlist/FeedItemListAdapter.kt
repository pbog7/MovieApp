package com.example.feeditemlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.*
import com.example.common.domain.models.FeedItemType
import com.example.feeditemlist.databinding.AdLayoutBinding
import com.example.feeditemlist.databinding.FeedItemLayoutBinding
import com.example.feeditemlist.databinding.SearchItemLayoutBinding
import javax.inject.Inject

class FeedItemListAdapter @Inject constructor() :
    ListAdapter<FeedItem, RecyclerView.ViewHolder>(feedItemDiffUtil) {
    var actionListener: FeedItemEventListener? = null
    var buttonClickEnabled = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FeedItemType.AD.type -> AdViewHolder(
                AdLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedItemType.MOVIE.type -> FeedItemViewHolder(
                FeedItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> SearchViewHolder(
                SearchItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            FeedItemType.AD.type -> (holder as AdViewHolder).bind(getItem(position) as Ad)
            FeedItemType.MOVIE.type -> (holder as FeedItemViewHolder).bind(
                getItem(position) as Movie,
                position
            )
            else -> (holder as SearchViewHolder).bind(getItem(position) as Movie, position)

        }


    inner class FeedItemViewHolder(
        private val binding: FeedItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, position: Int) {
            with(binding) {
                feedItemConstraintLayout.setOnClickListener {
                    actionListener?.onItemClick(movie)
                }
                movie.apply {
                    itemImage.apply {
                        Glide
                            .with(context)
                            .load(image)
                            .transform(
                                RoundedCorners(5)
                            )
                            .fitCenter()
                            .placeholder(R.drawable.placeholder)
                            .into(this)
                    }
                    favoriteButton.apply {
                        setOnClickListener {
                            if (buttonClickEnabled) {
                                actionListener?.onFavoriteClick(movie, position)
                            }
                        }
                        visibility = View.VISIBLE
                        when (favorite) {
                            true -> setImageResource(R.drawable.favorite_icon_selected)
                            false -> setImageResource(R.drawable.favorite_icon_not_selected)
                            null -> setImageResource(R.drawable.remove_icon)
                        }
                    }
                    titleTextView.text = title
                    ratingTextView.text = imdbRating.toString()
                }
            }
        }
    }

    inner class SearchViewHolder(
        private val binding: SearchItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, position: Int) {
            with(binding) {
                feedItemConstraintLayout.setOnClickListener {
                    actionListener?.onItemClick(movie)
                }
                movie.apply {
                    itemImage.apply {
                        Glide
                            .with(context)
                            .load(image)
                            .transform(
                                RoundedCorners(5)
                            )
                            .placeholder(R.drawable.placeholder)
                            .into(this)
                    }
                    favoriteButton.apply {
                        setOnClickListener {
                            if (buttonClickEnabled) {
                                actionListener?.onFavoriteClick(movie, position)
                            }
                        }
                        visibility = View.VISIBLE
                        when (favorite) {
                            true -> setImageResource(R.drawable.favorite_icon_selected)
                            false -> setImageResource(R.drawable.favorite_icon_not_selected)
                            null -> setImageResource(R.drawable.remove_icon)
                        }
                    }
                    titleTextView.text = title
                    descriptionTextView.text = description
                }
            }
        }

    }

    inner class AdViewHolder(
        private val binding: AdLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ad: Ad) {
            with(binding) {
                ad.apply {
                    adImage.apply {
                        Glide
                            .with(context)
                            .load(image)
                            .transform(
                                RoundedCorners(5)
                            )
                            .placeholder(R.drawable.placeholder)
                            .into(this)
                    }
                    adTitle.text = title
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position).feedItemType) {
            FeedItemType.AD -> FeedItemType.AD.type
            FeedItemType.MOVIE -> FeedItemType.MOVIE.type
            FeedItemType.SEARCH -> FeedItemType.SEARCH.type
        }


    companion object {
        val feedItemDiffUtil = object : DiffUtil.ItemCallback<FeedItem>() {
            override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean =
                oldItem.feedItemType == newItem.feedItemType && oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem) =
                oldItem == newItem

        }
    }
}